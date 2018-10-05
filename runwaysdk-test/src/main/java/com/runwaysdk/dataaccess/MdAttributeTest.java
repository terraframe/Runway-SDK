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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.runwaysdk.ProblemException;
import com.runwaysdk.ProblemIF;
import com.runwaysdk.constants.Constants;
import com.runwaysdk.constants.EnumerationMasterInfo;
import com.runwaysdk.constants.IndexTypes;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.constants.MdAttributeClobInfo;
import com.runwaysdk.constants.MdAttributeConcreteInfo;
import com.runwaysdk.constants.MdAttributeDateInfo;
import com.runwaysdk.constants.MdAttributeEnumerationInfo;
import com.runwaysdk.constants.MdAttributeFloatInfo;
import com.runwaysdk.constants.MdAttributeIntegerInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdAttributeReferenceInfo;
import com.runwaysdk.constants.MdAttributeTextInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.MdClassInfo;
import com.runwaysdk.constants.MdEnumerationInfo;
import com.runwaysdk.constants.MdIndexInfo;
import com.runwaysdk.constants.TypeInfo;
import com.runwaysdk.dataaccess.attributes.AttributeLengthCharacterException;
import com.runwaysdk.dataaccess.attributes.AttributeValueException;
import com.runwaysdk.dataaccess.attributes.AttributeValueProblem;
import com.runwaysdk.dataaccess.attributes.InvalidReferenceException;
import com.runwaysdk.dataaccess.attributes.entity.AttributeEnumeration;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.metadata.AttributeDefinitionDecimalException;
import com.runwaysdk.dataaccess.metadata.AttributeDefinitionLengthException;
import com.runwaysdk.dataaccess.metadata.AttributeInvalidUniquenessConstraintException;
import com.runwaysdk.dataaccess.metadata.MdAttributeBooleanDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeClobDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeConcreteDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDateDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeEnumerationDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeFloatDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeIntegerDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeReferenceDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeTextDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdEnumerationDAO;
import com.runwaysdk.dataaccess.metadata.MdIndexDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.query.BusinessDAOQuery;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.Request;

public class MdAttributeTest
{
  private static MdEnumerationDAOIF mdEnumerationDAOIF;

  private static MdBusinessDAOIF    stateEnumMdBusinessDAOIF;

