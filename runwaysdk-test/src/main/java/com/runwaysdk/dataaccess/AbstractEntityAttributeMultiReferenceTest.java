/**
*
*/
package com.runwaysdk.dataaccess;

import java.util.Arrays;
import java.util.Set;

import junit.framework.TestCase;

import org.junit.Assert;

import com.runwaysdk.dataaccess.attributes.entity.AttributeMultiReference;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.metadata.MdAttributeMultiReferenceDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdTermDAO;

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
public abstract class AbstractEntityAttributeMultiReferenceTest extends TestCase
{
  public abstract MdAttributeMultiReferenceDAO getMdAttribute();

  public abstract MdBusinessDAO getMdBusiness();

  public abstract MdTermDAO getMdTerm();

  public abstract BusinessDAO getDefaultValue();

  public abstract AttributeMultiReferenceIF getAttribute(BusinessDAO business);

  public void testGetAttribute()
  {
    BusinessDAO business = BusinessDAO.newInstance(this.getMdBusiness().definesType());
    AttributeIF attribute = business.getAttributeIF(this.getMdAttribute().definesAttribute());

    Assert.assertNotNull(attribute);
    Assert.assertTrue(attribute instanceof AttributeMultiReferenceIF);
  }

  public void testDefaultValueCached()
  {
    BusinessDAO business = BusinessDAO.newInstance(this.getMdBusiness().definesType());
    AttributeMultiReferenceIF attribute = getAttribute(business);

    Set<String> result = attribute.getCachedItemIdSet();

    Assert.assertEquals(1, result.size());
    Assert.assertEquals(this.getDefaultValue().getId(), result.iterator().next());
  }

  public void testDefaultValueRefreshed()
  {
    BusinessDAO business = BusinessDAO.newInstance(this.getMdBusiness().definesType());
    AttributeMultiReferenceIF attribute = getAttribute(business);

    Set<String> result = attribute.getItemIdList();

    Assert.assertEquals(0, result.size());
  }

  public void testApply()
  {
    BusinessDAO business = BusinessDAO.newInstance(this.getMdBusiness().definesType());
    business.apply();

    AttributeMultiReferenceIF attribute = getAttribute(business);
    Set<String> result = attribute.getItemIdList();

    Assert.assertEquals(1, result.size());
    Assert.assertEquals(this.getDefaultValue().getId(), result.iterator().next());
  }

