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
import com.runwaysdk.dataaccess.attributes.tranzient.AttributeMultiReference;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.metadata.MdAttributeMultiReferenceDAO;
import com.runwaysdk.dataaccess.metadata.MdTransientDAO;

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
public class TransientAttributeMultiReferenceTest extends TestCase
{
  private static MdTermDAO                    mdTerm;

  private static BusinessDAO                  defaultValue;

  private static MdTransientDAO               mdView;

  private static MdAttributeMultiReferenceDAO mdAttributeMultiReference;

  public static Test suite()
  {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(TransientAttributeMultiReferenceTest.class);

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
    TestFixtureFactory.delete(mdView);
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

    mdView = TestFixtureFactory.createMdView1();
    mdView.apply();

    mdAttributeMultiReference = MdAttributeMultiReferenceDAO.newInstance();
    mdAttributeMultiReference.setValue(MdAttributeMultiReferenceInfo.NAME, "testMultiReference");
    mdAttributeMultiReference.setStructValue(MdAttributeMultiReferenceInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Term Test");
    mdAttributeMultiReference.setValue(MdAttributeMultiReferenceInfo.REF_MD_ENTITY, mdTerm.getId());
    mdAttributeMultiReference.setValue(MdAttributeMultiReferenceInfo.DEFINING_MD_CLASS, mdView.getId());
    mdAttributeMultiReference.setValue(MdAttributeMultiReferenceInfo.DEFAULT_VALUE, defaultValue.getId());
    mdAttributeMultiReference.apply();
  }

  public void testGetAttribute()
  {
    TransientDAO transientDAO = TransientDAO.newInstance(mdView.definesType());
    AttributeIF attribute = transientDAO.getAttributeIF(mdAttributeMultiReference.definesAttribute());

    Assert.assertNotNull(attribute);
    Assert.assertTrue(attribute instanceof AttributeMultiReferenceIF);
  }

  public void testDefaultValueCached()
  {
    TransientDAO transientDAO = TransientDAO.newInstance(mdView.definesType());
    AttributeMultiReferenceIF attribute = (AttributeMultiReferenceIF) transientDAO.getAttributeIF(mdAttributeMultiReference.definesAttribute());

    Set<String> result = attribute.getCachedItemIdSet();

    Assert.assertEquals(1, result.size());
    Assert.assertEquals(defaultValue.getId(), result.iterator().next());
  }

  public void testApply()
  {
    TransientDAO transientDAO = TransientDAO.newInstance(mdView.definesType());
    transientDAO.apply();

    AttributeMultiReferenceIF attribute = (AttributeMultiReferenceIF) transientDAO.getAttributeIF(mdAttributeMultiReference.definesAttribute());
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
        TransientDAO transientDAO = TransientDAO.newInstance(mdView.definesType());
        AttributeMultiReference attribute = (AttributeMultiReference) transientDAO.getAttribute(mdAttributeMultiReference.definesAttribute());
        attribute.addItem(value1.getId());
        attribute.addItem(value2.getId());
        transientDAO.apply();

        AttributeMultiReferenceIF attributeIF = (AttributeMultiReferenceIF) transientDAO.getAttributeIF(mdAttributeMultiReference.definesAttribute());
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
    TransientDAO transientDAO = TransientDAO.newInstance(mdView.definesType());
    AttributeMultiReference attribute = (AttributeMultiReference) transientDAO.getAttribute(mdAttributeMultiReference.definesAttribute());
    attribute.addItem(defaultValue.getId());
    attribute.addItem(defaultValue.getId());
    transientDAO.apply();

    AttributeMultiReferenceIF attributeIF = (AttributeMultiReferenceIF) transientDAO.getAttributeIF(mdAttributeMultiReference.definesAttribute());
    Set<String> result = attributeIF.getItemIdList();

    Assert.assertEquals(1, result.size());
    Assert.assertEquals(defaultValue.getId(), result.iterator().next());
  }

  public void testReplace()
  {
    BusinessDAO value = BusinessDAO.newInstance(mdTerm.definesType());
    value.apply();

    try
    {
      TransientDAO transientDAO = TransientDAO.newInstance(mdView.definesType());
      transientDAO.apply();

      AttributeMultiReference attribute = (AttributeMultiReference) transientDAO.getAttribute(mdAttributeMultiReference.definesAttribute());
      attribute.replaceItems(Arrays.asList(value.getId()));
      transientDAO.apply();

      AttributeMultiReferenceIF attributeIF = (AttributeMultiReferenceIF) transientDAO.getAttributeIF(mdAttributeMultiReference.definesAttribute());
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
    TransientDAO transientDAO = TransientDAO.newInstance(mdView.definesType());
    transientDAO.apply();

    AttributeMultiReference attribute = (AttributeMultiReference) transientDAO.getAttribute(mdAttributeMultiReference.definesAttribute());
    attribute.clearItems();
    transientDAO.apply();

    AttributeMultiReferenceIF attributeIF = (AttributeMultiReferenceIF) transientDAO.getAttributeIF(mdAttributeMultiReference.definesAttribute());
    Set<String> result = attributeIF.getItemIdList();

    Assert.assertEquals(0, result.size());
  }

  public void testRemove()
  {
    TransientDAO transientDAO = TransientDAO.newInstance(mdView.definesType());
    transientDAO.apply();

    AttributeMultiReference attribute = (AttributeMultiReference) transientDAO.getAttribute(mdAttributeMultiReference.definesAttribute());
    attribute.removeItem(defaultValue.getId());
    transientDAO.apply();

    AttributeMultiReferenceIF attributeIF = (AttributeMultiReferenceIF) transientDAO.getAttributeIF(mdAttributeMultiReference.definesAttribute());
    Set<String> result = attributeIF.getItemIdList();

    Assert.assertEquals(0, result.size());
  }

  public void testRemoveUnsetItem()
  {
    BusinessDAO value1 = BusinessDAO.newInstance(mdTerm.definesType());
    value1.apply();

    try
    {
      TransientDAO transientDAO = TransientDAO.newInstance(mdView.definesType());
      transientDAO.apply();

      AttributeMultiReference attribute = (AttributeMultiReference) transientDAO.getAttribute(mdAttributeMultiReference.definesAttribute());
      attribute.removeItem(value1.getId());
      transientDAO.apply();

      AttributeMultiReferenceIF attributeIF = (AttributeMultiReferenceIF) transientDAO.getAttributeIF(mdAttributeMultiReference.definesAttribute());
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
      TransientDAO transientDAO = TransientDAO.newInstance(mdView.definesType());
      AttributeMultiReference attribute = (AttributeMultiReference) transientDAO.getAttribute(mdAttributeMultiReference.definesAttribute());
      attribute.addItem(value1.getId());
      transientDAO.apply();

      AttributeMultiReferenceIF attributeIF = (AttributeMultiReferenceIF) transientDAO.getAttributeIF(mdAttributeMultiReference.definesAttribute());
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
    TransientDAO transientDAO = TransientDAO.newInstance(mdView.definesType());
    AttributeMultiReference attribute = (AttributeMultiReference) transientDAO.getAttribute(mdAttributeMultiReference.definesAttribute());
    attribute.clearItems();
    transientDAO.apply();

    AttributeMultiReferenceIF attributeIF = (AttributeMultiReferenceIF) transientDAO.getAttributeIF(mdAttributeMultiReference.definesAttribute());
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
        TransientDAO transientDAO = TransientDAO.newInstance(mdView.definesType());
        transientDAO.addItem(mdAttributeMultiReference.definesAttribute(), value1.getId());
        transientDAO.addItem(mdAttributeMultiReference.definesAttribute(), value2.getId());
        transientDAO.apply();

        AttributeMultiReferenceIF attributeIF = (AttributeMultiReferenceIF) transientDAO.getAttributeIF(mdAttributeMultiReference.definesAttribute());
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
      TransientDAO transientDAO = TransientDAO.newInstance(mdView.definesType());
      transientDAO.replaceItems(mdAttributeMultiReference.definesAttribute(), Arrays.asList(value.getId()));
      transientDAO.apply();

      AttributeMultiReferenceIF attributeIF = (AttributeMultiReferenceIF) transientDAO.getAttributeIF(mdAttributeMultiReference.definesAttribute());
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
    TransientDAO transientDAO = TransientDAO.newInstance(mdView.definesType());
    transientDAO.apply();

    transientDAO.removeItem(mdAttributeMultiReference.definesAttribute(), defaultValue.getId());
    transientDAO.apply();

    AttributeMultiReferenceIF attributeIF = (AttributeMultiReferenceIF) transientDAO.getAttributeIF(mdAttributeMultiReference.definesAttribute());
    Set<String> result = attributeIF.getItemIdList();

    Assert.assertEquals(0, result.size());
  }

  public void testClearItems()
  {
    TransientDAO transientDAO = TransientDAO.newInstance(mdView.definesType());
    transientDAO.clearItems(mdAttributeMultiReference.definesAttribute());
    transientDAO.apply();

    AttributeMultiReferenceIF attributeIF = (AttributeMultiReferenceIF) transientDAO.getAttributeIF(mdAttributeMultiReference.definesAttribute());
    Set<String> result = attributeIF.getItemIdList();

    Assert.assertEquals(0, result.size());
  }

}