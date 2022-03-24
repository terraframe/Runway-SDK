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
/**
*
*/
package com.runwaysdk.dataaccess;

import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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
import com.runwaysdk.session.Request;
public class IdPropigationTest
{
  /*
   * (non-Javadoc)
   * 
   * @see junit.framework.TestCase#setUp()
   */
  @Request
  @Before
  public void setUp() throws Exception
  {
    // new MdPackage("test.xmlclasses").delete();
  }

  /**
   * Delete all MetaData objects which were created in the class
   * 
   * @see junit.framework.TestCase#tearDown()
   */
  @Request
  @After
  public void tearDown() throws Exception
  {
    new MdPackage("test.xmlclasses").delete();
  }

  @Request
  @Test
  public void testSingleCachedEnumerationAttribute()
  {
    MdBusinessDAO mdBusinessEnum = TestFixtureFactory.createEnumClass1();
    mdBusinessEnum.setValue(MdBusinessInfo.HAS_DETERMINISTIC_IDS, MdAttributeBooleanInfo.FALSE);
    mdBusinessEnum.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusinessEnum.apply();

    MdAttributeCharacterDAO mdEnumAttribute = TestFixtureFactory.addCharacterAttribute(mdBusinessEnum);
    mdEnumAttribute.apply();

    EnumerationItemDAO item = EnumerationItemDAO.newInstance(mdBusinessEnum.definesType());
    item.setValue(EnumerationMasterInfo.NAME, "CO");
    item.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Colorado");
    item.setValue(mdEnumAttribute.definesAttribute(), "CO");
    item.apply();

    MdEnumerationDAO mdEnumeration = TestFixtureFactory.createMdEnumeration1(mdBusinessEnum);
    mdEnumeration.setValue(MdEnumerationInfo.INCLUDE_ALL, MdAttributeBooleanInfo.TRUE);
    mdEnumeration.setValue(MdEnumerationInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdEnumeration.apply();

    MdBusinessDAO mdBusiness = TestFixtureFactory.createMdBusiness1();
    mdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness.apply();

    MdAttributeEnumerationDAO mdAttributeEnum = TestFixtureFactory.addEnumerationAttribute(mdBusiness, mdEnumeration);
    mdAttributeEnum.apply();

    BusinessDAO business = BusinessDAO.newInstance(mdBusiness.definesType());
    business.addItem(mdAttributeEnum.definesAttribute(), item.getOid());
    business.apply();

    mdBusinessEnum = MdBusinessDAO.get(mdBusinessEnum.getOid()).getBusinessDAO();
    mdBusinessEnum.setValue(MdBusinessInfo.HAS_DETERMINISTIC_IDS, MdAttributeBooleanInfo.TRUE);
    mdBusinessEnum.apply();

    // Update the oid of the item
    this.updateItems(mdBusinessEnum, item);

    // Ensure the oid of the enumerated item is different
    BusinessDAOIF testItem = this.getItem(mdBusinessEnum, item);
    Assert.assertFalse(testItem.getOid().equals(item.getOid()));

    BusinessDAOIF test = BusinessDAO.get(business.getOid());
    AttributeEnumerationIF attributeIF = (AttributeEnumerationIF) test.getAttributeIF(mdAttributeEnum.definesAttribute());

    // Ensure the referenced item of the enumeration attribute has been updated
    Set<String> enumItemIdList = attributeIF.getEnumItemIdList();

    Assert.assertEquals(1, enumItemIdList.size());

    Assert.assertTrue(enumItemIdList.contains(testItem.getOid()));

    // Ensure the cached reference of the enumeration attribute has been updated
    String cachedItems = Database.getEnumCacheFieldInTable(mdBusiness.getTableName(), mdAttributeEnum.getDefinedCacheColumnName(), test.getOid());

    Assert.assertEquals(testItem.getOid(), cachedItems);
  }

  @Request
  @Test
  public void testMultipleCachedEnumerationAttribute()
  {
    MdBusinessDAO mdBusinessEnum = TestFixtureFactory.createEnumClass1();
    mdBusinessEnum.setValue(MdBusinessInfo.HAS_DETERMINISTIC_IDS, MdAttributeBooleanInfo.FALSE);
    mdBusinessEnum.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
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

    MdEnumerationDAO mdEnumeration = TestFixtureFactory.createMdEnumeration1(mdBusinessEnum);
    mdEnumeration.setValue(MdEnumerationInfo.INCLUDE_ALL, MdAttributeBooleanInfo.TRUE);
    mdEnumeration.setValue(MdEnumerationInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdEnumeration.apply();

    MdBusinessDAO mdBusiness = TestFixtureFactory.createMdBusiness1();
    mdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness.apply();

    MdAttributeEnumerationDAO mdAttributeEnum = TestFixtureFactory.addEnumerationAttribute(mdBusiness, mdEnumeration);
    mdAttributeEnum.setValue(MdAttributeEnumerationInfo.SELECT_MULTIPLE, MdAttributeBooleanInfo.TRUE);
    mdAttributeEnum.apply();

    BusinessDAO business = BusinessDAO.newInstance(mdBusiness.definesType());
    business.addItem(mdAttributeEnum.definesAttribute(), colorado.getOid());
    business.addItem(mdAttributeEnum.definesAttribute(), texas.getOid());
    business.apply();

    mdBusinessEnum = MdBusinessDAO.get(mdBusinessEnum.getOid()).getBusinessDAO();
    mdBusinessEnum.setValue(MdBusinessInfo.HAS_DETERMINISTIC_IDS, MdAttributeBooleanInfo.TRUE);
    mdBusinessEnum.apply();

    // Update the ids of the item
    EnumerationItemDAO[] array = new EnumerationItemDAO[] { colorado, texas };

    updateItems(mdBusinessEnum, array);

    BusinessDAOIF test = BusinessDAO.get(business.getOid());
    AttributeEnumerationIF attributeIF = (AttributeEnumerationIF) test.getAttributeIF(mdAttributeEnum.definesAttribute());

    for (EnumerationItemDAO item : array)
    {
      BusinessDAOIF testItem = this.getItem(mdBusinessEnum, item);

      // Ensure the referenced item of the enumeration attribute has been
      // updated
      Set<String> enumItemIdList = attributeIF.getEnumItemIdList();

      Assert.assertEquals(2, enumItemIdList.size());

      String oid = testItem.getOid();

      Assert.assertTrue(enumItemIdList.contains(oid));

      // Ensure the cached reference of the enumeration attribute has been
      // updated
      String cachedItems = Database.getEnumCacheFieldInTable(mdBusiness.getTableName(), mdAttributeEnum.getDefinedCacheColumnName(), test.getOid());

      Assert.assertTrue("[" + oid + "] Not found in cached items [" + cachedItems + "]", cachedItems.contains(oid));
    }
  }

  @Request
  @Test
  public void testEnumerationDefaultValue()
  {
    MdBusinessDAO mdBusinessEnum = TestFixtureFactory.createEnumClass1();
    mdBusinessEnum.setValue(MdBusinessInfo.HAS_DETERMINISTIC_IDS, MdAttributeBooleanInfo.FALSE);
    mdBusinessEnum.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusinessEnum.apply();

    MdAttributeCharacterDAO mdEnumAttribute = TestFixtureFactory.addCharacterAttribute(mdBusinessEnum);
    mdEnumAttribute.apply();

    EnumerationItemDAO colorado = EnumerationItemDAO.newInstance(mdBusinessEnum.definesType());
    colorado.setValue(EnumerationMasterInfo.NAME, "CO");
    colorado.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Colorado");
    colorado.setValue(mdEnumAttribute.definesAttribute(), "CO");
    colorado.apply();

    MdEnumerationDAO mdEnumeration = TestFixtureFactory.createMdEnumeration1(mdBusinessEnum);
    mdEnumeration.setValue(MdEnumerationInfo.INCLUDE_ALL, MdAttributeBooleanInfo.TRUE);
    mdEnumeration.setValue(MdEnumerationInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdEnumeration.apply();

    MdBusinessDAO mdBusiness = TestFixtureFactory.createMdBusiness1();
    mdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness.apply();

    MdAttributeEnumerationDAO mdAttributeEnum = TestFixtureFactory.addEnumerationAttribute(mdBusiness, mdEnumeration);
    mdAttributeEnum.setValue(MdAttributeEnumerationInfo.SELECT_MULTIPLE, MdAttributeBooleanInfo.FALSE);
    mdAttributeEnum.setValue(MdAttributeEnumerationInfo.DEFAULT_VALUE, colorado.getOid());
    mdAttributeEnum.apply();

    mdBusinessEnum = MdBusinessDAO.get(mdBusinessEnum.getOid()).getBusinessDAO();
    mdBusinessEnum.setValue(MdBusinessInfo.HAS_DETERMINISTIC_IDS, MdAttributeBooleanInfo.TRUE);
    mdBusinessEnum.apply();

    // Update the ids of the item
    updateItems(mdBusinessEnum, colorado);

    BusinessDAOIF testItem = this.getItem(mdBusinessEnum, colorado);

    // Ensure the default value has been updated
    MdAttributeEnumerationDAOIF testMdAttributeEnumeration = MdAttributeEnumerationDAO.get(mdAttributeEnum.getOid());

    Assert.assertEquals(testItem.getOid(), testMdAttributeEnumeration.getValue(MdAttributeEnumerationInfo.DEFAULT_VALUE));
  }

  @Request
  @Test
  public void testEnumerationDimensionDefaultValue()
  {
    MdDimensionDAO mdDimension = TestFixtureFactory.createMdDimension();
    mdDimension.apply();

    try
    {

      MdBusinessDAO mdBusinessEnum = TestFixtureFactory.createEnumClass1();
      mdBusinessEnum.setValue(MdBusinessInfo.HAS_DETERMINISTIC_IDS, MdAttributeBooleanInfo.FALSE);
      mdBusinessEnum.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      mdBusinessEnum.apply();

      MdAttributeCharacterDAO mdEnumAttribute = TestFixtureFactory.addCharacterAttribute(mdBusinessEnum);
      mdEnumAttribute.apply();

      EnumerationItemDAO colorado = EnumerationItemDAO.newInstance(mdBusinessEnum.definesType());
      colorado.setValue(EnumerationMasterInfo.NAME, "CO");
      colorado.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Colorado");
      colorado.setValue(mdEnumAttribute.definesAttribute(), "CO");
      colorado.apply();

      MdEnumerationDAO mdEnumeration = TestFixtureFactory.createMdEnumeration1(mdBusinessEnum);
      mdEnumeration.setValue(MdEnumerationInfo.INCLUDE_ALL, MdAttributeBooleanInfo.TRUE);
      mdEnumeration.setValue(MdEnumerationInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      mdEnumeration.apply();

      MdBusinessDAO mdBusiness = TestFixtureFactory.createMdBusiness1();
      mdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      mdBusiness.apply();

      MdAttributeEnumerationDAO mdAttributeEnum = TestFixtureFactory.addEnumerationAttribute(mdBusiness, mdEnumeration);
      mdAttributeEnum.setValue(MdAttributeEnumerationInfo.SELECT_MULTIPLE, MdAttributeBooleanInfo.FALSE);
      mdAttributeEnum.apply();

      MdAttributeDimensionDAO mdAttributeDimension = mdAttributeEnum.getMdAttributeDimension(mdDimension).getBusinessDAO();
      mdAttributeDimension.setValue(MdAttributeDimensionInfo.DEFAULT_VALUE, colorado.getOid());
      mdAttributeDimension.apply();

      mdBusinessEnum = MdBusinessDAO.get(mdBusinessEnum.getOid()).getBusinessDAO();
      mdBusinessEnum.setValue(MdBusinessInfo.HAS_DETERMINISTIC_IDS, MdAttributeBooleanInfo.TRUE);
      mdBusinessEnum.apply();

      // Update the ids of the item
      updateItems(mdBusinessEnum, colorado);

      BusinessDAOIF testItem = this.getItem(mdBusinessEnum, colorado);

      // Ensure the default value has been updated
      MdAttributeEnumerationDAOIF testMdAttributeEnumeration = MdAttributeEnumerationDAO.get(mdAttributeEnum.getOid());
      MdAttributeDimensionDAOIF testMdAttributeDimension = testMdAttributeEnumeration.getMdAttributeDimension(mdDimension);

      Assert.assertEquals(testItem.getOid(), testMdAttributeDimension.getValue(MdAttributeEnumerationInfo.DEFAULT_VALUE));
    }
    finally
    {
      TestFixtureFactory.delete(mdDimension);
    }
  }

  @Request
  @Test
  public void testReferenceAttribute()
  {
    MdBusinessDAO referenceMdBusiness = TestFixtureFactory.createMdBusiness2();
    referenceMdBusiness.setValue(MdBusinessInfo.HAS_DETERMINISTIC_IDS, MdAttributeBooleanInfo.FALSE);
    referenceMdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    referenceMdBusiness.apply();

    MdAttributeCharacterDAO mdEnumAttribute = TestFixtureFactory.addCharacterAttribute(referenceMdBusiness);
    mdEnumAttribute.apply();

    BusinessDAO item = BusinessDAO.newInstance(referenceMdBusiness.definesType());
    item.setValue(mdEnumAttribute.definesAttribute(), "CO");
    item.setValue(BusinessInfo.KEY, "TEST-KEY");
    item.apply();

    MdBusinessDAO mdBusiness = TestFixtureFactory.createMdBusiness1();
    mdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness.apply();

    MdAttributeReferenceDAO mdAttributeEnum = TestFixtureFactory.addReferenceAttribute(mdBusiness, referenceMdBusiness);
    mdAttributeEnum.apply();

    BusinessDAO business = BusinessDAO.newInstance(mdBusiness.definesType());
    business.setValue(mdAttributeEnum.definesAttribute(), item.getOid());
    business.apply();

    referenceMdBusiness = MdBusinessDAO.get(referenceMdBusiness.getOid()).getBusinessDAO();
    referenceMdBusiness.setValue(MdBusinessInfo.HAS_DETERMINISTIC_IDS, MdAttributeBooleanInfo.TRUE);
    referenceMdBusiness.apply();

    // Update the oid of the item
    this.updateItems(referenceMdBusiness, item);

    // Ensure the oid of the enumerated item is different
    BusinessDAOIF testItem = this.getItem(referenceMdBusiness, item);
    Assert.assertFalse(testItem.getOid().equals(item.getOid()));

    BusinessDAOIF test = BusinessDAO.get(business.getOid());
    AttributeReferenceIF attributeIF = (AttributeReferenceIF) test.getAttributeIF(mdAttributeEnum.definesAttribute());

    // Ensure the referenced item of the enumeration attribute has been updated
    Assert.assertEquals(testItem.getOid(), attributeIF.getValue());
  }

  @Request
  @Test
  public void testReferenceDefaultValue()
  {
    MdBusinessDAO referenceMdBusiness = TestFixtureFactory.createMdBusiness2();
    referenceMdBusiness.setValue(MdBusinessInfo.HAS_DETERMINISTIC_IDS, MdAttributeBooleanInfo.FALSE);
    referenceMdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    referenceMdBusiness.apply();

    MdAttributeCharacterDAO mdEnumAttribute = TestFixtureFactory.addCharacterAttribute(referenceMdBusiness);
    mdEnumAttribute.apply();

    BusinessDAO colorado = BusinessDAO.newInstance(referenceMdBusiness.definesType());
    colorado.setValue(mdEnumAttribute.definesAttribute(), "CO");
    colorado.setValue(BusinessInfo.KEY, "CO");
    colorado.apply();

    MdBusinessDAO mdBusiness = TestFixtureFactory.createMdBusiness1();
    mdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness.apply();

    MdAttributeReferenceDAO mdAttributeEnum = TestFixtureFactory.addReferenceAttribute(mdBusiness, referenceMdBusiness);
    mdAttributeEnum.setValue(MdAttributeReferenceInfo.DEFAULT_VALUE, colorado.getOid());
    mdAttributeEnum.apply();

    referenceMdBusiness = MdBusinessDAO.get(referenceMdBusiness.getOid()).getBusinessDAO();
    referenceMdBusiness.setValue(MdBusinessInfo.HAS_DETERMINISTIC_IDS, MdAttributeBooleanInfo.TRUE);
    referenceMdBusiness.apply();

    // Update the ids of the item
    updateItems(referenceMdBusiness, colorado);

    BusinessDAOIF testItem = this.getItem(referenceMdBusiness, colorado);

    // Ensure the default value has been updated
    MdAttributeReferenceDAOIF testMdAttributeReference = MdAttributeReferenceDAO.get(mdAttributeEnum.getOid());

    Assert.assertEquals(testItem.getOid(), testMdAttributeReference.getValue(MdAttributeReferenceInfo.DEFAULT_VALUE));
  }

  @Request
  @Test
  public void testReferenceDimensionDefaultValue()
  {
    MdDimensionDAO mdDimension = TestFixtureFactory.createMdDimension();
    mdDimension.apply();

    try
    {

      MdBusinessDAO referenceMdBusiness = TestFixtureFactory.createMdBusiness2();
      referenceMdBusiness.setValue(MdBusinessInfo.HAS_DETERMINISTIC_IDS, MdAttributeBooleanInfo.FALSE);
      referenceMdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      referenceMdBusiness.apply();

      MdAttributeCharacterDAO mdEnumAttribute = TestFixtureFactory.addCharacterAttribute(referenceMdBusiness);
      mdEnumAttribute.apply();

      BusinessDAO colorado = BusinessDAO.newInstance(referenceMdBusiness.definesType());
      colorado.setValue(mdEnumAttribute.definesAttribute(), "CO");
      colorado.setValue(BusinessInfo.KEY, "CO");
      colorado.apply();

      MdBusinessDAO mdBusiness = TestFixtureFactory.createMdBusiness1();
      mdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      mdBusiness.apply();

      MdAttributeReferenceDAO mdAttributeEnum = TestFixtureFactory.addReferenceAttribute(mdBusiness, referenceMdBusiness);
      mdAttributeEnum.apply();

      MdAttributeDimensionDAO mdAttributeDimension = mdAttributeEnum.getMdAttributeDimension(mdDimension).getBusinessDAO();
      mdAttributeDimension.setValue(MdAttributeDimensionInfo.DEFAULT_VALUE, colorado.getOid());
      mdAttributeDimension.apply();

      referenceMdBusiness = MdBusinessDAO.get(referenceMdBusiness.getOid()).getBusinessDAO();
      referenceMdBusiness.setValue(MdBusinessInfo.HAS_DETERMINISTIC_IDS, MdAttributeBooleanInfo.TRUE);
      referenceMdBusiness.apply();

      // Update the ids of the item
      updateItems(referenceMdBusiness, colorado);

      BusinessDAOIF testItem = this.getItem(referenceMdBusiness, colorado);

      // Ensure the default value has been updated
      MdAttributeReferenceDAOIF testMdAttributeReference = MdAttributeReferenceDAO.get(mdAttributeEnum.getOid());
      MdAttributeDimensionDAOIF testMdAttributeDimension = testMdAttributeReference.getMdAttributeDimension(mdDimension);

      Assert.assertEquals(testItem.getOid(), testMdAttributeDimension.getValue(MdAttributeReferenceInfo.DEFAULT_VALUE));
    }
    finally
    {
      TestFixtureFactory.delete(mdDimension);
    }
  }

  @Request
  @Test
  public void testMerge()
  {
    mergeInTransaction();
  }

  @Transaction
  private void mergeInTransaction()
  {
    MdBusinessDAO referenceMdBusiness = TestFixtureFactory.createMdBusiness2();
    referenceMdBusiness.setValue(MdBusinessInfo.HAS_DETERMINISTIC_IDS, MdAttributeBooleanInfo.FALSE);
    referenceMdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
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
    mdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness.apply();

    MdAttributeReferenceDAO mdAttributeReference = TestFixtureFactory.addReferenceAttribute(mdBusiness, referenceMdBusiness);
    mdAttributeReference.apply();

    BusinessDAO business1 = BusinessDAO.newInstance(mdBusiness.definesType());
    business1.setValue(mdAttributeReference.definesAttribute(), item1.getOid());
    business1.apply();

    BusinessDAO business2 = BusinessDAO.newInstance(mdBusiness.definesType());
    business2.setValue(mdAttributeReference.definesAttribute(), item2.getOid());
    business2.apply();

    // Convert item1 references to item2
    BusinessDAOFactory.floatObjectIdReferences(item1, item1.getOid(), item2.getOid());

    // Delete item1
    item1.delete();

    // Ensure the business1 reference was updated to be item2
    BusinessDAOIF test = BusinessDAO.get(business1.getOid());

    Assert.assertEquals(item2.getOid(), test.getValue(mdAttributeReference.definesAttribute()));
  }

  @Request
  @Test
  public void testMergeIgnoreDatabaseExceptions()
  {
    mergeInTransactionIgnoreDatabaseExceptions();
  }

  @Transaction
  private void mergeInTransactionIgnoreDatabaseExceptions()
  {
    MdBusinessDAO mdBusiness = TestFixtureFactory.createMdBusiness1();
    mdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness.apply();

    MdTreeDAO mdTree = TestFixtureFactory.createMdTree(mdBusiness, mdBusiness);
    mdTree.setValue(MdTreeInfo.PARENT_CARDINALITY, "*");
    mdTree.setValue(MdTreeInfo.CHILD_CARDINALITY, "*");
    mdTree.setValue(MdTreeInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdTree.apply();

    BusinessDAO parent1 = BusinessDAO.newInstance(mdBusiness.definesType());
    parent1.apply();

    BusinessDAO parent2 = BusinessDAO.newInstance(mdBusiness.definesType());
    parent2.apply();

    BusinessDAO business1 = BusinessDAO.newInstance(mdBusiness.definesType());
    business1.apply();

    BusinessDAO business2 = BusinessDAO.newInstance(mdBusiness.definesType());
    business2.apply();

    business1.addParent(parent1.getOid(), mdTree.definesType()).apply();
    business1.addParent(parent2.getOid(), mdTree.definesType()).apply();
    business2.addParent(parent2.getOid(), mdTree.definesType()).apply();

    // Convert business 1 references to business 2
    BusinessDAOFactory.floatObjectIdReferences(business1, business1.getOid(), business2.getOid(), true);

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
      // changed in order to force the predictive oid algorithm to run
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