  public void testAddMultiple()
  {
    BusinessDAO value1 = BusinessDAO.newInstance(this.getMdTerm().definesType());
    value1.apply();

    try
    {
      BusinessDAO value2 = BusinessDAO.newInstance(this.getMdTerm().definesType());
      value2.apply();

      try
      {
        BusinessDAO business = BusinessDAO.newInstance(this.getMdBusiness().definesType());
        AttributeMultiReference attribute = (AttributeMultiReference) business.getAttribute(this.getMdAttribute().definesAttribute());
        attribute.addItem(value1.getId());
        attribute.addItem(value2.getId());
        business.apply();

        AttributeMultiReferenceIF attributeIF = getAttribute(business);
        Set<String> result = attributeIF.getItemIdList();

        Assert.assertEquals(3, result.size());
        Assert.assertTrue(result.contains(this.getDefaultValue().getId()));
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
    BusinessDAO business = BusinessDAO.newInstance(this.getMdBusiness().definesType());
    AttributeMultiReference attribute = (AttributeMultiReference) business.getAttribute(this.getMdAttribute().definesAttribute());
    attribute.addItem(this.getDefaultValue().getId());
    attribute.addItem(this.getDefaultValue().getId());
    business.apply();

    AttributeMultiReferenceIF attributeIF = getAttribute(business);
    Set<String> result = attributeIF.getItemIdList();

    Assert.assertEquals(1, result.size());
    Assert.assertEquals(this.getDefaultValue().getId(), result.iterator().next());
  }

  public void testGet()
  {
    BusinessDAO business = BusinessDAO.newInstance(this.getMdBusiness().definesType());
    business.apply();

    BusinessDAOIF test = BusinessDAO.get(business.getId());

    AttributeMultiReferenceIF attribute = (AttributeMultiReferenceIF) test.getAttributeIF(this.getMdAttribute().definesAttribute());
    Set<String> result = attribute.getItemIdList();

    Assert.assertEquals(1, result.size());
    Assert.assertEquals(this.getDefaultValue().getId(), result.iterator().next());
  }

  public void testReplace()
  {
    BusinessDAO value = BusinessDAO.newInstance(this.getMdTerm().definesType());
    value.apply();

    try
    {
      BusinessDAO business = BusinessDAO.newInstance(this.getMdBusiness().definesType());
      business.apply();

      AttributeMultiReference attribute = (AttributeMultiReference) business.getAttribute(this.getMdAttribute().definesAttribute());
      attribute.replaceItems(Arrays.asList(value.getId()));
      business.apply();

      AttributeMultiReferenceIF attributeIF = getAttribute(business);
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
    BusinessDAO business = BusinessDAO.newInstance(this.getMdBusiness().definesType());
    business.apply();

    AttributeMultiReference attribute = (AttributeMultiReference) business.getAttribute(this.getMdAttribute().definesAttribute());
    attribute.clearItems();
    business.apply();

    AttributeMultiReferenceIF attributeIF = getAttribute(business);
    Set<String> result = attributeIF.getItemIdList();

    Assert.assertEquals(0, result.size());
  }

  public void testRemove()
  {
    BusinessDAO business = BusinessDAO.newInstance(this.getMdBusiness().definesType());
    business.apply();

    AttributeMultiReference attribute = (AttributeMultiReference) business.getAttribute(this.getMdAttribute().definesAttribute());
    attribute.removeItem(this.getDefaultValue().getId());
    business.apply();

    AttributeMultiReferenceIF attributeIF = getAttribute(business);
    Set<String> result = attributeIF.getItemIdList();

    Assert.assertEquals(0, result.size());
  }

  public void testRemoveUnsetItem()
  {
    BusinessDAO value1 = BusinessDAO.newInstance(this.getMdTerm().definesType());
    value1.apply();

    try
    {
      BusinessDAO business = BusinessDAO.newInstance(this.getMdBusiness().definesType());
      business.apply();

      AttributeMultiReference attribute = (AttributeMultiReference) business.getAttribute(this.getMdAttribute().definesAttribute());
      attribute.removeItem(value1.getId());
      business.apply();

      AttributeMultiReferenceIF attributeIF = getAttribute(business);
      Set<String> result = attributeIF.getItemIdList();

      Assert.assertEquals(1, result.size());
      Assert.assertEquals(this.getDefaultValue().getId(), result.iterator().next());
    }
    finally
    {
      TestFixtureFactory.delete(value1);
    }
  }

  public void testDereference()
  {
    BusinessDAO value1 = BusinessDAO.newInstance(this.getMdTerm().definesType());
    value1.apply();

    try
    {
      BusinessDAO business = BusinessDAO.newInstance(this.getMdBusiness().definesType());
      AttributeMultiReference attribute = (AttributeMultiReference) business.getAttribute(this.getMdAttribute().definesAttribute());
      attribute.addItem(value1.getId());
      business.apply();

      AttributeMultiReferenceIF attributeIF = getAttribute(business);
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
    BusinessDAO business = BusinessDAO.newInstance(this.getMdBusiness().definesType());
    AttributeMultiReference attribute = (AttributeMultiReference) business.getAttribute(this.getMdAttribute().definesAttribute());
    attribute.clearItems();
    business.apply();

    AttributeMultiReferenceIF attributeIF = getAttribute(business);
    BusinessDAOIF[] results = attributeIF.dereference();

    Assert.assertNotNull(results);
    Assert.assertEquals(0, results.length);
  }

  public void testAddItem()
  {
    BusinessDAO value1 = BusinessDAO.newInstance(this.getMdTerm().definesType());
    value1.apply();

    try
    {
      BusinessDAO value2 = BusinessDAO.newInstance(this.getMdTerm().definesType());
      value2.apply();

      try
      {
        BusinessDAO business = BusinessDAO.newInstance(this.getMdBusiness().definesType());
        business.addItem(this.getMdAttribute().definesAttribute(), value1.getId());
        business.addItem(this.getMdAttribute().definesAttribute(), value2.getId());
        business.apply();

        AttributeMultiReferenceIF attributeIF = getAttribute(business);
        Set<String> result = attributeIF.getItemIdList();

        Assert.assertEquals(3, result.size());
        Assert.assertTrue(result.contains(this.getDefaultValue().getId()));
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
    BusinessDAO value = BusinessDAO.newInstance(this.getMdTerm().definesType());
    value.apply();

    try
    {
      BusinessDAO business = BusinessDAO.newInstance(this.getMdBusiness().definesType());
      business.replaceItems(this.getMdAttribute().definesAttribute(), Arrays.asList(value.getId()));
      business.apply();

      AttributeMultiReferenceIF attributeIF = getAttribute(business);
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
    BusinessDAO business = BusinessDAO.newInstance(this.getMdBusiness().definesType());
    business.apply();

    business.removeItem(this.getMdAttribute().definesAttribute(), this.getDefaultValue().getId());
    business.apply();

    AttributeMultiReferenceIF attributeIF = getAttribute(business);
    Set<String> result = attributeIF.getItemIdList();

    Assert.assertEquals(0, result.size());
  }

  public void testClearItems()
  {
    BusinessDAO business = BusinessDAO.newInstance(this.getMdBusiness().definesType());
    business.clearItems(this.getMdAttribute().definesAttribute());
    business.apply();

    AttributeMultiReferenceIF attributeIF = getAttribute(business);
    Set<String> result = attributeIF.getItemIdList();

    Assert.assertEquals(0, result.size());
  }

}
