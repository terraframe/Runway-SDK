/**
*
*/
package com.runwaysdk.dataaccess;

import java.util.Arrays;
import java.util.Set;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.junit.Assert;

import com.runwaysdk.business.ontology.MdTermDAO;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdAttributeMultiReferenceInfo;
import com.runwaysdk.dataaccess.attributes.entity.AttributeMultiReference;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.metadata.MdAttributeMultiReferenceDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;

/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved.
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
 ******************************************************************************/
public class EntityAttributeMultiReferenceTest extends TestCase
{
  private static MdTermDAO                    mdTerm;

  private static BusinessDAO                  defaultValue;

  private static MdBusinessDAO                mdBusiness;

  private static MdAttributeMultiReferenceDAO mdAttributeMultiReference;

  public static Test suite()
  {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(EntityAttributeMultiReferenceTest.class);

    TestSetup wrapper = new TestSetup(suite)
    {
      protected void setUp()
      {
        classSetUp();
      }

      protected void tearDown()
      {
        classTearDown();
      }
    };

    return wrapper;
  }

  /**
  * 
  */
  public static void classTearDown()
  {
    TestFixtureFactory.delete(mdBusiness);
    TestFixtureFactory.delete(mdTerm);
  }

  /**
  * 
  */
  public static void classSetUp()
  {
    mdTerm = TestFixtureFactory.createMdTerm();
    mdTerm.apply();

    defaultValue = BusinessDAO.newInstance(mdTerm.definesType());
    defaultValue.apply();

    mdBusiness = TestFixtureFactory.createMdBusiness1();
    mdBusiness.apply();

    mdAttributeMultiReference = MdAttributeMultiReferenceDAO.newInstance();
    mdAttributeMultiReference.setValue(MdAttributeMultiReferenceInfo.NAME, "testMultiReference");
    mdAttributeMultiReference.setStructValue(MdAttributeMultiReferenceInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Term Test");
    mdAttributeMultiReference.setValue(MdAttributeMultiReferenceInfo.REF_MD_ENTITY, mdTerm.getId());
    mdAttributeMultiReference.setValue(MdAttributeMultiReferenceInfo.DEFINING_MD_CLASS, mdBusiness.getId());
    mdAttributeMultiReference.setValue(MdAttributeMultiReferenceInfo.DEFAULT_VALUE, defaultValue.getId());
    mdAttributeMultiReference.apply();
  }

  public void testGetAttribute()
  {
    BusinessDAO business = BusinessDAO.newInstance(mdBusiness.definesType());
    AttributeIF attribute = business.getAttributeIF(mdAttributeMultiReference.definesAttribute());

    Assert.assertNotNull(attribute);
    Assert.assertTrue(attribute instanceof AttributeMultiReferenceIF);
  }

  public void testDefaultValueCached()
  {
    BusinessDAO business = BusinessDAO.newInstance(mdBusiness.definesType());
    AttributeMultiReferenceIF attribute = (AttributeMultiReferenceIF) business.getAttributeIF(mdAttributeMultiReference.definesAttribute());

    Set<String> result = attribute.getCachedItemIdSet();

    Assert.assertEquals(1, result.size());
    Assert.assertEquals(defaultValue.getId(), result.iterator().next());
  }

  public void testDefaultValueRefreshed()
  {
    BusinessDAO business = BusinessDAO.newInstance(mdBusiness.definesType());
    AttributeMultiReferenceIF attribute = (AttributeMultiReferenceIF) business.getAttributeIF(mdAttributeMultiReference.definesAttribute());

    Set<String> result = attribute.getItemIdList();

    Assert.assertEquals(0, result.size());
  }

  public void testApply()
  {
    BusinessDAO business = BusinessDAO.newInstance(mdBusiness.definesType());
    business.apply();

    AttributeMultiReferenceIF attribute = (AttributeMultiReferenceIF) business.getAttributeIF(mdAttributeMultiReference.definesAttribute());
    Set<String> result = attribute.getItemIdList();

    Assert.assertEquals(1, result.size());
    Assert.assertEquals(defaultValue.getId(), result.iterator().next());
  }

  public void testAddMultiple()
  {
    BusinessDAO value1 = BusinessDAO.newInstance(mdTerm.definesType());
    value1.apply();

    try
    {
      BusinessDAO value2 = BusinessDAO.newInstance(mdTerm.definesType());
      value2.apply();

      try
      {
        BusinessDAO business = BusinessDAO.newInstance(mdBusiness.definesType());
        AttributeMultiReference attribute = (AttributeMultiReference) business.getAttribute(mdAttributeMultiReference.definesAttribute());
        attribute.addItem(value1.getId());
        attribute.addItem(value2.getId());
        business.apply();

        AttributeMultiReferenceIF attributeIF = (AttributeMultiReferenceIF) business.getAttributeIF(mdAttributeMultiReference.definesAttribute());
        Set<String> result = attributeIF.getItemIdList();

        Assert.assertEquals(3, result.size());
        Assert.assertTrue(result.contains(defaultValue.getId()));
        Assert.assertTrue(result.contains(value1.getId()));
        Assert.assertTrue(result.contains(value2.getId()));
      }
      finally
      {
        TestFixtureFactory.delete(value2);
      }
    }
    finally
    {
      TestFixtureFactory.delete(value1);
    }
  }

  public void testAddDuplicates()
  {
    BusinessDAO business = BusinessDAO.newInstance(mdBusiness.definesType());
    AttributeMultiReference attribute = (AttributeMultiReference) business.getAttribute(mdAttributeMultiReference.definesAttribute());
    attribute.addItem(defaultValue.getId());
    attribute.addItem(defaultValue.getId());
    business.apply();

    AttributeMultiReferenceIF attributeIF = (AttributeMultiReferenceIF) business.getAttributeIF(mdAttributeMultiReference.definesAttribute());
    Set<String> result = attributeIF.getItemIdList();

    Assert.assertEquals(1, result.size());
    Assert.assertEquals(defaultValue.getId(), result.iterator().next());
  }

  public void testGet()
  {
    BusinessDAO business = BusinessDAO.newInstance(mdBusiness.definesType());
    business.apply();

    BusinessDAOIF test = BusinessDAO.get(business.getId());

    AttributeMultiReferenceIF attribute = (AttributeMultiReferenceIF) test.getAttributeIF(mdAttributeMultiReference.definesAttribute());
    Set<String> result = attribute.getItemIdList();

    Assert.assertEquals(1, result.size());
    Assert.assertEquals(defaultValue.getId(), result.iterator().next());
  }

  public void testReplace()
  {
    BusinessDAO value = BusinessDAO.newInstance(mdTerm.definesType());
    value.apply();

    try
    {
      BusinessDAO business = BusinessDAO.newInstance(mdBusiness.definesType());
      business.apply();

      AttributeMultiReference attribute = (AttributeMultiReference) business.getAttribute(mdAttributeMultiReference.definesAttribute());
      attribute.replaceItems(Arrays.asList(value.getId()));
      business.apply();

      AttributeMultiReferenceIF attributeIF = (AttributeMultiReferenceIF) business.getAttributeIF(mdAttributeMultiReference.definesAttribute());
      Set<String> result = attributeIF.getItemIdList();

      Assert.assertEquals(1, result.size());
      Assert.assertTrue(result.contains(value.getId()));
    }
    finally
    {
      TestFixtureFactory.delete(value);
    }
  }

  public void testClear()
  {
    BusinessDAO business = BusinessDAO.newInstance(mdBusiness.definesType());
    business.apply();

    AttributeMultiReference attribute = (AttributeMultiReference) business.getAttribute(mdAttributeMultiReference.definesAttribute());
    attribute.clearItems();
    business.apply();

    AttributeMultiReferenceIF attributeIF = (AttributeMultiReferenceIF) business.getAttributeIF(mdAttributeMultiReference.definesAttribute());
    Set<String> result = attributeIF.getItemIdList();

    Assert.assertEquals(0, result.size());
  }

  public void testRemove()
  {
    BusinessDAO business = BusinessDAO.newInstance(mdBusiness.definesType());
    business.apply();

    AttributeMultiReference attribute = (AttributeMultiReference) business.getAttribute(mdAttributeMultiReference.definesAttribute());
    attribute.removeItem(defaultValue.getId());
    business.apply();

    AttributeMultiReferenceIF attributeIF = (AttributeMultiReferenceIF) business.getAttributeIF(mdAttributeMultiReference.definesAttribute());
    Set<String> result = attributeIF.getItemIdList();

    Assert.assertEquals(0, result.size());
  }

  public void testRemoveUnsetItem()
  {
    BusinessDAO value1 = BusinessDAO.newInstance(mdTerm.definesType());
    value1.apply();

    try
    {
      BusinessDAO business = BusinessDAO.newInstance(mdBusiness.definesType());
      business.apply();

      AttributeMultiReference attribute = (AttributeMultiReference) business.getAttribute(mdAttributeMultiReference.definesAttribute());
      attribute.removeItem(value1.getId());
      business.apply();

      AttributeMultiReferenceIF attributeIF = (AttributeMultiReferenceIF) business.getAttributeIF(mdAttributeMultiReference.definesAttribute());
      Set<String> result = attributeIF.getItemIdList();

      Assert.assertEquals(1, result.size());
      Assert.assertEquals(defaultValue.getId(), result.iterator().next());
    }
    finally
    {
      TestFixtureFactory.delete(value1);
    }
  }

  public void testDereference()
  {
    BusinessDAO value1 = BusinessDAO.newInstance(mdTerm.definesType());
    value1.apply();

    try
    {
      BusinessDAO business = BusinessDAO.newInstance(mdBusiness.definesType());
      AttributeMultiReference attribute = (AttributeMultiReference) business.getAttribute(mdAttributeMultiReference.definesAttribute());
      attribute.addItem(value1.getId());
      business.apply();

      AttributeMultiReferenceIF attributeIF = (AttributeMultiReferenceIF) business.getAttributeIF(mdAttributeMultiReference.definesAttribute());
      BusinessDAOIF[] results = attributeIF.dereference();

      Assert.assertNotNull(results);
      Assert.assertEquals(2, results.length);
    }
    finally
    {
      TestFixtureFactory.delete(value1);
    }
  }

  public void testEmptyDereference()
  {
    BusinessDAO business = BusinessDAO.newInstance(mdBusiness.definesType());
    AttributeMultiReference attribute = (AttributeMultiReference) business.getAttribute(mdAttributeMultiReference.definesAttribute());
    attribute.clearItems();
    business.apply();

    AttributeMultiReferenceIF attributeIF = (AttributeMultiReferenceIF) business.getAttributeIF(mdAttributeMultiReference.definesAttribute());
    BusinessDAOIF[] results = attributeIF.dereference();

    Assert.assertNotNull(results);
    Assert.assertEquals(0, results.length);
  }

  public void testAddItem()
  {
    BusinessDAO value1 = BusinessDAO.newInstance(mdTerm.definesType());
    value1.apply();

    try
    {
      BusinessDAO value2 = BusinessDAO.newInstance(mdTerm.definesType());
      value2.apply();

      try
      {
        BusinessDAO business = BusinessDAO.newInstance(mdBusiness.definesType());
        business.addItem(mdAttributeMultiReference.definesAttribute(), value1.getId());
        business.addItem(mdAttributeMultiReference.definesAttribute(), value2.getId());
        business.apply();

        AttributeMultiReferenceIF attributeIF = (AttributeMultiReferenceIF) business.getAttributeIF(mdAttributeMultiReference.definesAttribute());
        Set<String> result = attributeIF.getItemIdList();

        Assert.assertEquals(3, result.size());
        Assert.assertTrue(result.contains(defaultValue.getId()));
        Assert.assertTrue(result.contains(value1.getId()));
        Assert.assertTrue(result.contains(value2.getId()));
      }
      finally
      {
        TestFixtureFactory.delete(value2);
      }
    }
    finally
    {
      TestFixtureFactory.delete(value1);
    }

  }

  public void testReplaceItems()
  {
    BusinessDAO value = BusinessDAO.newInstance(mdTerm.definesType());
    value.apply();

    try
    {
      BusinessDAO business = BusinessDAO.newInstance(mdBusiness.definesType());
      business.replaceItems(mdAttributeMultiReference.definesAttribute(), Arrays.asList(value.getId()));
      business.apply();

      AttributeMultiReferenceIF attributeIF = (AttributeMultiReferenceIF) business.getAttributeIF(mdAttributeMultiReference.definesAttribute());
      Set<String> result = attributeIF.getItemIdList();

      Assert.assertEquals(1, result.size());
      Assert.assertTrue(result.contains(value.getId()));
    }
    finally
    {
      TestFixtureFactory.delete(value);
    }
  }

  public void testRemoveItem()
  {
    BusinessDAO business = BusinessDAO.newInstance(mdBusiness.definesType());
    business.apply();

    business.removeItem(mdAttributeMultiReference.definesAttribute(), defaultValue.getId());
    business.apply();

    AttributeMultiReferenceIF attributeIF = (AttributeMultiReferenceIF) business.getAttributeIF(mdAttributeMultiReference.definesAttribute());
    Set<String> result = attributeIF.getItemIdList();

    Assert.assertEquals(0, result.size());
  }

  public void testClearItems()
  {
    BusinessDAO business = BusinessDAO.newInstance(mdBusiness.definesType());
    business.clearItems(mdAttributeMultiReference.definesAttribute());
    business.apply();

    AttributeMultiReferenceIF attributeIF = (AttributeMultiReferenceIF) business.getAttributeIF(mdAttributeMultiReference.definesAttribute());
    Set<String> result = attributeIF.getItemIdList();

    Assert.assertEquals(0, result.size());
  }

}
