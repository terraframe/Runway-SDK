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
/**
*
*/
package com.runwaysdk.dataaccess;

import java.util.List;
import java.util.Set;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;

import org.junit.Assert;

import com.runwaysdk.constants.BusinessInfo;
import com.runwaysdk.constants.EnumerationMasterInfo;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeDimensionInfo;
import com.runwaysdk.constants.MdAttributeEnumerationInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdAttributeReferenceInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.MdEnumerationInfo;
import com.runwaysdk.constants.MdTreeInfo;
import com.runwaysdk.dataaccess.database.BusinessDAOFactory;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.metadata.MdAttributeCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDimensionDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeEnumerationDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeReferenceDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdDimensionDAO;
import com.runwaysdk.dataaccess.metadata.MdEnumerationDAO;
import com.runwaysdk.dataaccess.metadata.MdPackage;
import com.runwaysdk.dataaccess.metadata.MdTreeDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;

/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved.
 * 
 * This file is part of Runway SDK(tm).
 * 
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 * 
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
public class IdPropigationTest extends TestCase
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

  public static Test suite()
  {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(IdPropigationTest.class);

    TestSetup wrapper = new TestSetup(suite)
    {
      // Heads up: uncomment
      // protected void setUp()
      // {
      // classSetUp();
      // }
      //
      // protected void tearDown()
      // {
      // classTearDown();
      // }

    };

    return wrapper;
  }

  /*
   * (non-Javadoc)
   * 
   * @see junit.framework.TestCase#setUp()
   */
  @Override
  protected void setUp() throws Exception
  {
    // new MdPackage("test.xmlclasses").delete();
  }

  /**
   * Delete all MetaData objects which were created in the class
   * 
   * @see junit.framework.TestCase#tearDown()
   */
  protected void tearDown() throws Exception
  {
    new MdPackage("test.xmlclasses").delete();
  }

  public void testSingleCachedEnumerationAttribute()
  {
    MdBusinessDAO mdBusinessEnum = TestFixtureFactory.createEnumClass1();
    mdBusinessEnum.setValue(MdBusinessInfo.HAS_DETERMINISTIC_IDS, MdAttributeBooleanInfo.FALSE);
    mdBusinessEnum.apply();

    MdAttributeCharacterDAO mdEnumAttribute = TestFixtureFactory.addCharacterAttribute(mdBusinessEnum);
    mdEnumAttribute.apply();

    EnumerationItemDAO item = EnumerationItemDAO.newInstance(mdBusinessEnum.definesType());
    item.setValue(EnumerationMasterInfo.NAME, "CO");
    item.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Colorado");
    item.setValue(mdEnumAttribute.definesAttribute(), "CO");
    item.apply();

    MdEnumerationDAO mdEnumeration = TestFixtureFactory.createMdEnumeation1(mdBusinessEnum);
    mdEnumeration.setValue(MdEnumerationInfo.INCLUDE_ALL, MdAttributeBooleanInfo.TRUE);
    mdEnumeration.apply();

    MdBusinessDAO mdBusiness = TestFixtureFactory.createMdBusiness1();
    mdBusiness.apply();

    MdAttributeEnumerationDAO mdAttributeEnum = TestFixtureFactory.addEnumerationAttribute(mdBusiness, mdEnumeration);
    mdAttributeEnum.apply();

    BusinessDAO business = BusinessDAO.newInstance(mdBusiness.definesType());
    business.addItem(mdAttributeEnum.definesAttribute(), item.getId());
    business.apply();

    mdBusinessEnum = MdBusinessDAO.get(mdBusinessEnum.getId()).getBusinessDAO();
    mdBusinessEnum.setValue(MdBusinessInfo.HAS_DETERMINISTIC_IDS, MdAttributeBooleanInfo.TRUE);
    mdBusinessEnum.apply();

    // Update the id of the item
    this.updateItems(mdBusinessEnum, item);

    // Ensure the id of the enumerated item is different
    BusinessDAOIF testItem = this.getItem(mdBusinessEnum, item);
    assertFalse(testItem.getId().equals(item.getId()));

    BusinessDAOIF test = BusinessDAO.get(business.getId());
    AttributeEnumerationIF attributeIF = (AttributeEnumerationIF) test.getAttributeIF(mdAttributeEnum.definesAttribute());

    // Ensure the referenced item of the enumeration attribute has been updated
    Set<String> enumItemIdList = attributeIF.getEnumItemIdList();

    assertEquals(1, enumItemIdList.size());

    assertTrue(enumItemIdList.contains(testItem.getId()));

    // Ensure the cached reference of the enumeration attribute has been updated
    String cachedItems = Database.getEnumCacheFieldInTable(mdBusiness.getTableName(), mdAttributeEnum.getDefinedCacheColumnName(), test.getId());

    assertEquals(testItem.getId(), cachedItems);
  }

  public void testMultipleCachedEnumerationAttribute()
  {
    MdBusinessDAO mdBusinessEnum = TestFixtureFactory.createEnumClass1();
    mdBusinessEnum.setValue(MdBusinessInfo.HAS_DETERMINISTIC_IDS, MdAttributeBooleanInfo.FALSE);
    mdBusinessEnum.apply();

    MdAttributeCharacterDAO mdEnumAttribute = TestFixtureFactory.addCharacterAttribute(mdBusinessEnum);
    mdEnumAttribute.apply();

    EnumerationItemDAO colorado = EnumerationItemDAO.newInstance(mdBusinessEnum.definesType());
    colorado.setValue(EnumerationMasterInfo.NAME, "CO");
    colorado.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Colorado");
    colorado.setValue(mdEnumAttribute.definesAttribute(), "CO");
    colorado.apply();

    EnumerationItemDAO texas = EnumerationItemDAO.newInstance(mdBusinessEnum.definesType());
    texas.setValue(EnumerationMasterInfo.NAME, "TX");
    texas.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Colorado");
    texas.setValue(mdEnumAttribute.definesAttribute(), "TX");
    texas.apply();

    MdEnumerationDAO mdEnumeration = TestFixtureFactory.createMdEnumeation1(mdBusinessEnum);
    mdEnumeration.setValue(MdEnumerationInfo.INCLUDE_ALL, MdAttributeBooleanInfo.TRUE);
    mdEnumeration.apply();

    MdBusinessDAO mdBusiness = TestFixtureFactory.createMdBusiness1();
    mdBusiness.apply();

    MdAttributeEnumerationDAO mdAttributeEnum = TestFixtureFactory.addEnumerationAttribute(mdBusiness, mdEnumeration);
    mdAttributeEnum.setValue(MdAttributeEnumerationInfo.SELECT_MULTIPLE, MdAttributeBooleanInfo.TRUE);
    mdAttributeEnum.apply();

    BusinessDAO business = BusinessDAO.newInstance(mdBusiness.definesType());
    business.addItem(mdAttributeEnum.definesAttribute(), colorado.getId());
    business.addItem(mdAttributeEnum.definesAttribute(), texas.getId());
    business.apply();

    mdBusinessEnum = MdBusinessDAO.get(mdBusinessEnum.getId()).getBusinessDAO();
    mdBusinessEnum.setValue(MdBusinessInfo.HAS_DETERMINISTIC_IDS, MdAttributeBooleanInfo.TRUE);
    mdBusinessEnum.apply();

    // Update the ids of the item
    EnumerationItemDAO[] array = new EnumerationItemDAO[] { colorado, texas };

    updateItems(mdBusinessEnum, array);

    BusinessDAOIF test = BusinessDAO.get(business.getId());
    AttributeEnumerationIF attributeIF = (AttributeEnumerationIF) test.getAttributeIF(mdAttributeEnum.definesAttribute());

    for (EnumerationItemDAO item : array)
    {
      BusinessDAOIF testItem = this.getItem(mdBusinessEnum, item);

      // Ensure the referenced item of the enumeration attribute has been
      // updated
      Set<String> enumItemIdList = attributeIF.getEnumItemIdList();

      assertEquals(2, enumItemIdList.size());

      assertTrue(enumItemIdList.contains(testItem.getId()));

      // Ensure the cached reference of the enumeration attribute has been
      // updated
      String cachedItems = Database.getEnumCacheFieldInTable(mdBusiness.getTableName(), mdAttributeEnum.getDefinedCacheColumnName(), test.getId());

      System.out.println(testItem.getId() + "--:--" + cachedItems);

      assertTrue(cachedItems.contains(testItem.getId()));
    }
  }

  public void testEnumerationDefaultValue()
  {
    MdBusinessDAO mdBusinessEnum = TestFixtureFactory.createEnumClass1();
    mdBusinessEnum.setValue(MdBusinessInfo.HAS_DETERMINISTIC_IDS, MdAttributeBooleanInfo.FALSE);
    mdBusinessEnum.apply();

    MdAttributeCharacterDAO mdEnumAttribute = TestFixtureFactory.addCharacterAttribute(mdBusinessEnum);
    mdEnumAttribute.apply();

    EnumerationItemDAO colorado = EnumerationItemDAO.newInstance(mdBusinessEnum.definesType());
    colorado.setValue(EnumerationMasterInfo.NAME, "CO");
    colorado.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Colorado");
    colorado.setValue(mdEnumAttribute.definesAttribute(), "CO");
    colorado.apply();

    MdEnumerationDAO mdEnumeration = TestFixtureFactory.createMdEnumeation1(mdBusinessEnum);
    mdEnumeration.setValue(MdEnumerationInfo.INCLUDE_ALL, MdAttributeBooleanInfo.TRUE);
    mdEnumeration.apply();

    MdBusinessDAO mdBusiness = TestFixtureFactory.createMdBusiness1();
    mdBusiness.apply();

    MdAttributeEnumerationDAO mdAttributeEnum = TestFixtureFactory.addEnumerationAttribute(mdBusiness, mdEnumeration);
    mdAttributeEnum.setValue(MdAttributeEnumerationInfo.SELECT_MULTIPLE, MdAttributeBooleanInfo.FALSE);
    mdAttributeEnum.setValue(MdAttributeEnumerationInfo.DEFAULT_VALUE, colorado.getId());
    mdAttributeEnum.apply();

    mdBusinessEnum = MdBusinessDAO.get(mdBusinessEnum.getId()).getBusinessDAO();
    mdBusinessEnum.setValue(MdBusinessInfo.HAS_DETERMINISTIC_IDS, MdAttributeBooleanInfo.TRUE);
    mdBusinessEnum.apply();

    // Update the ids of the item
    updateItems(mdBusinessEnum, colorado);

    BusinessDAOIF testItem = this.getItem(mdBusinessEnum, colorado);

    // Ensure the default value has been updated
    MdAttributeEnumerationDAOIF testMdAttributeEnumeration = MdAttributeEnumerationDAO.get(mdAttributeEnum.getId());

    assertEquals(testItem.getId(), testMdAttributeEnumeration.getValue(MdAttributeEnumerationInfo.DEFAULT_VALUE));
  }

  public void testEnumerationDimensionDefaultValue()
  {
    MdDimensionDAO mdDimension = TestFixtureFactory.createMdDimension();
    mdDimension.apply();

    try
    {

      MdBusinessDAO mdBusinessEnum = TestFixtureFactory.createEnumClass1();
      mdBusinessEnum.setValue(MdBusinessInfo.HAS_DETERMINISTIC_IDS, MdAttributeBooleanInfo.FALSE);
      mdBusinessEnum.apply();

      MdAttributeCharacterDAO mdEnumAttribute = TestFixtureFactory.addCharacterAttribute(mdBusinessEnum);
      mdEnumAttribute.apply();

      EnumerationItemDAO colorado = EnumerationItemDAO.newInstance(mdBusinessEnum.definesType());
      colorado.setValue(EnumerationMasterInfo.NAME, "CO");
      colorado.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Colorado");
      colorado.setValue(mdEnumAttribute.definesAttribute(), "CO");
      colorado.apply();

      MdEnumerationDAO mdEnumeration = TestFixtureFactory.createMdEnumeation1(mdBusinessEnum);
      mdEnumeration.setValue(MdEnumerationInfo.INCLUDE_ALL, MdAttributeBooleanInfo.TRUE);
      mdEnumeration.apply();

      MdBusinessDAO mdBusiness = TestFixtureFactory.createMdBusiness1();
      mdBusiness.apply();

      MdAttributeEnumerationDAO mdAttributeEnum = TestFixtureFactory.addEnumerationAttribute(mdBusiness, mdEnumeration);
      mdAttributeEnum.setValue(MdAttributeEnumerationInfo.SELECT_MULTIPLE, MdAttributeBooleanInfo.FALSE);
      mdAttributeEnum.apply();

      MdAttributeDimensionDAO mdAttributeDimension = mdAttributeEnum.getMdAttributeDimension(mdDimension).getBusinessDAO();
      mdAttributeDimension.setValue(MdAttributeDimensionInfo.DEFAULT_VALUE, colorado.getId());
      mdAttributeDimension.apply();

      mdBusinessEnum = MdBusinessDAO.get(mdBusinessEnum.getId()).getBusinessDAO();
      mdBusinessEnum.setValue(MdBusinessInfo.HAS_DETERMINISTIC_IDS, MdAttributeBooleanInfo.TRUE);
      mdBusinessEnum.apply();

      // Update the ids of the item
      updateItems(mdBusinessEnum, colorado);

      BusinessDAOIF testItem = this.getItem(mdBusinessEnum, colorado);

      // Ensure the default value has been updated
      MdAttributeEnumerationDAOIF testMdAttributeEnumeration = MdAttributeEnumerationDAO.get(mdAttributeEnum.getId());
      MdAttributeDimensionDAOIF testMdAttributeDimension = testMdAttributeEnumeration.getMdAttributeDimension(mdDimension);

      assertEquals(testItem.getId(), testMdAttributeDimension.getValue(MdAttributeEnumerationInfo.DEFAULT_VALUE));
    }
    finally
    {
      TestFixtureFactory.delete(mdDimension);
    }
  }

  public void testReferenceAttribute()
  {
    MdBusinessDAO referenceMdBusiness = TestFixtureFactory.createMdBusiness2();
    referenceMdBusiness.setValue(MdBusinessInfo.HAS_DETERMINISTIC_IDS, MdAttributeBooleanInfo.FALSE);
    referenceMdBusiness.apply();

    MdAttributeCharacterDAO mdEnumAttribute = TestFixtureFactory.addCharacterAttribute(referenceMdBusiness);
    mdEnumAttribute.apply();

    BusinessDAO item = BusinessDAO.newInstance(referenceMdBusiness.definesType());
    item.setValue(mdEnumAttribute.definesAttribute(), "CO");
    item.setValue(BusinessInfo.KEY, "TEST-KEY");
    item.apply();

    MdBusinessDAO mdBusiness = TestFixtureFactory.createMdBusiness1();
    mdBusiness.apply();

    MdAttributeReferenceDAO mdAttributeEnum = TestFixtureFactory.addReferenceAttribute(mdBusiness, referenceMdBusiness);
    mdAttributeEnum.apply();

    BusinessDAO business = BusinessDAO.newInstance(mdBusiness.definesType());
    business.setValue(mdAttributeEnum.definesAttribute(), item.getId());
    business.apply();

    referenceMdBusiness = MdBusinessDAO.get(referenceMdBusiness.getId()).getBusinessDAO();
    referenceMdBusiness.setValue(MdBusinessInfo.HAS_DETERMINISTIC_IDS, MdAttributeBooleanInfo.TRUE);
    referenceMdBusiness.apply();

    // Update the id of the item
    this.updateItems(referenceMdBusiness, item);

    // Ensure the id of the enumerated item is different
    BusinessDAOIF testItem = this.getItem(referenceMdBusiness, item);
    assertFalse(testItem.getId().equals(item.getId()));

    BusinessDAOIF test = BusinessDAO.get(business.getId());
    AttributeReferenceIF attributeIF = (AttributeReferenceIF) test.getAttributeIF(mdAttributeEnum.definesAttribute());

    // Ensure the referenced item of the enumeration attribute has been updated
    assertEquals(testItem.getId(), attributeIF.getValue());
  }

  public void testReferenceDefaultValue()
  {
    MdBusinessDAO referenceMdBusiness = TestFixtureFactory.createMdBusiness2();
    referenceMdBusiness.setValue(MdBusinessInfo.HAS_DETERMINISTIC_IDS, MdAttributeBooleanInfo.FALSE);
    referenceMdBusiness.apply();

    MdAttributeCharacterDAO mdEnumAttribute = TestFixtureFactory.addCharacterAttribute(referenceMdBusiness);
    mdEnumAttribute.apply();

    BusinessDAO colorado = BusinessDAO.newInstance(referenceMdBusiness.definesType());
    colorado.setValue(mdEnumAttribute.definesAttribute(), "CO");
    colorado.setValue(BusinessInfo.KEY, "CO");
    colorado.apply();

    MdBusinessDAO mdBusiness = TestFixtureFactory.createMdBusiness1();
    mdBusiness.apply();

    MdAttributeReferenceDAO mdAttributeEnum = TestFixtureFactory.addReferenceAttribute(mdBusiness, referenceMdBusiness);
    mdAttributeEnum.setValue(MdAttributeReferenceInfo.DEFAULT_VALUE, colorado.getId());
    mdAttributeEnum.apply();

    referenceMdBusiness = MdBusinessDAO.get(referenceMdBusiness.getId()).getBusinessDAO();
    referenceMdBusiness.setValue(MdBusinessInfo.HAS_DETERMINISTIC_IDS, MdAttributeBooleanInfo.TRUE);
    referenceMdBusiness.apply();

    // Update the ids of the item
    updateItems(referenceMdBusiness, colorado);

    BusinessDAOIF testItem = this.getItem(referenceMdBusiness, colorado);

    // Ensure the default value has been updated
    MdAttributeReferenceDAOIF testMdAttributeReference = MdAttributeReferenceDAO.get(mdAttributeEnum.getId());

    assertEquals(testItem.getId(), testMdAttributeReference.getValue(MdAttributeReferenceInfo.DEFAULT_VALUE));
  }

  public void testReferenceDimensionDefaultValue()
  {
    MdDimensionDAO mdDimension = TestFixtureFactory.createMdDimension();
    mdDimension.apply();

    try
    {

      MdBusinessDAO referenceMdBusiness = TestFixtureFactory.createMdBusiness2();
      referenceMdBusiness.setValue(MdBusinessInfo.HAS_DETERMINISTIC_IDS, MdAttributeBooleanInfo.FALSE);
      referenceMdBusiness.apply();

      MdAttributeCharacterDAO mdEnumAttribute = TestFixtureFactory.addCharacterAttribute(referenceMdBusiness);
      mdEnumAttribute.apply();

      BusinessDAO colorado = BusinessDAO.newInstance(referenceMdBusiness.definesType());
      colorado.setValue(mdEnumAttribute.definesAttribute(), "CO");
      colorado.setValue(BusinessInfo.KEY, "CO");
      colorado.apply();

      MdBusinessDAO mdBusiness = TestFixtureFactory.createMdBusiness1();
      mdBusiness.apply();

      MdAttributeReferenceDAO mdAttributeEnum = TestFixtureFactory.addReferenceAttribute(mdBusiness, referenceMdBusiness);
      mdAttributeEnum.apply();

      MdAttributeDimensionDAO mdAttributeDimension = mdAttributeEnum.getMdAttributeDimension(mdDimension).getBusinessDAO();
      mdAttributeDimension.setValue(MdAttributeDimensionInfo.DEFAULT_VALUE, colorado.getId());
      mdAttributeDimension.apply();

      referenceMdBusiness = MdBusinessDAO.get(referenceMdBusiness.getId()).getBusinessDAO();
      referenceMdBusiness.setValue(MdBusinessInfo.HAS_DETERMINISTIC_IDS, MdAttributeBooleanInfo.TRUE);
      referenceMdBusiness.apply();

      // Update the ids of the item
      updateItems(referenceMdBusiness, colorado);

      BusinessDAOIF testItem = this.getItem(referenceMdBusiness, colorado);

      // Ensure the default value has been updated
      MdAttributeReferenceDAOIF testMdAttributeReference = MdAttributeReferenceDAO.get(mdAttributeEnum.getId());
      MdAttributeDimensionDAOIF testMdAttributeDimension = testMdAttributeReference.getMdAttributeDimension(mdDimension);

      assertEquals(testItem.getId(), testMdAttributeDimension.getValue(MdAttributeReferenceInfo.DEFAULT_VALUE));
    }
    finally
    {
      TestFixtureFactory.delete(mdDimension);
    }
  }

  public void testMerge()
  {
    mergeInTransaction();
  }

  @Transaction
  private void mergeInTransaction()
  {
    MdBusinessDAO referenceMdBusiness = TestFixtureFactory.createMdBusiness2();
    referenceMdBusiness.setValue(MdBusinessInfo.HAS_DETERMINISTIC_IDS, MdAttributeBooleanInfo.FALSE);
    referenceMdBusiness.apply();

    MdAttributeCharacterDAO mdEnumAttribute = TestFixtureFactory.addCharacterAttribute(referenceMdBusiness);
    mdEnumAttribute.apply();

    BusinessDAO item1 = BusinessDAO.newInstance(referenceMdBusiness.definesType());
    item1.setValue(mdEnumAttribute.definesAttribute(), "CO");
    item1.setValue(BusinessInfo.KEY, "CO");
    item1.apply();

    BusinessDAO item2 = BusinessDAO.newInstance(referenceMdBusiness.definesType());
    item2.setValue(mdEnumAttribute.definesAttribute(), "CA");
    item2.setValue(BusinessInfo.KEY, "CA");
    item2.apply();

    MdBusinessDAO mdBusiness = TestFixtureFactory.createMdBusiness1();
    mdBusiness.apply();

    MdAttributeReferenceDAO mdAttributeReference = TestFixtureFactory.addReferenceAttribute(mdBusiness, referenceMdBusiness);
    mdAttributeReference.apply();

    BusinessDAO business1 = BusinessDAO.newInstance(mdBusiness.definesType());
    business1.setValue(mdAttributeReference.definesAttribute(), item1.getId());
    business1.apply();

    BusinessDAO business2 = BusinessDAO.newInstance(mdBusiness.definesType());
    business2.setValue(mdAttributeReference.definesAttribute(), item2.getId());
    business2.apply();

    // Convert item1 references to item2
    BusinessDAOFactory.floatObjectIdReferences(item1, item1.getId(), item2.getId());

    // Delete item1
    item1.delete();

    // Ensure the business1 reference was updated to be item2
    BusinessDAOIF test = BusinessDAO.get(business1.getId());

    assertEquals(item2.getId(), test.getValue(mdAttributeReference.definesAttribute()));
  }

  public void testMergeIgnoreDatabaseExceptions()
  {
    mergeInTransactionIgnoreDatabaseExceptions();
  }

  @Transaction
  private void mergeInTransactionIgnoreDatabaseExceptions()
  {
    MdBusinessDAO mdBusiness = TestFixtureFactory.createMdBusiness1();
    mdBusiness.apply();

    MdTreeDAO mdTree = TestFixtureFactory.createMdTree(mdBusiness, mdBusiness);
    mdTree.setValue(MdTreeInfo.PARENT_CARDINALITY, "*");
    mdTree.setValue(MdTreeInfo.CHILD_CARDINALITY, "*");
    mdTree.apply();

    BusinessDAO parent1 = BusinessDAO.newInstance(mdBusiness.definesType());
    parent1.apply();

    BusinessDAO parent2 = BusinessDAO.newInstance(mdBusiness.definesType());
    parent2.apply();

    BusinessDAO business1 = BusinessDAO.newInstance(mdBusiness.definesType());
    business1.apply();

    BusinessDAO business2 = BusinessDAO.newInstance(mdBusiness.definesType());
    business2.apply();

    business1.addParent(parent1.getId(), mdTree.definesType()).apply();
    business1.addParent(parent2.getId(), mdTree.definesType()).apply();
    business2.addParent(parent2.getId(), mdTree.definesType()).apply();

    // Convert business 1 references to business 2
    BusinessDAOFactory.floatObjectIdReferences(business1, business1.getId(), business2.getId(), true);

    business1.delete();

    List<RelationshipDAOIF> parents = business2.getAllParents();

    Assert.assertEquals(2, parents.size());
  }

  /**
   * @param mdBusinessEnum
   * @param items
   */
  private void updateItems(MdBusinessDAO mdBusinessEnum, BusinessDAO... items)
  {
    for (BusinessDAO item : items)
    {
      BusinessDAO updateItem = BusinessDAO.get(mdBusinessEnum.definesType(), item.getKey()).getBusinessDAO();

      // Important: We must set the key to modified even though it hasn't been
      // changed in order to force the predictive id algorithm to run
      updateItem.getAttribute(BusinessInfo.KEY).setModified(true);
      updateItem.apply();
    }
  }

  /**
   * @param mdBusinessEnum
   * @param item
   * @return
   */
  private BusinessDAOIF getItem(MdBusinessDAO mdBusinessEnum, BusinessDAO item)
  {
    return BusinessDAO.get(mdBusinessEnum.definesType(), item.getKey());
  }
}