  private static final TypeInfo     stateClass = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "US_State");

  private static final TypeInfo     stateEnum  = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "US_State_Enum");

  /**
   * Provides setup operations required for all of the tests in this class.
   * Specifically, <code>classSetUp()</code> defines a new Enumerated Type
   * (STATE) and adds it as an attribute on the MasterTestSetup.TEST_CLASS type.
   */
  @Request
  @BeforeClass
  public static void classSetUp()
  {
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

    stateEnumMdBusinessDAOIF = stateEnumMdBusiness;

    // Instantiate an md_enumeration to define State
    MdEnumerationDAO mdEnumeration = MdEnumerationDAO.newInstance();
    mdEnumeration.setValue(MdEnumerationInfo.NAME, stateEnum.getTypeName());
    mdEnumeration.setValue(MdEnumerationInfo.PACKAGE, stateEnum.getPackageName());
    mdEnumeration.setStructValue(MdEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "All States in the United States");
    mdEnumeration.setStructValue(MdEnumerationInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test");
    mdEnumeration.setValue(MdEnumerationInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdEnumeration.setValue(MdEnumerationInfo.INCLUDE_ALL, MdAttributeBooleanInfo.TRUE);
    mdEnumeration.setValue(MdEnumerationInfo.MASTER_MD_BUSINESS, stateEnumMdBusinessDAOIF.getOid());
    mdEnumeration.setValue(MdEnumerationInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdEnumeration.apply();

    mdEnumerationDAOIF = mdEnumeration;

    QueryFactory qf = new QueryFactory();
    BusinessDAOQuery bq = qf.businessDAOQuery(EntityMasterTestSetup.TEST_CLASS.getType());

    OIterator<BusinessDAOIF> i = bq.getIterator();
    for (BusinessDAOIF businessDAOIF : i)
    {
      businessDAOIF.getBusinessDAO().delete();
    }

  }

  /**
   * Deletes the previously defined STATE Enumeration after all tests have
   * completed.
   */
  @Request
  @AfterClass
  public static void classTearDown()
  {
    TestFixtureFactory.delete(mdEnumerationDAOIF);
    TestFixtureFactory.delete(stateEnumMdBusinessDAOIF);
  }

  /**
   * Set the index or a reference attribute to NON_UNIQUE when no index is
   * defined.
   * 
   */
  @Request
  @Test
  public void testDefaultReferenceFieldForNoIndex()
  {
    MdBusinessDAOIF testMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(EntityMasterTestSetup.TEST_CLASS.getType());

    MdBusinessDAOIF referenceMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(EntityMasterTestSetup.REFERENCE_CLASS.getType());

    MdAttributeReferenceDAO mdAttributeReferenceDAO = TestFixtureFactory.addReferenceAttribute(testMdBusinessIF, referenceMdBusinessIF);

    try
    {
      mdAttributeReferenceDAO.apply();

      AttributeEnumeration index = (AttributeEnumeration) mdAttributeReferenceDAO.getAttributeIF(MdAttributeConcreteInfo.INDEX_TYPE);
      Iterator<String> i = index.getEnumItemIdList().iterator();
      String indexTypeId = i.next();

      Assert.assertEquals("Referenced attribute did receive the correct non-unique default index", IndexTypes.NON_UNIQUE_INDEX.getOid(), indexTypeId);

      // check for correctness
      if (!Database.nonUniqueAttributeExists(testMdBusinessIF.getTableName(), mdAttributeReferenceDAO.getColumnName(), mdAttributeReferenceDAO.getIndexName()))
      {
        Assert.fail("An attribute with an index of type non unique was not correctly created.");
      }
    }
    finally
    {
      if (!mdAttributeReferenceDAO.isNew())
      {
        mdAttributeReferenceDAO.delete();
      }
    }
  }

  /**
   * Do not set the index or a reference attribute if a UNIQUE index is defined.
   * 
   */
  @Request
  @Test
  public void testDefaultReferenceFieldForUniqueIndex()
  {
    MdBusinessDAOIF testMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(EntityMasterTestSetup.TEST_CLASS.getType());

    MdBusinessDAOIF referenceMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(EntityMasterTestSetup.REFERENCE_CLASS.getType());

    MdAttributeReferenceDAO mdAttributeReferenceDAO = TestFixtureFactory.addReferenceAttribute(testMdBusinessIF, referenceMdBusinessIF);

    mdAttributeReferenceDAO.setValue(MdAttributeConcreteInfo.INDEX_TYPE, IndexTypes.UNIQUE_INDEX.getOid());

    try
    {
      mdAttributeReferenceDAO.apply();

      AttributeEnumeration index = (AttributeEnumeration) mdAttributeReferenceDAO.getAttributeIF(MdAttributeConcreteInfo.INDEX_TYPE);
      Iterator<String> i = index.getEnumItemIdList().iterator();
      String indexTypeId = i.next();

      Assert.assertEquals("Referenced attribute had a UNIQUE index set but something other than that resulted", IndexTypes.UNIQUE_INDEX.getOid(), indexTypeId);

      // check for correctness
      if (!Database.uniqueAttributeExists(testMdBusinessIF.getTableName(), mdAttributeReferenceDAO.getColumnName(), mdAttributeReferenceDAO.getIndexName()))
      {
        Assert.fail("An attribute with an index of type non unique was not correctly created.");
      }
    }
    finally
    {
      if (!mdAttributeReferenceDAO.isNew())
      {
        mdAttributeReferenceDAO.delete();
      }
    }
  }

  /**
   * Tests to make sure an attribute of the same name can be deleted and then
   * added as an enumeration within a single transaction.
   */
  @Request
  @Test
  public void testAttribute_Add_Delete_Add_Enum_in_transaction()
  {
    MdBusinessDAOIF testMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(EntityMasterTestSetup.TEST_CLASS.getType());

    BusinessDAO enumBusinessDAO = BusinessDAO.newInstance(stateClass.getPackageName() + "." + stateClass.getTypeName());
    enumBusinessDAO.setValue(EnumerationMasterInfo.NAME, "CO");
    enumBusinessDAO.apply();

    MdAttributeEnumerationDAO mdAttributeEnumerationDAO = null;

    String object1Id = null;

    List<String> hashedColumnNames = new LinkedList<String>();

    try
    {

      mdAttributeEnumerationDAO = this.attribute_Add_Delete_Add_Enum_in_transaction(hashedColumnNames, enumBusinessDAO.getOid());

      QueryFactory qf = new QueryFactory();
      BusinessDAOQuery bq = qf.businessDAOQuery(EntityMasterTestSetup.TEST_CLASS.getType());

      OIterator<BusinessDAOIF> i = bq.getIterator();

      try
      {
        for (BusinessDAOIF businessDAOIF : i)
        {
          object1Id = businessDAOIF.getOid();

          AttributeEnumerationIF attributeEnum = (AttributeEnumerationIF) businessDAOIF.getAttributeIF("myAttribute");

          if (!attributeEnum.getCachedEnumItemIdSet().contains(enumBusinessDAO.getOid()))
          {
            Assert.fail("Hashed temp column for cached enum ids did not transfer over its values to the final column at the end of the transaction.");
          }
        }
      }
      finally
      {
        i.close();
      }

      if (object1Id == null)
      {
        Assert.fail("Attribute values from an attribute that was added, dropped, and then added of the same name but different datatype were missing.");
      }
    }
    finally
    {
      enumBusinessDAO.delete();

      if (mdAttributeEnumerationDAO != null)
      {
        mdAttributeEnumerationDAO.delete();
        if (Database.columnExists(mdAttributeEnumerationDAO.getDefinedColumnName(), testMdBusinessIF.getTableName()))
        {
          Assert.fail("An attribute metadata object was deleted but the column [" + mdAttributeEnumerationDAO.getDefinedColumnName() + "] " + "was not removed from the table [" + testMdBusinessIF.getTableName() + "].");
        }
      }
    }
  }

  @Transaction
  public MdAttributeEnumerationDAO attribute_Add_Delete_Add_Enum_in_transaction(List<String> hashedColumnNames, String enumId)
  {
    MdBusinessDAOIF testMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(EntityMasterTestSetup.TEST_CLASS.getType());

    MdAttributeIntegerDAO mdAttributeInteger = null;
    MdAttributeDateDAO mdAttributeDate = null;
    MdAttributeEnumerationDAO mdAttributeEnumeration1 = null;
    MdAttributeEnumerationDAO mdAttributeEnumeration2 = null;
    MdAttributeBooleanDAO mdAttributeBoolean = null;

    String hashedColumn = "";

    // First, create an attribute on the type.
    mdAttributeInteger = MdAttributeIntegerDAO.newInstance();
    mdAttributeInteger.setValue(MdAttributeIntegerInfo.NAME, "myAttribute");
    mdAttributeInteger.setStructValue(MdAttributeIntegerInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "My Attribute");
    mdAttributeInteger.setValue(MdAttributeIntegerInfo.DEFINING_MD_CLASS, testMdBusinessIF.getOid());
    mdAttributeInteger.apply();

    // Now delete it
    mdAttributeInteger.delete();

    // Add an attribute of the same name but of a different datatype
    mdAttributeDate = MdAttributeDateDAO.newInstance();
    mdAttributeDate.setValue(MdAttributeDateInfo.NAME, "myAttribute");
    mdAttributeDate.setStructValue(MdAttributeDateInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "My Attribute");
    mdAttributeDate.setValue(MdAttributeDateInfo.DEFINING_MD_CLASS, testMdBusinessIF.getOid());
    mdAttributeDate.apply();

    // A hashed column should have been created.
    if (mdAttributeDate.getDefinedColumnName().equals(mdAttributeDate.getColumnName()))
    {
      Assert.fail("Attribute metadata does not define a hashed temporary column when it should.  Hashed column[" + mdAttributeDate.getColumnName() + "] vs defined column [" + mdAttributeDate.getDefinedColumnName() + "]");
    }

    hashedColumn = mdAttributeDate.getColumnName();
    hashedColumnNames.add(hashedColumn);
    if (!Database.columnExists(hashedColumn, testMdBusinessIF.getTableName()))
    {
      Assert.fail("Hashed column was not defined in the database [" + hashedColumn + "]");
    }

    // Now delete it
    mdAttributeDate.delete();

    mdAttributeEnumeration1 = TestFixtureFactory.addEnumerationAttribute(testMdBusinessIF, mdEnumerationDAOIF);
    mdAttributeEnumeration1.setValue(MdAttributeDateInfo.NAME, "myAttribute");
    mdAttributeEnumeration1.apply();

    // A hashed column should have been created.
    if (mdAttributeEnumeration1.getDefinedColumnName().equals(mdAttributeEnumeration1.getColumnName()))
    {
      Assert.fail("Attribute metadata does not define a hashed temporary column when it should.  Hashed column[" + mdAttributeEnumeration1.getColumnName() + "] vs defined column [" + mdAttributeEnumeration1.getDefinedColumnName() + "]");
    }
    // at this point, the cache column will not be hashed, as one did not exist
    // previously
    if (!mdAttributeEnumeration1.getDefinedCacheColumnName().equals(mdAttributeEnumeration1.getCacheColumnName()))
    {
      Assert.fail("Attribute metadataenumeration has a hashed temporary cache column when it should not, as no previous column existed.  Hashed column[" + mdAttributeEnumeration1.getCacheColumnName() + "] vs defined column [" + mdAttributeEnumeration1.getDefinedCacheColumnName() + "]");
    }

    hashedColumn = mdAttributeEnumeration1.getColumnName();
    hashedColumnNames.add(hashedColumn);
    if (!Database.columnExists(hashedColumn, testMdBusinessIF.getTableName()))
    {
      Assert.fail("Hashed column was not defined in the database [" + hashedColumn + "]");
    }
    // Also perform the check for the cache column
    hashedColumn = mdAttributeEnumeration1.getCacheColumnName();
    hashedColumnNames.add(hashedColumn);
    if (!Database.columnExists(hashedColumn, testMdBusinessIF.getTableName()))
    {
      Assert.fail("Hashed column was not defined in the database [" + hashedColumn + "]");
    }

    mdAttributeEnumeration1.delete();

    // Add an attribute of the same name but of a different datatype
    mdAttributeBoolean = MdAttributeBooleanDAO.newInstance();
    mdAttributeBoolean.setValue(MdAttributeBooleanInfo.NAME, "myAttribute");
    mdAttributeBoolean.setStructValue(MdAttributeBooleanInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "My Attribute");
    mdAttributeBoolean.setValue(MdAttributeBooleanInfo.DEFINING_MD_CLASS, testMdBusinessIF.getOid());
    mdAttributeBoolean.apply();

    // A hashed column should have been created.
    if (mdAttributeBoolean.getDefinedColumnName().equals(mdAttributeBoolean.getColumnName()))
    {
      Assert.fail("Attribute metadata does not define a hashed temporary column when it should.  Hashed column[" + mdAttributeBoolean.getColumnName() + "] vs defined column [" + mdAttributeBoolean.getDefinedColumnName() + "]");
    }

    hashedColumn = mdAttributeBoolean.getColumnName();
    hashedColumnNames.add(hashedColumn);
    if (!Database.columnExists(hashedColumn, testMdBusinessIF.getTableName()))
    {
      Assert.fail("Hashed column was not defined in the database [" + hashedColumn + "]");
    }

    // Now delete it
    mdAttributeBoolean.delete();

    mdAttributeEnumeration2 = TestFixtureFactory.addEnumerationAttribute(testMdBusinessIF, mdEnumerationDAOIF);
    mdAttributeEnumeration2.setValue(MdAttributeDateInfo.NAME, "myAttribute");
    mdAttributeEnumeration2.apply();

    // A hashed column should have been created.
    if (mdAttributeEnumeration2.getDefinedColumnName().equals(mdAttributeEnumeration2.getColumnName()))
    {
      Assert.fail("Attribute metadata does not define a hashed temporary column when it should.  Hashed column[" + mdAttributeEnumeration2.getColumnName() + "] vs defined column [" + mdAttributeEnumeration2.getDefinedColumnName() + "]");
    }
    if (mdAttributeEnumeration2.getDefinedCacheColumnName().equals(mdAttributeEnumeration2.getCacheColumnName()))
    {
      Assert.fail("Attribute metadataenumeration has a hashed temporary cache column when it should not, as no previous column existed.  Hashed column[" + mdAttributeEnumeration2.getCacheColumnName() + "] vs defined column [" + mdAttributeEnumeration2.getDefinedCacheColumnName() + "]");
    }

    hashedColumn = mdAttributeEnumeration2.getColumnName();
    hashedColumnNames.add(hashedColumn);
    if (!Database.columnExists(hashedColumn, testMdBusinessIF.getTableName()))
    {
      Assert.fail("Hashed column was not defined in the database [" + hashedColumn + "]");
    }
    // Also perform the check for the cache column
    hashedColumn = mdAttributeEnumeration2.getCacheColumnName();
    hashedColumnNames.add(hashedColumn);
    if (!Database.columnExists(hashedColumn, testMdBusinessIF.getTableName()))
    {
      Assert.fail("Hashed column was not defined in the database [" + hashedColumn + "]");
    }

    BusinessDAO object1 = BusinessDAO.newInstance(EntityMasterTestSetup.TEST_CLASS.getType());
    object1.setValue("myAttribute", enumId);
    object1.apply();

    return mdAttributeEnumeration2;
  }

  /**
   * Tests to make sure an attribute of the same name can be deleted, then
   * added, and then deleted within a single transaction.
   */
  @Request
  @Test
  public void testAttribute_Add_Delete_Add_Delete_in_transaction()
  {
    MdBusinessDAOIF testMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(EntityMasterTestSetup.TEST_CLASS.getType());

    List<String> hashedColumnNames = new LinkedList<String>();

    this.attribute_Add_Delete_Add_Delete_in_transaction(hashedColumnNames);

    if (Database.columnExists("my_attribute", testMdBusinessIF.getTableName()))
    {
      Assert.fail("Column was not properly removed at the end of a transaction [my_attribute].");
    }

    Assert.assertEquals(8, hashedColumnNames.size());

    for (String hashedColumnName : hashedColumnNames)
    {
      if (Database.columnExists(hashedColumnName, testMdBusinessIF.getTableName()))
      {
        Assert.fail("Hashed column was not properly removed at the end of a transaction [" + hashedColumnName + "].");
      }
    }
  }

  @Transaction
  public void attribute_Add_Delete_Add_Delete_in_transaction(List<String> hashedColumnNames)
  {
    MdBusinessDAOIF testMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(EntityMasterTestSetup.TEST_CLASS.getType());

    MdAttributeIntegerDAO mdAttributeInteger = null;
    MdAttributeDateDAO mdAttributeDate = null;
    MdAttributeEnumerationDAO mdAttributeEnumeration1 = null;
    MdAttributeEnumerationDAO mdAttributeEnumeration2 = null;
    MdAttributeBooleanDAO mdAttributeBoolean = null;
    MdAttributeCharacterDAO mdAttributeCharacter = null;
    MdAttributeTextDAO mdAttributeText = null;

    String hashedColumn = "";

    // First, create an attribute on the type.
    mdAttributeInteger = MdAttributeIntegerDAO.newInstance();
    mdAttributeInteger.setValue(MdAttributeIntegerInfo.NAME, "myAttribute");
    mdAttributeInteger.setStructValue(MdAttributeIntegerInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "My Attribute");
    mdAttributeInteger.setValue(MdAttributeIntegerInfo.DEFINING_MD_CLASS, testMdBusinessIF.getOid());
    mdAttributeInteger.apply();

    // Now delete it
    mdAttributeInteger.delete();

    // Add an attribute of the same name but of a different datatype
    mdAttributeDate = MdAttributeDateDAO.newInstance();
    mdAttributeDate.setValue(MdAttributeDateInfo.NAME, "myAttribute");
    mdAttributeDate.setStructValue(MdAttributeDateInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "My Attribute");
    mdAttributeDate.setValue(MdAttributeDateInfo.DEFINING_MD_CLASS, testMdBusinessIF.getOid());
    mdAttributeDate.apply();

    // A hashed column should have been created.
    if (mdAttributeDate.getDefinedColumnName().equals(mdAttributeDate.getColumnName()))
    {
      Assert.fail("Attribute metadata does not define a hashed temporary column when it should.  Hashed column[" + mdAttributeDate.getColumnName() + "] vs defined column [" + mdAttributeDate.getDefinedColumnName() + "]");
    }

    hashedColumn = mdAttributeDate.getColumnName();
    hashedColumnNames.add(hashedColumn);
    if (!Database.columnExists(hashedColumn, testMdBusinessIF.getTableName()))
    {
      Assert.fail("Hashed column was not defined in the database [" + hashedColumn + "]");
    }

    // Now delete it
    mdAttributeDate.delete();

    mdAttributeEnumeration1 = TestFixtureFactory.addEnumerationAttribute(testMdBusinessIF, mdEnumerationDAOIF);
    mdAttributeEnumeration1.setValue(MdAttributeDateInfo.NAME, "myAttribute");
    mdAttributeEnumeration1.apply();

    // A hashed column should have been created.
    if (mdAttributeEnumeration1.getDefinedColumnName().equals(mdAttributeEnumeration1.getColumnName()))
    {
      Assert.fail("Attribute metadata does not define a hashed temporary column when it should.  Hashed column[" + mdAttributeEnumeration1.getColumnName() + "] vs defined column [" + mdAttributeEnumeration1.getDefinedColumnName() + "]");
    }
    // at this point, the cache column will not be hashed, as one did not exist
    // previously
    if (!mdAttributeEnumeration1.getDefinedCacheColumnName().equals(mdAttributeEnumeration1.getCacheColumnName()))
    {
      Assert.fail("Attribute metadataenumeration has a hashed temporary cache column when it should not, as no previous column existed.  Hashed column[" + mdAttributeEnumeration1.getCacheColumnName() + "] vs defined column [" + mdAttributeEnumeration1.getDefinedCacheColumnName() + "]");
    }

    hashedColumn = mdAttributeEnumeration1.getColumnName();
    hashedColumnNames.add(hashedColumn);
    if (!Database.columnExists(hashedColumn, testMdBusinessIF.getTableName()))
    {
      Assert.fail("Hashed column was not defined in the database [" + hashedColumn + "]");
    }
    // Also perform the check for the cache column
    hashedColumn = mdAttributeEnumeration1.getCacheColumnName();
    hashedColumnNames.add(hashedColumn);
    if (!Database.columnExists(hashedColumn, testMdBusinessIF.getTableName()))
    {
      Assert.fail("Hashed column was not defined in the database [" + hashedColumn + "]");
    }

    mdAttributeEnumeration1.delete();

    // Add an attribute of the same name but of a different datatype
    mdAttributeBoolean = MdAttributeBooleanDAO.newInstance();
    mdAttributeBoolean.setValue(MdAttributeBooleanInfo.NAME, "myAttribute");
    mdAttributeBoolean.setStructValue(MdAttributeBooleanInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "My Attribute");
    mdAttributeBoolean.setValue(MdAttributeBooleanInfo.DEFINING_MD_CLASS, testMdBusinessIF.getOid());
    mdAttributeBoolean.apply();

    // A hashed column should have been created.
    if (mdAttributeBoolean.getDefinedColumnName().equals(mdAttributeBoolean.getColumnName()))
    {
      Assert.fail("Attribute metadata does not define a hashed temporary column when it should.  Hashed column[" + mdAttributeBoolean.getColumnName() + "] vs defined column [" + mdAttributeBoolean.getDefinedColumnName() + "]");
    }

    hashedColumn = mdAttributeBoolean.getColumnName();
    hashedColumnNames.add(hashedColumn);
    if (!Database.columnExists(hashedColumn, testMdBusinessIF.getTableName()))
    {
      Assert.fail("Hashed column was not defined in the database [" + hashedColumn + "]");
    }

    // Now delete it
    mdAttributeBoolean.delete();

    mdAttributeEnumeration2 = TestFixtureFactory.addEnumerationAttribute(testMdBusinessIF, mdEnumerationDAOIF);
    mdAttributeEnumeration2.setValue(MdAttributeDateInfo.NAME, "myAttribute");
    mdAttributeEnumeration2.apply();

    // A hashed column should have been created.
    if (mdAttributeEnumeration2.getDefinedColumnName().equals(mdAttributeEnumeration2.getColumnName()))
    {
      Assert.fail("Attribute metadata does not define a hashed temporary column when it should.  Hashed column[" + mdAttributeEnumeration2.getColumnName() + "] vs defined column [" + mdAttributeEnumeration2.getDefinedColumnName() + "]");
    }
    if (mdAttributeEnumeration2.getDefinedCacheColumnName().equals(mdAttributeEnumeration2.getCacheColumnName()))
    {
      Assert.fail("Attribute metadataenumeration has a hashed temporary cache column when it should not, as no previous column existed.  Hashed column[" + mdAttributeEnumeration2.getCacheColumnName() + "] vs defined column [" + mdAttributeEnumeration2.getDefinedCacheColumnName() + "]");
    }

    hashedColumn = mdAttributeEnumeration2.getColumnName();
    hashedColumnNames.add(hashedColumn);
    if (!Database.columnExists(hashedColumn, testMdBusinessIF.getTableName()))
    {
      Assert.fail("Hashed column was not defined in the database [" + hashedColumn + "]");
    }
    // Also perform the check for the cache column
    hashedColumn = mdAttributeEnumeration2.getCacheColumnName();
    hashedColumnNames.add(hashedColumn);
    if (!Database.columnExists(hashedColumn, testMdBusinessIF.getTableName()))
    {
      Assert.fail("Hashed column was not defined in the database [" + hashedColumn + "]");
    }

    mdAttributeEnumeration2.delete();

    // Add an attribute of the same name but of a different datatype
    mdAttributeCharacter = MdAttributeCharacterDAO.newInstance();
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.NAME, "myAttribute");
    mdAttributeCharacter.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "My Attribute");
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.SIZE, "32");
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, testMdBusinessIF.getOid());
    mdAttributeCharacter.apply();

    // A hashed column should have been created.
    if (mdAttributeCharacter.getDefinedColumnName().equals(mdAttributeCharacter.getColumnName()))
    {
      Assert.fail("Attribute metadata does not define a hashed temporary column when it should.  Hashed column[" + mdAttributeCharacter.getColumnName() + "] vs defined column [" + mdAttributeCharacter.getDefinedColumnName() + "]");
    }

    hashedColumn = mdAttributeCharacter.getColumnName();
    hashedColumnNames.add(hashedColumn);
    if (!Database.columnExists(hashedColumn, testMdBusinessIF.getTableName()))
    {
      Assert.fail("Hashed column was not defined in the database [" + hashedColumn + "]");
    }

    // Now delete it
    mdAttributeCharacter.delete();

    // Add an attribute of the same name but of a different datatype
    mdAttributeText = MdAttributeTextDAO.newInstance();
    mdAttributeText.setValue(MdAttributeTextInfo.NAME, "myAttribute");
    mdAttributeText.setStructValue(MdAttributeTextInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "My Attribute");
    mdAttributeText.setValue(MdAttributeTextInfo.DEFINING_MD_CLASS, testMdBusinessIF.getOid());
    mdAttributeText.apply();

    // A hashed column should have been created.
    if (mdAttributeText.getDefinedColumnName().equals(mdAttributeText.getColumnName()))
    {
      Assert.fail("Attribute metadata does not define a hashed temporary column when it should.  Hashed column[" + mdAttributeText.getColumnName() + "] vs defined column [" + mdAttributeText.getDefinedColumnName() + "]");
    }

    hashedColumn = mdAttributeText.getColumnName();
    hashedColumnNames.add(hashedColumn);
    if (!Database.columnExists(hashedColumn, testMdBusinessIF.getTableName()))
    {
      Assert.fail("Hashed column was not defined in the database [" + hashedColumn + "]");
    }

    mdAttributeText.delete();
  }

  /**
   * Tests to make sure an attribute of the same name can be deleted, and then
   * added within a single transaction.
   */
  @Request
  @Test
  public void testAttribute_Add_Delete_Add_in_transaction()
  {
    MdAttributeConcreteDAO mdAttribute = null;

    String object1Id = null;
    String object2Id = null;

    MdBusinessDAOIF testMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(EntityMasterTestSetup.TEST_CLASS.getType());

    List<String> hashedColumnNames = new LinkedList<String>();

    try
    {
      mdAttribute = this.attribute_Add_Delete_Add_in_transaction(hashedColumnNames);

      if (!mdAttribute.getColumnName().equals(mdAttribute.getDefinedColumnName()))
      {
        Assert.fail("[" + MdAttributeConcreteInfo.COLUMN_NAME + "] value on an attribute metadata object contains a temporary hashed value after a transaction has completed.");
      }

      Assert.assertEquals(8, hashedColumnNames.size());

      for (String hashedColumnName : hashedColumnNames)
      {
        if (Database.columnExists(hashedColumnName, testMdBusinessIF.getTableName()))
        {
          Assert.fail("Hashed column was not properly removed at the end of a transaction [" + hashedColumnName + "].");
        }
      }

      QueryFactory qf = new QueryFactory();
      BusinessDAOQuery bq = qf.businessDAOQuery(EntityMasterTestSetup.TEST_CLASS.getType());

      OIterator<BusinessDAOIF> i = bq.getIterator();

      try
      {
        for (BusinessDAOIF businessDAOIF : i)
        {
          if (businessDAOIF.getValue("myAttribute").equals("Value 1"))
          {
            object1Id = businessDAOIF.getOid();
          }
          else if (businessDAOIF.getValue("myAttribute").equals("Value 2"))
          {
            object2Id = businessDAOIF.getOid();
          }
        }
      }
      finally
      {
        i.close();
      }

      if (object1Id == null || object2Id == null)
      {
        Assert.fail("Attribute values from an attribute that was added, dropped, and then added of the same name but different datatype were missing.");
      }
    }
    finally
    {
      if (object1Id != null)
      {
        BusinessDAO businessDAO = BusinessDAO.get(object1Id).getBusinessDAO();
        businessDAO.delete();
      }

      if (object2Id != null)
      {
        BusinessDAO businessDAO = BusinessDAO.get(object2Id).getBusinessDAO();
        businessDAO.delete();
      }

      if (mdAttribute != null)
      {
        mdAttribute.delete();
        if (Database.columnExists(mdAttribute.getDefinedColumnName(), testMdBusinessIF.getTableName()))
        {
          Assert.fail("An attribute metadata object was deleted but the column [" + mdAttribute.getDefinedColumnName() + "] " + "was not removed from the table [" + testMdBusinessIF.getTableName() + "].");
        }
      }
    }
  }

  @Transaction
  public MdAttributeConcreteDAO attribute_Add_Delete_Add_in_transaction(List<String> hashedColumnNames)
  {
    MdBusinessDAOIF testMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(EntityMasterTestSetup.TEST_CLASS.getType());

    MdAttributeIntegerDAO mdAttributeInteger = null;
    MdAttributeDateDAO mdAttributeDate = null;
    MdAttributeEnumerationDAO mdAttributeEnumeration1 = null;
    MdAttributeEnumerationDAO mdAttributeEnumeration2 = null;
    MdAttributeBooleanDAO mdAttributeBoolean = null;
    MdAttributeCharacterDAO mdAttributeCharacter = null;
    MdAttributeTextDAO mdAttributeText = null;

    String hashedColumn = "";

    // First, create an attribute on the type.
    mdAttributeInteger = MdAttributeIntegerDAO.newInstance();
    mdAttributeInteger.setValue(MdAttributeIntegerInfo.NAME, "myAttribute");
    mdAttributeInteger.setStructValue(MdAttributeIntegerInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "My Attribute");
    mdAttributeInteger.setValue(MdAttributeIntegerInfo.DEFINING_MD_CLASS, testMdBusinessIF.getOid());
    mdAttributeInteger.apply();

    // Now delete it
    mdAttributeInteger.delete();

    // Add an attribute of the same name but of a different datatype
    mdAttributeDate = MdAttributeDateDAO.newInstance();
    mdAttributeDate.setValue(MdAttributeDateInfo.NAME, "myAttribute");
    mdAttributeDate.setStructValue(MdAttributeDateInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "My Attribute");
    mdAttributeDate.setValue(MdAttributeDateInfo.DEFINING_MD_CLASS, testMdBusinessIF.getOid());
    mdAttributeDate.apply();

    // A hashed column should have been created.
    if (mdAttributeDate.getDefinedColumnName().equals(mdAttributeDate.getColumnName()))
    {
      Assert.fail("Attribute metadata does not define a hashed temporary column when it should.  Hashed column[" + mdAttributeDate.getColumnName() + "] vs defined column [" + mdAttributeDate.getDefinedColumnName() + "]");
    }

    hashedColumn = mdAttributeDate.getColumnName();
    hashedColumnNames.add(hashedColumn);
    if (!Database.columnExists(hashedColumn, testMdBusinessIF.getTableName()))
    {
      Assert.fail("Hashed column was not defined in the database [" + hashedColumn + "]");
    }

    // Now delete it
    mdAttributeDate.delete();

    mdAttributeEnumeration1 = TestFixtureFactory.addEnumerationAttribute(testMdBusinessIF, mdEnumerationDAOIF);
    mdAttributeEnumeration1.setValue(MdAttributeDateInfo.NAME, "myAttribute");
    mdAttributeEnumeration1.apply();

    // A hashed column should have been created.
    if (mdAttributeEnumeration1.getDefinedColumnName().equals(mdAttributeEnumeration1.getColumnName()))
    {
      Assert.fail("Attribute metadata does not define a hashed temporary column when it should.  Hashed column[" + mdAttributeEnumeration1.getColumnName() + "] vs defined column [" + mdAttributeEnumeration1.getDefinedColumnName() + "]");
    }
    // at this point, the cache column will not be hashed, as one did not exist
    // previously
    if (!mdAttributeEnumeration1.getDefinedCacheColumnName().equals(mdAttributeEnumeration1.getCacheColumnName()))
    {
      Assert.fail("Attribute metadataenumeration has a hashed temporary cache column when it should not, as no previous column existed.  Hashed column[" + mdAttributeEnumeration1.getCacheColumnName() + "] vs defined column [" + mdAttributeEnumeration1.getDefinedCacheColumnName() + "]");
    }

    hashedColumn = mdAttributeEnumeration1.getColumnName();
    hashedColumnNames.add(hashedColumn);
    if (!Database.columnExists(hashedColumn, testMdBusinessIF.getTableName()))
    {
      Assert.fail("Hashed column was not defined in the database [" + hashedColumn + "]");
    }
    // Also perform the check for the cache column
    hashedColumn = mdAttributeEnumeration1.getCacheColumnName();
    hashedColumnNames.add(hashedColumn);
    if (!Database.columnExists(hashedColumn, testMdBusinessIF.getTableName()))
    {
      Assert.fail("Hashed column was not defined in the database [" + hashedColumn + "]");
    }

    mdAttributeEnumeration1.delete();

    // Add an attribute of the same name but of a different datatype
    mdAttributeBoolean = MdAttributeBooleanDAO.newInstance();
    mdAttributeBoolean.setValue(MdAttributeBooleanInfo.NAME, "myAttribute");
    mdAttributeBoolean.setStructValue(MdAttributeBooleanInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "My Attribute");
    mdAttributeBoolean.setValue(MdAttributeBooleanInfo.DEFINING_MD_CLASS, testMdBusinessIF.getOid());
    mdAttributeBoolean.apply();

    // A hashed column should have been created.
    if (mdAttributeBoolean.getDefinedColumnName().equals(mdAttributeBoolean.getColumnName()))
    {
      Assert.fail("Attribute metadata does not define a hashed temporary column when it should.  Hashed column[" + mdAttributeBoolean.getColumnName() + "] vs defined column [" + mdAttributeBoolean.getDefinedColumnName() + "]");
    }

    hashedColumn = mdAttributeBoolean.getColumnName();
    hashedColumnNames.add(hashedColumn);
    if (!Database.columnExists(hashedColumn, testMdBusinessIF.getTableName()))
    {
      Assert.fail("Hashed column was not defined in the database [" + hashedColumn + "]");
    }

    // Now delete it
    mdAttributeBoolean.delete();

    mdAttributeEnumeration2 = TestFixtureFactory.addEnumerationAttribute(testMdBusinessIF, mdEnumerationDAOIF);
    mdAttributeEnumeration2.setValue(MdAttributeDateInfo.NAME, "myAttribute");
    mdAttributeEnumeration2.apply();

    // A hashed column should have been created.
    if (mdAttributeEnumeration2.getDefinedColumnName().equals(mdAttributeEnumeration2.getColumnName()))
    {
      Assert.fail("Attribute metadata does not define a hashed temporary column when it should.  Hashed column[" + mdAttributeEnumeration2.getColumnName() + "] vs defined column [" + mdAttributeEnumeration2.getDefinedColumnName() + "]");
    }
    if (mdAttributeEnumeration2.getDefinedCacheColumnName().equals(mdAttributeEnumeration2.getCacheColumnName()))
    {
      Assert.fail("Attribute metadataenumeration has a hashed temporary cache column when it should not, as no previous column existed.  Hashed column[" + mdAttributeEnumeration2.getCacheColumnName() + "] vs defined column [" + mdAttributeEnumeration2.getDefinedCacheColumnName() + "]");
    }

    hashedColumn = mdAttributeEnumeration2.getColumnName();
    hashedColumnNames.add(hashedColumn);
    if (!Database.columnExists(hashedColumn, testMdBusinessIF.getTableName()))
    {
      Assert.fail("Hashed column was not defined in the database [" + hashedColumn + "]");
    }
    // Also perform the check for the cache column
    hashedColumn = mdAttributeEnumeration2.getCacheColumnName();
    hashedColumnNames.add(hashedColumn);
    if (!Database.columnExists(hashedColumn, testMdBusinessIF.getTableName()))
    {
      Assert.fail("Hashed column was not defined in the database [" + hashedColumn + "]");
    }

    mdAttributeEnumeration2.delete();

    // Add an attribute of the same name but of a different datatype
    mdAttributeCharacter = MdAttributeCharacterDAO.newInstance();
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.NAME, "myAttribute");
    mdAttributeCharacter.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "My Attribute");
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.SIZE, "32");
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, testMdBusinessIF.getOid());
    mdAttributeCharacter.apply();

    // A hashed column should have been created.
    if (mdAttributeCharacter.getDefinedColumnName().equals(mdAttributeCharacter.getColumnName()))
    {
      Assert.fail("Attribute metadata does not define a hashed temporary column when it should.  Hashed column[" + mdAttributeCharacter.getColumnName() + "] vs defined column [" + mdAttributeCharacter.getDefinedColumnName() + "]");
    }

    hashedColumn = mdAttributeCharacter.getColumnName();
    hashedColumnNames.add(hashedColumn);
    if (!Database.columnExists(hashedColumn, testMdBusinessIF.getTableName()))
    {
      Assert.fail("Hashed column was not defined in the database [" + hashedColumn + "]");
    }

    // Now delete it
    mdAttributeCharacter.delete();

    // Add an attribute of the same name but of a different datatype
    mdAttributeText = MdAttributeTextDAO.newInstance();
    mdAttributeText.setValue(MdAttributeTextInfo.NAME, "myAttribute");
    mdAttributeText.setStructValue(MdAttributeTextInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "My Attribute");
    mdAttributeText.setValue(MdAttributeTextInfo.DEFINING_MD_CLASS, testMdBusinessIF.getOid());
    mdAttributeText.apply();

    // A hashed column should have been created.
    if (mdAttributeText.getDefinedColumnName().equals(mdAttributeText.getColumnName()))
    {
      Assert.fail("Attribute metadata does not define a hashed temporary column when it should.  Hashed column[" + mdAttributeText.getColumnName() + "] vs defined column [" + mdAttributeText.getDefinedColumnName() + "]");
    }

    hashedColumn = mdAttributeText.getColumnName();
    hashedColumnNames.add(hashedColumn);
    if (!Database.columnExists(hashedColumn, testMdBusinessIF.getTableName()))
    {
      Assert.fail("Hashed column was not defined in the database [" + hashedColumn + "]");
    }

    BusinessDAO object1 = BusinessDAO.newInstance(EntityMasterTestSetup.TEST_CLASS.getType());
    object1.setValue("myAttribute", "Value 1");
    object1.apply();

    BusinessDAO object2 = BusinessDAO.newInstance(EntityMasterTestSetup.TEST_CLASS.getType());
    object2.setValue("myAttribute", "Value 2");
    object2.apply();

    return mdAttributeText;
  }

  /**
   * Tests to make sure an attribute of the same name can be deleted, and then
   * added within a single transaction. This test uses a user specified column
   * name for the attribute.
   */
  @Request
  @Test
  public void testAttribute_Add_Delete_Add_in_transaction_customColumn()
  {
    MdAttributeConcreteDAO mdAttribute = null;

    String object1Id = null;
    String object2Id = null;

    MdBusinessDAOIF testMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(EntityMasterTestSetup.TEST_CLASS.getType());

    List<String> hashedColumnNames = new LinkedList<String>();

    try
    {
      mdAttribute = this.attribute_Add_Delete_Add_in_transaction_customColumn(hashedColumnNames);

      if (!mdAttribute.getColumnName().equals("custom_column"))
      {
        Assert.fail("[" + MdAttributeConcreteInfo.COLUMN_NAME + "] value on an attribute metadata object contains a temporary hashed value after a transaction has completed.");
      }

      Assert.assertEquals(4, hashedColumnNames.size());

      for (String hashedColumnName : hashedColumnNames)
      {
        if (Database.columnExists(hashedColumnName, testMdBusinessIF.getTableName()))
        {
          Assert.fail("Hashed column was not properly removed at the end of a transaction [" + hashedColumnName + "].");
        }
      }

      QueryFactory qf = new QueryFactory();
      BusinessDAOQuery bq = qf.businessDAOQuery(EntityMasterTestSetup.TEST_CLASS.getType());

      OIterator<BusinessDAOIF> i = bq.getIterator();

      try
      {
        for (BusinessDAOIF businessDAOIF : i)
        {
          if (businessDAOIF.getValue("myAttribute").equals("Value 1"))
          {
            object1Id = businessDAOIF.getOid();
          }
          else if (businessDAOIF.getValue("myAttribute").equals("Value 2"))
          {
            object2Id = businessDAOIF.getOid();
          }
        }
      }
      finally
      {
        i.close();
      }

      if (object1Id == null || object2Id == null)
      {
        Assert.fail("Attribute values from an attribute that was added, dropped, and then added of the same name but different datatype were missing.");
      }
    }
    finally
    {
      if (object1Id != null)
      {
        BusinessDAO businessDAO = BusinessDAO.get(object1Id).getBusinessDAO();
        businessDAO.delete();
      }

      if (object2Id != null)
      {
        BusinessDAO businessDAO = BusinessDAO.get(object2Id).getBusinessDAO();
        businessDAO.delete();
      }

      if (mdAttribute != null)
      {
        mdAttribute.delete();
        if (Database.columnExists(mdAttribute.getDefinedColumnName(), testMdBusinessIF.getTableName()))
        {
          Assert.fail("An attribute metadata object was deleted but the column [" + mdAttribute.getDefinedColumnName() + "] " + "was not removed from the table [" + testMdBusinessIF.getTableName() + "].");
        }
      }
    }
  }

  @Transaction
  public MdAttributeConcreteDAO attribute_Add_Delete_Add_in_transaction_customColumn(List<String> hashedColumnNames)
  {
    MdBusinessDAOIF testMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(EntityMasterTestSetup.TEST_CLASS.getType());

    MdAttributeIntegerDAO mdAttributeInteger = null;
    MdAttributeDateDAO mdAttributeDate = null;
    MdAttributeBooleanDAO mdAttributeBoolean = null;
    MdAttributeCharacterDAO mdAttributeCharacter = null;
    MdAttributeTextDAO mdAttributeText = null;

    String hashedColumn = "";

    // First, create an attribute on the type.
    mdAttributeInteger = MdAttributeIntegerDAO.newInstance();
    mdAttributeInteger.setValue(MdAttributeIntegerInfo.NAME, "myAttribute");
    mdAttributeInteger.setValue(MdAttributeIntegerInfo.COLUMN_NAME, "custom_column");
    mdAttributeInteger.setStructValue(MdAttributeIntegerInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "My Attribute");
    mdAttributeInteger.setValue(MdAttributeIntegerInfo.DEFINING_MD_CLASS, testMdBusinessIF.getOid());
    mdAttributeInteger.apply();

    // Now delete it
    mdAttributeInteger.delete();

    // Add an attribute of the same name but of a different datatype
    mdAttributeDate = MdAttributeDateDAO.newInstance();
    mdAttributeDate.setValue(MdAttributeDateInfo.NAME, "myAttribute");
    mdAttributeDate.setValue(MdAttributeDateInfo.COLUMN_NAME, "custom_column");
    mdAttributeDate.setStructValue(MdAttributeDateInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "My Attribute");
    mdAttributeDate.setValue(MdAttributeDateInfo.DEFINING_MD_CLASS, testMdBusinessIF.getOid());
    mdAttributeDate.apply();

    // A hashed column should have been created.
    if (mdAttributeDate.getDefinedColumnName().equals(mdAttributeDate.getColumnName()))
    {
      Assert.fail("Attribute metadata does not define a hashed temporary column when it should.  Hashed column[" + mdAttributeDate.getColumnName() + "] vs defined column [" + mdAttributeDate.getDefinedColumnName() + "]");
    }

    hashedColumn = mdAttributeDate.getColumnName();
    hashedColumnNames.add(hashedColumn);
    if (!Database.columnExists(hashedColumn, testMdBusinessIF.getTableName()))
    {
      Assert.fail("Hashed column was not defined in the database [" + hashedColumn + "]");
    }

    // Now delete it
    mdAttributeDate.delete();

    // Add an attribute of the same name but of a different datatype
    mdAttributeBoolean = MdAttributeBooleanDAO.newInstance();
    mdAttributeBoolean.setValue(MdAttributeBooleanInfo.NAME, "myAttribute");
    mdAttributeBoolean.setValue(MdAttributeBooleanInfo.COLUMN_NAME, "custom_column");
    mdAttributeBoolean.setStructValue(MdAttributeBooleanInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "My Attribute");
    mdAttributeBoolean.setValue(MdAttributeBooleanInfo.DEFINING_MD_CLASS, testMdBusinessIF.getOid());
    mdAttributeBoolean.apply();

    // A hashed column should have been created.
    if (mdAttributeBoolean.getDefinedColumnName().equals(mdAttributeBoolean.getColumnName()))
    {
      Assert.fail("Attribute metadata does not define a hashed temporary column when it should.  Hashed column[" + mdAttributeBoolean.getColumnName() + "] vs defined column [" + mdAttributeBoolean.getDefinedColumnName() + "]");
    }

    hashedColumn = mdAttributeBoolean.getColumnName();
    hashedColumnNames.add(hashedColumn);
    if (!Database.columnExists(hashedColumn, testMdBusinessIF.getTableName()))
    {
      Assert.fail("Hashed column was not defined in the database [" + hashedColumn + "]");
    }

    // Now delete it
    mdAttributeBoolean.delete();

    // Add an attribute of the same name but of a different datatype
    mdAttributeCharacter = MdAttributeCharacterDAO.newInstance();
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.NAME, "myAttribute");
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.COLUMN_NAME, "custom_column");
    mdAttributeCharacter.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "My Attribute");
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.SIZE, "32");
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, testMdBusinessIF.getOid());
    mdAttributeCharacter.apply();

    // A hashed column should have been created.
    if (mdAttributeCharacter.getDefinedColumnName().equals(mdAttributeCharacter.getColumnName()))
    {
      Assert.fail("Attribute metadata does not define a hashed temporary column when it should.  Hashed column[" + mdAttributeCharacter.getColumnName() + "] vs defined column [" + mdAttributeCharacter.getDefinedColumnName() + "]");
    }

    hashedColumn = mdAttributeCharacter.getColumnName();
    hashedColumnNames.add(hashedColumn);
    if (!Database.columnExists(hashedColumn, testMdBusinessIF.getTableName()))
    {
      Assert.fail("Hashed column was not defined in the database [" + hashedColumn + "]");
    }

    // Now delete it
    mdAttributeCharacter.delete();

    // Add an attribute of the same name but of a different datatype
    mdAttributeText = MdAttributeTextDAO.newInstance();
    mdAttributeText.setValue(MdAttributeTextInfo.NAME, "myAttribute");
    mdAttributeText.setValue(MdAttributeTextInfo.COLUMN_NAME, "custom_column");
    mdAttributeText.setStructValue(MdAttributeTextInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "My Attribute");
    mdAttributeText.setValue(MdAttributeTextInfo.DEFINING_MD_CLASS, testMdBusinessIF.getOid());
    mdAttributeText.apply();

    // A hashed column should have been created.
    if (mdAttributeText.getDefinedColumnName().equals(mdAttributeText.getColumnName()))
    {
      Assert.fail("Attribute metadata does not define a hashed temporary column when it should.  Hashed column[" + mdAttributeText.getColumnName() + "] vs defined column [" + mdAttributeText.getDefinedColumnName() + "]");
    }

    hashedColumn = mdAttributeText.getColumnName();
    hashedColumnNames.add(hashedColumn);
    if (!Database.columnExists(hashedColumn, testMdBusinessIF.getTableName()))
    {
      Assert.fail("Hashed column was not defined in the database [" + hashedColumn + "]");
    }

    BusinessDAO object1 = BusinessDAO.newInstance(EntityMasterTestSetup.TEST_CLASS.getType());
    object1.setValue("myAttribute", "Value 1");
    object1.apply();

    BusinessDAO object2 = BusinessDAO.newInstance(EntityMasterTestSetup.TEST_CLASS.getType());
    object2.setValue("myAttribute", "Value 2");
    object2.apply();

    return mdAttributeText;
  }

  /**
   * Tests to make sure that rollback executes properly during a transaction
   * with an attribute of the same name deleted, and then added within a single
   * transaction.
   */
  @Request
  @Test
  public void testAttribute_Add_Delete_Add_in_transaction_rollback()
  {
    MdBusinessDAOIF testMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(EntityMasterTestSetup.TEST_CLASS.getType());

    // Add an attribute of the same name but of a different datatype
    MdAttributeTextDAO mdAttributeText = MdAttributeTextDAO.newInstance();
    mdAttributeText.setValue(MdAttributeTextInfo.NAME, "myAttribute");
    mdAttributeText.setStructValue(MdAttributeTextInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "My Attribute");
    mdAttributeText.setValue(MdAttributeTextInfo.DEFINING_MD_CLASS, testMdBusinessIF.getOid());
    mdAttributeText.apply();

    List<String> hashedColumnNames = new LinkedList<String>();

    try
    {
      this.attribute_Add_Delete_Add_in_transaction_rollback(mdAttributeText, hashedColumnNames);
    }
    catch (AttributeValueException e)
    {
      // we want to land here
    }
    try
    {
      Assert.assertEquals(4, hashedColumnNames.size());

      for (String hashedColumnName : hashedColumnNames)
      {
        if (Database.columnExists(hashedColumnName, testMdBusinessIF.getTableName()))
        {
          Assert.fail("Hashed column was not properly removed at the end of a transaction [" + hashedColumnName + "].");
        }
      }

      // A hashed column should have been removed.
      if (!mdAttributeText.getDefinedColumnName().equals(mdAttributeText.getColumnName()))
      {
        Assert.fail("Attribute metadata still has a hashed column after a rolledback transaction.  Hashed column[" + mdAttributeText.getColumnName() + "] vs defined column [" + mdAttributeText.getDefinedColumnName() + "]");
      }

      if (!Database.columnExists(mdAttributeText.getDefinedColumnName(), testMdBusinessIF.getTableName()))
      {
        Assert.fail("Transaction rolled back but the original column on an attribute metadata object was improperly deleted [" + mdAttributeText.getDefinedColumnName() + "] " + "from the table [" + testMdBusinessIF.getTableName() + "].");
      }

    }
    finally
    {
      TestFixtureFactory.delete(mdAttributeText);
    }
  }

  @Transaction
  public void attribute_Add_Delete_Add_in_transaction_rollback(MdAttributeTextDAO mdAttributeText, List<String> hashedColumnNames)
  {
    MdBusinessDAOIF testMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(EntityMasterTestSetup.TEST_CLASS.getType());

    MdAttributeIntegerDAO mdAttributeInteger = null;
    MdAttributeDateDAO mdAttributeDate = null;
    MdAttributeBooleanDAO mdAttributeBoolean = null;
    MdAttributeCharacterDAO mdAttributeCharacter = null;

    String hashedColumn = "";

    // Delete the attribute
    mdAttributeText.delete();

    // Add an attribute of the same name but of a different datatype
    mdAttributeDate = MdAttributeDateDAO.newInstance();
    mdAttributeDate.setValue(MdAttributeDateInfo.NAME, "myAttribute");
    mdAttributeDate.setStructValue(MdAttributeDateInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "My Attribute");
    mdAttributeDate.setValue(MdAttributeDateInfo.DEFINING_MD_CLASS, testMdBusinessIF.getOid());
    mdAttributeDate.apply();

    // A hashed column should have been created.
    if (mdAttributeDate.getDefinedColumnName().equals(mdAttributeDate.getColumnName()))
    {
      Assert.fail("Attribute metadata does not define a hashed temporary column when it should.  Hashed column[" + mdAttributeDate.getColumnName() + "] vs defined column [" + mdAttributeDate.getDefinedColumnName() + "]");
    }

    hashedColumn = mdAttributeDate.getColumnName();
    hashedColumnNames.add(hashedColumn);
    if (!Database.columnExists(hashedColumn, testMdBusinessIF.getTableName()))
    {
      Assert.fail("Hashed column was not defined in the database [" + hashedColumn + "]");
    }

    // Now delete it
    mdAttributeDate.delete();

    // Add an attribute of the same name but of a different datatype
    mdAttributeBoolean = MdAttributeBooleanDAO.newInstance();
    mdAttributeBoolean.setValue(MdAttributeBooleanInfo.NAME, "myAttribute");
    mdAttributeBoolean.setStructValue(MdAttributeBooleanInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "My Attribute");
    mdAttributeBoolean.setValue(MdAttributeBooleanInfo.DEFINING_MD_CLASS, testMdBusinessIF.getOid());
    mdAttributeBoolean.apply();

    // A hashed column should have been created.
    if (mdAttributeBoolean.getDefinedColumnName().equals(mdAttributeBoolean.getColumnName()))
    {
      Assert.fail("Attribute metadata does not define a hashed temporary column when it should.  Hashed column[" + mdAttributeBoolean.getColumnName() + "] vs defined column [" + mdAttributeBoolean.getDefinedColumnName() + "]");
    }

    hashedColumn = mdAttributeBoolean.getColumnName();
    hashedColumnNames.add(hashedColumn);
    if (!Database.columnExists(hashedColumn, testMdBusinessIF.getTableName()))
    {
      Assert.fail("Hashed column was not defined in the database [" + hashedColumn + "]");
    }

    // Now delete it
    mdAttributeBoolean.delete();

    // Add an attribute of the same name but of a different datatype
    mdAttributeCharacter = MdAttributeCharacterDAO.newInstance();
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.NAME, "myAttribute");
    mdAttributeCharacter.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "My Attribute");
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.SIZE, "32");
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, testMdBusinessIF.getOid());
    mdAttributeCharacter.apply();

    // A hashed column should have been created.
    if (mdAttributeCharacter.getDefinedColumnName().equals(mdAttributeCharacter.getColumnName()))
    {
      Assert.fail("Attribute metadata does not define a hashed temporary column when it should.  Hashed column[" + mdAttributeCharacter.getColumnName() + "] vs defined column [" + mdAttributeCharacter.getDefinedColumnName() + "]");
    }

    hashedColumn = mdAttributeCharacter.getColumnName();
    hashedColumnNames.add(hashedColumn);
    if (!Database.columnExists(hashedColumn, testMdBusinessIF.getTableName()))
    {
      Assert.fail("Hashed column was not defined in the database [" + hashedColumn + "]");
    }

    // Now delete it
    mdAttributeCharacter.delete();

    // Add an attribute of the same name but of a different datatype
    mdAttributeInteger = MdAttributeIntegerDAO.newInstance();
    mdAttributeInteger.setValue(MdAttributeIntegerInfo.NAME, "myAttribute");
    mdAttributeInteger.setStructValue(MdAttributeIntegerInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "My Attribute");
    mdAttributeInteger.setValue(MdAttributeIntegerInfo.DEFINING_MD_CLASS, testMdBusinessIF.getOid());
    mdAttributeInteger.apply();

    // A hashed column should have been created.
    if (mdAttributeInteger.getDefinedColumnName().equals(mdAttributeInteger.getColumnName()))
    {
      Assert.fail("Attribute metadata does not define a hashed temporary column when it should.  Hashed column[" + mdAttributeInteger.getColumnName() + "] vs defined column [" + mdAttributeInteger.getDefinedColumnName() + "]");
    }

    hashedColumn = mdAttributeInteger.getColumnName();
    hashedColumnNames.add(hashedColumn);
    if (!Database.columnExists(hashedColumn, testMdBusinessIF.getTableName()))
    {
      Assert.fail("Hashed column was not defined in the database [" + hashedColumn + "]");
    }

    BusinessDAO object1 = BusinessDAO.newInstance(EntityMasterTestSetup.TEST_CLASS.getType());
    object1.setValue("myAttribute", "Invalid Integer");
    object1.apply();
  }

  /**
   * Testing changing a newly created attribute's index type in the middle of a
   * transaction.
   * 
   */
  @Request
  @Test
  public void testNewAttributeChangeIndexType()
  {
    newAttributeChangeIndexType();
  }

  /**
   * Testing changing a newly created attribute's index type in the middle of a
   * transaction.
   * 
   */
  @Transaction
  public void newAttributeChangeIndexType()
  {
    MdBusinessDAOIF testMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(EntityMasterTestSetup.TEST_CLASS.getType());

    MdAttributeCharacterDAO mdAttrChar = MdAttributeCharacterDAO.newInstance();
    mdAttrChar.setValue(MdAttributeCharacterInfo.NAME, "changeCharIndex");
    mdAttrChar.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Change Index Type Attribute");
    mdAttrChar.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    mdAttrChar.addItem(MdAttributeCharacterInfo.INDEX_TYPE, IndexTypes.UNIQUE_INDEX.getOid());
    mdAttrChar.setValue(MdAttributeCharacterInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttrChar.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, testMdBusinessIF.getOid());
    mdAttrChar.setValue(MdAttributeCharacterInfo.SIZE, "64");

    mdAttrChar.apply();

    String indexName = mdAttrChar.getIndexName();

    if (!Database.uniqueAttributeExists(testMdBusinessIF.getTableName(), mdAttrChar.getColumnName(), indexName))
    {
      Assert.fail("A unique indesx was not properly created.");
    }

    try
    {
      mdAttrChar.addItem(MdAttributeClobInfo.INDEX_TYPE, IndexTypes.NON_UNIQUE_INDEX.getOid());
      mdAttrChar.apply();

      if (!Database.nonUniqueAttributeExists(testMdBusinessIF.getTableName(), mdAttrChar.getColumnName(), indexName))
      {
        Assert.fail("A unique indesx was not properly created.");
      }
    }
    finally
    {
      mdAttrChar.delete();
    }
  }

  /**
   * CLOB attributes cannot be unique.
   * 
   */
  @Request
  @Test
  public void testUniqueAttributeClob()
  {
    MdBusinessDAOIF testMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(EntityMasterTestSetup.TEST_CLASS.getType());

    MdAttributeClobDAO mdAttrClob = MdAttributeClobDAO.newInstance();
    mdAttrClob.setValue(MdAttributeClobInfo.NAME, "uniqueAttrClob");
    mdAttrClob.setStructValue(MdAttributeClobInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Unique Attribute Clob");
    mdAttrClob.setValue(MdAttributeClobInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttrClob.addItem(MdAttributeClobInfo.INDEX_TYPE, IndexTypes.UNIQUE_INDEX.getOid());
    mdAttrClob.setValue(MdAttributeClobInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttrClob.setValue(MdAttributeClobInfo.DEFINING_MD_CLASS, testMdBusinessIF.getOid());

    try
    {
      mdAttrClob.apply();
    }
    catch (AttributeInvalidUniquenessConstraintException e)
    {
      // This is expected
      return;
    }

    mdAttrClob.delete();
    Assert.fail("A Clob attribute was defined to be unique.  Clob attributes cannot participate in a uniqueness constraint.");
  }

  /**
   * CLOB Attributes cannot participate in a uniqueness constraint.
   * 
   */
  @Request
  @Test
  public void testUniqueAttributeGroupClob()
  {
    MdBusinessDAOIF testMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(EntityMasterTestSetup.TEST_CLASS.getType());

    MdAttributeClobDAO mdAttrClob = MdAttributeClobDAO.newInstance();
    mdAttrClob.setValue(MdAttributeClobInfo.NAME, "uniqueAttrGrpEnum");
    mdAttrClob.setStructValue(MdAttributeClobInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Unique Attribute Clob");
    mdAttrClob.setValue(MdAttributeClobInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttrClob.setValue(MdAttributeClobInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttrClob.setValue(MdAttributeClobInfo.DEFINING_MD_CLASS, testMdBusinessIF.getOid());
    mdAttrClob.apply();

    MdIndexDAO mdIndex = MdIndexDAO.newInstance();
    mdIndex.setValue(MdIndexInfo.MD_ENTITY, testMdBusinessIF.getOid());
    mdIndex.setValue(MdIndexInfo.UNIQUE, MdAttributeBooleanInfo.TRUE);
    mdIndex.setStructValue(MdIndexInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Index");
    mdIndex.apply();

    try
    {
      // add the unique group index
      mdIndex.addAttribute(mdAttrClob, 0);
      Assert.fail("A Clob attribute was defined to be in a unique attribute group.  Clob attributes cannot participate in a uniqueness constraint.");
    }
    catch (AttributeInvalidUniquenessConstraintException e)
    {
      // This is expected
    }
    finally
    {
      mdIndex.delete();
      mdAttrClob.delete();
    }
  }

  /**
   * Tests that an attribute that should be unique should also be required.
   * 
   */
  @Request
  @Test
  public void testInvalidDefaultFloatAttribute()
  {
    MdBusinessDAOIF testMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(EntityMasterTestSetup.TEST_CLASS.getType());

    MdAttributeFloatDAO mdAttrFloat = MdAttributeFloatDAO.newInstance();
    mdAttrFloat.setValue(MdAttributeFloatInfo.NAME, "attrFloat");
    mdAttrFloat.setStructValue(MdAttributeFloatInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Attribute Float");
    mdAttrFloat.setValue(MdAttributeFloatInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttrFloat.setValue(MdAttributeFloatInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttrFloat.setValue(MdAttributeFloatInfo.DEFINING_MD_CLASS, testMdBusinessIF.getOid());
    mdAttrFloat.setValue(MdAttributeFloatInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttrFloat.setValue(MdAttributeFloatInfo.REJECT_NEGATIVE, MdAttributeBooleanInfo.TRUE);
    mdAttrFloat.setValue(MdAttributeFloatInfo.LENGTH, "2");
    mdAttrFloat.setValue(MdAttributeFloatInfo.DECIMAL, "1");
    // Valid float but not a valid default value
    mdAttrFloat.setValue(MdAttributeFloatInfo.DEFAULT_VALUE, "-1.0");

    try
    {
      mdAttrFloat.apply();

      mdAttrFloat.delete();

      Assert.fail("A float attribute was defined with an invalid value.");
    }
    catch (ProblemException e)
    {
      // This is expected
      List<ProblemIF> problems = e.getProblems();

      Assert.assertEquals(1, problems.size());
      Assert.assertTrue(problems.get(0) instanceof AttributeValueProblem);
    }

  }

  /**
   * Tests that an attribute that should be unique should also be required.
   * 
   */
  @Request
  @Test
  public void testInvalidDefaultBooleanAttribute()
  {
    MdBusinessDAOIF testMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(EntityMasterTestSetup.TEST_CLASS.getType());

    MdAttributeBooleanDAO mdAttrBoolean = MdAttributeBooleanDAO.newInstance();
    mdAttrBoolean.setValue(MdAttributeBooleanInfo.NAME, "attrBoolean");
    mdAttrBoolean.setStructValue(MdAttributeBooleanInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Attribute Boolean");
    mdAttrBoolean.setStructValue(MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.TRUE);
    mdAttrBoolean.setStructValue(MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.FALSE);
    mdAttrBoolean.setValue(MdAttributeBooleanInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttrBoolean.setValue(MdAttributeBooleanInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttrBoolean.setValue(MdAttributeBooleanInfo.DEFINING_MD_CLASS, testMdBusinessIF.getOid());

    try
    {
      mdAttrBoolean.setValue(MdAttributeBooleanInfo.DEFAULT_VALUE, "Hello!");
      mdAttrBoolean.apply();
    }
    catch (AttributeValueException e)
    {
      // This is expected
      return;
    }

    mdAttrBoolean.delete();
    Assert.fail("A boolean attribute was defined with an invalid value.");
  }

  /**
   * Tests that an attribute that should be unique should also be required.
   * 
   */
  @Request
  @Test
  public void testInvalidDefaultCharacterAttribute()
  {
    MdBusinessDAOIF testMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(EntityMasterTestSetup.TEST_CLASS.getType());

    MdAttributeCharacterDAO mdAttrCharacter = MdAttributeCharacterDAO.newInstance();
    mdAttrCharacter.setValue(MdAttributeCharacterInfo.NAME, "attrCharacter");
    mdAttrCharacter.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Attribute Character");
    mdAttrCharacter.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttrCharacter.setValue(MdAttributeCharacterInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttrCharacter.setValue(MdAttributeCharacterInfo.SIZE, "4");
    mdAttrCharacter.setValue(MdAttributeCharacterInfo.DEFAULT_VALUE, "A default character greater than 4");
    mdAttrCharacter.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, testMdBusinessIF.getOid());

    try
    {
      mdAttrCharacter.apply();
    }
    catch (AttributeLengthCharacterException e)
    {
      // This is expected
      return;
    }

    mdAttrCharacter.delete();
    Assert.fail("A character attribute was defined with an invalid value.");
  }

  /**
   * Tests that an attribute that should be unique should also be required.
   * 
   */
  @Request
  @Test
  public void testInvalidDefaultReferenceAttribute()
  {
    MdBusinessDAOIF testMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(EntityMasterTestSetup.TEST_CLASS.getType());

    MdAttributeReferenceDAO mdAttrRef = MdAttributeReferenceDAO.newInstance();
    mdAttrRef.setValue(MdAttributeReferenceInfo.NAME, "attrReference");
    mdAttrRef.setStructValue(MdAttributeReferenceInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Attribute Reference");
    mdAttrRef.setValue(MdAttributeReferenceInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttrRef.setValue(MdAttributeReferenceInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttrRef.setValue(MdAttributeReferenceInfo.REF_MD_ENTITY, MdClassInfo.ID_VALUE);
    mdAttrRef.setValue(MdAttributeReferenceInfo.DEFAULT_VALUE, "99999999-9999-9999-9999-999999999999");
    mdAttrRef.setValue(MdAttributeReferenceInfo.DEFINING_MD_CLASS, testMdBusinessIF.getOid());

    try
    {
      mdAttrRef.apply();
    }
    catch (InvalidReferenceException e)
    {
      // This is expected
      return;
    }

    mdAttrRef.delete();
    Assert.fail("A reference attribute was defined with an invalid value.");
  }

  /**
   * Tests that an attribute that should be unique should also be required.
   * 
   */
  @Request
  @Test
  public void testInvalidDefaultEnumerationAttribute()
  {
    MdEnumerationDAOIF mdEnumerationIF = MdEnumerationDAO.getMdEnumerationDAO(Constants.SYSTEM_PACKAGE + ".AllEntryEnumeration");

    MdBusinessDAOIF testMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(EntityMasterTestSetup.TEST_CLASS.getType());

    MdAttributeEnumerationDAO mdAttrEnum = MdAttributeEnumerationDAO.newInstance();
    mdAttrEnum.setValue(MdAttributeEnumerationInfo.NAME, "attrEnumeration");
    mdAttrEnum.setStructValue(MdAttributeEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Attribute Enumeration");
    mdAttrEnum.setValue(MdAttributeEnumerationInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttrEnum.setValue(MdAttributeEnumerationInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttrEnum.setValue(MdAttributeEnumerationInfo.SELECT_MULTIPLE, MdAttributeBooleanInfo.FALSE);
    mdAttrEnum.setValue(MdAttributeEnumerationInfo.MD_ENUMERATION, mdEnumerationIF.getOid());
    mdAttrEnum.setValue(MdAttributeEnumerationInfo.DEFAULT_VALUE, "An invalid enumeration item");
    mdAttrEnum.setValue(MdAttributeEnumerationInfo.DEFINING_MD_CLASS, testMdBusinessIF.getOid());

    try
    {
      mdAttrEnum.apply();
    }
    catch (AttributeValueException e)
    {
      // This is expected
      return;
    }

    mdAttrEnum.delete();
    Assert.fail("A reference attribute was defined with an invalid value.");
  }

  @Request
  @Test
  public void testInvalidNegativeDecLength()
  {
    MdBusinessDAOIF testMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(EntityMasterTestSetup.TEST_CLASS.getType());

    MdAttributeFloatDAO mdAttrFloat = MdAttributeFloatDAO.newInstance();
    mdAttrFloat.setValue(MdAttributeFloatInfo.NAME, "attrFloat");
    mdAttrFloat.setStructValue(MdAttributeFloatInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Attribute Float");
    mdAttrFloat.setValue(MdAttributeFloatInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttrFloat.setValue(MdAttributeFloatInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttrFloat.setValue(MdAttributeFloatInfo.DEFINING_MD_CLASS, testMdBusinessIF.getOid());
    mdAttrFloat.setValue(MdAttributeFloatInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttrFloat.setValue(MdAttributeFloatInfo.REJECT_NEGATIVE, MdAttributeBooleanInfo.TRUE);
    mdAttrFloat.setValue(MdAttributeFloatInfo.LENGTH, "-2");
    mdAttrFloat.setValue(MdAttributeFloatInfo.DECIMAL, "1");
    // Valid float but not a valid default value
    mdAttrFloat.setValue(MdAttributeFloatInfo.DEFAULT_VALUE, "-1.0");

    try
    {
      mdAttrFloat.apply();

      mdAttrFloat.delete();

      Assert.fail("A float attribute was defined with an invalid length value.");
    }
    catch (AttributeDefinitionLengthException e)
    {
      // This is expected
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testInvalidZeroDecLength()
  {
    MdBusinessDAOIF testMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(EntityMasterTestSetup.TEST_CLASS.getType());

    MdAttributeFloatDAO mdAttrFloat = MdAttributeFloatDAO.newInstance();
    mdAttrFloat.setValue(MdAttributeFloatInfo.NAME, "attrFloat");
    mdAttrFloat.setStructValue(MdAttributeFloatInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Attribute Float");
    mdAttrFloat.setValue(MdAttributeFloatInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttrFloat.setValue(MdAttributeFloatInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttrFloat.setValue(MdAttributeFloatInfo.DEFINING_MD_CLASS, testMdBusinessIF.getOid());
    mdAttrFloat.setValue(MdAttributeFloatInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttrFloat.setValue(MdAttributeFloatInfo.REJECT_NEGATIVE, MdAttributeBooleanInfo.TRUE);
    mdAttrFloat.setValue(MdAttributeFloatInfo.LENGTH, "0");
    mdAttrFloat.setValue(MdAttributeFloatInfo.DECIMAL, "1");
    // Valid float but not a valid default value
    mdAttrFloat.setValue(MdAttributeFloatInfo.DEFAULT_VALUE, "-1.0");

    try
    {
      mdAttrFloat.apply();

      mdAttrFloat.delete();

      Assert.fail("A float attribute was defined with an invalid length value.");
    }
    catch (AttributeDefinitionLengthException e)
    {
      // This is expected
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testInvalidNegativeDecDecimal()
  {
    MdBusinessDAOIF testMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(EntityMasterTestSetup.TEST_CLASS.getType());

    MdAttributeFloatDAO mdAttrFloat = MdAttributeFloatDAO.newInstance();
    mdAttrFloat.setValue(MdAttributeFloatInfo.NAME, "attrFloat");
    mdAttrFloat.setStructValue(MdAttributeFloatInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Attribute Float");
    mdAttrFloat.setValue(MdAttributeFloatInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttrFloat.setValue(MdAttributeFloatInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttrFloat.setValue(MdAttributeFloatInfo.DEFINING_MD_CLASS, testMdBusinessIF.getOid());
    mdAttrFloat.setValue(MdAttributeFloatInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttrFloat.setValue(MdAttributeFloatInfo.REJECT_NEGATIVE, MdAttributeBooleanInfo.TRUE);
    mdAttrFloat.setValue(MdAttributeFloatInfo.LENGTH, "5");
    mdAttrFloat.setValue(MdAttributeFloatInfo.DECIMAL, "-1");
    // Valid float but not a valid default value
    mdAttrFloat.setValue(MdAttributeFloatInfo.DEFAULT_VALUE, "-1.0");

    try
    {
      mdAttrFloat.apply();

      mdAttrFloat.delete();

      Assert.fail("A float attribute was defined with an invalid decimal value.");
    }
    catch (AttributeDefinitionDecimalException e)
    {
      // This is expected
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testInvalidZeroDecDecimal()
  {
    MdBusinessDAOIF testMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(EntityMasterTestSetup.TEST_CLASS.getType());

    MdAttributeFloatDAO mdAttrFloat = MdAttributeFloatDAO.newInstance();
    mdAttrFloat.setValue(MdAttributeFloatInfo.NAME, "attrFloat");
    mdAttrFloat.setStructValue(MdAttributeFloatInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Attribute Float");
    mdAttrFloat.setValue(MdAttributeFloatInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttrFloat.setValue(MdAttributeFloatInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttrFloat.setValue(MdAttributeFloatInfo.DEFINING_MD_CLASS, testMdBusinessIF.getOid());
    mdAttrFloat.setValue(MdAttributeFloatInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttrFloat.setValue(MdAttributeFloatInfo.REJECT_NEGATIVE, MdAttributeBooleanInfo.TRUE);
    mdAttrFloat.setValue(MdAttributeFloatInfo.LENGTH, "5");
    mdAttrFloat.setValue(MdAttributeFloatInfo.DECIMAL, "0");
    // Valid float but not a valid default value
    mdAttrFloat.setValue(MdAttributeFloatInfo.DEFAULT_VALUE, "-1.0");

    try
    {
      mdAttrFloat.apply();

      mdAttrFloat.delete();

      Assert.fail("A float attribute was defined with an invalid decimal value.");
    }
    catch (AttributeDefinitionDecimalException e)
    {
      // This is expected
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testTooBigDecDecimal()
  {
    MdBusinessDAOIF testMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(EntityMasterTestSetup.TEST_CLASS.getType());

    MdAttributeFloatDAO mdAttrFloat = MdAttributeFloatDAO.newInstance();
    mdAttrFloat.setValue(MdAttributeFloatInfo.NAME, "attrFloat");
    mdAttrFloat.setStructValue(MdAttributeFloatInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Attribute Float");
    mdAttrFloat.setValue(MdAttributeFloatInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttrFloat.setValue(MdAttributeFloatInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttrFloat.setValue(MdAttributeFloatInfo.DEFINING_MD_CLASS, testMdBusinessIF.getOid());
    mdAttrFloat.setValue(MdAttributeFloatInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttrFloat.setValue(MdAttributeFloatInfo.REJECT_NEGATIVE, MdAttributeBooleanInfo.TRUE);
    mdAttrFloat.setValue(MdAttributeFloatInfo.LENGTH, "5");
    mdAttrFloat.setValue(MdAttributeFloatInfo.DECIMAL, "6");
    // Valid float but not a valid default value
    mdAttrFloat.setValue(MdAttributeFloatInfo.DEFAULT_VALUE, "-1.0");

    try
    {
      mdAttrFloat.apply();

      mdAttrFloat.delete();

      Assert.fail("A float attribute was defined with an invalid decimal value.");
    }
    catch (AttributeDefinitionDecimalException e)
    {
      // This is expected
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

}
