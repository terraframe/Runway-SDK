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

import java.util.Arrays;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.TermInfo;
import com.runwaysdk.dataaccess.attributes.tranzient.AttributeMultiReference;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.metadata.MdAttributeMultiReferenceDAO;
import com.runwaysdk.dataaccess.metadata.MdTermDAO;
import com.runwaysdk.dataaccess.metadata.MdTransientDAO;
import com.runwaysdk.session.Request;

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
public abstract class AbstractTransientAttributeMultiReferenceTest
{
  public abstract MdAttributeMultiReferenceDAO getMdAttribute();

  public abstract MdTransientDAO getMdTransient();

  public abstract MdTermDAO getMdTerm();

  public abstract BusinessDAO getDefaultValue();

  public abstract AttributeMultiReferenceIF getAttribute(BusinessDAO business);

  @Request
  @Test
  public void testGetAttribute()
  {
    TransientDAO transientDAO = TransientDAO.newInstance(this.getMdTransient().definesType());
    AttributeIF attribute = transientDAO.getAttributeIF(this.getMdAttribute().definesAttribute());

    Assert.assertNotNull(attribute);
    Assert.assertTrue(attribute instanceof AttributeMultiReferenceIF);
  }

  @Request
  @Test
  public void testDefaultValueCached()
  {
    TransientDAO transientDAO = TransientDAO.newInstance(this.getMdTransient().definesType());
    AttributeMultiReferenceIF attribute = (AttributeMultiReferenceIF) transientDAO.getAttributeIF(this.getMdAttribute().definesAttribute());

    Set<String> result = attribute.getCachedItemIdSet();

    Assert.assertEquals(1, result.size());
    Assert.assertEquals(this.getDefaultValue().getOid(), result.iterator().next());
  }

  @Request
  @Test
  public void testApply()
  {
    TransientDAO transientDAO = TransientDAO.newInstance(this.getMdTransient().definesType());
    transientDAO.apply();

    AttributeMultiReferenceIF attribute = (AttributeMultiReferenceIF) transientDAO.getAttributeIF(this.getMdAttribute().definesAttribute());
    Set<String> result = attribute.getItemIdList();

    Assert.assertEquals(1, result.size());
    Assert.assertEquals(this.getDefaultValue().getOid(), result.iterator().next());
  }

  @Request
  @Test
  public void testAddMultiple()
  {
    BusinessDAO value1 = BusinessDAO.newInstance(this.getMdTerm().definesType());
    value1.setStructValue(TermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Term 1");
    value1.apply();

    try
    {
      BusinessDAO value2 = BusinessDAO.newInstance(this.getMdTerm().definesType());
      value2.setStructValue(TermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Term 2");
      value2.apply();

      try
      {
        TransientDAO transientDAO = TransientDAO.newInstance(this.getMdTransient().definesType());
        AttributeMultiReference attribute = (AttributeMultiReference) transientDAO.getAttribute(this.getMdAttribute().definesAttribute());
        attribute.addItem(value1.getOid());
        attribute.addItem(value2.getOid());
        transientDAO.apply();

        AttributeMultiReferenceIF attributeIF = (AttributeMultiReferenceIF) transientDAO.getAttributeIF(this.getMdAttribute().definesAttribute());
        Set<String> result = attributeIF.getItemIdList();

        Assert.assertEquals(3, result.size());
        Assert.assertTrue(result.contains(this.getDefaultValue().getOid()));
        Assert.assertTrue(result.contains(value1.getOid()));
        Assert.assertTrue(result.contains(value2.getOid()));
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

  @Request
  @Test
  public void testAddDuplicates()
  {
    TransientDAO transientDAO = TransientDAO.newInstance(this.getMdTransient().definesType());
    AttributeMultiReference attribute = (AttributeMultiReference) transientDAO.getAttribute(this.getMdAttribute().definesAttribute());
    attribute.addItem(this.getDefaultValue().getOid());
    attribute.addItem(this.getDefaultValue().getOid());
    transientDAO.apply();

    AttributeMultiReferenceIF attributeIF = (AttributeMultiReferenceIF) transientDAO.getAttributeIF(this.getMdAttribute().definesAttribute());
    Set<String> result = attributeIF.getItemIdList();

    Assert.assertEquals(1, result.size());
    Assert.assertEquals(this.getDefaultValue().getOid(), result.iterator().next());
  }

  @Request
  @Test
  public void testReplace()
  {
    BusinessDAO value = BusinessDAO.newInstance(this.getMdTerm().definesType());
    value.setStructValue(TermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Term 1");
    value.apply();

    try
    {
      TransientDAO transientDAO = TransientDAO.newInstance(this.getMdTransient().definesType());
      transientDAO.apply();

      AttributeMultiReference attribute = (AttributeMultiReference) transientDAO.getAttribute(this.getMdAttribute().definesAttribute());
      attribute.replaceItems(Arrays.asList(value.getOid()));
      transientDAO.apply();

      AttributeMultiReferenceIF attributeIF = (AttributeMultiReferenceIF) transientDAO.getAttributeIF(this.getMdAttribute().definesAttribute());
      Set<String> result = attributeIF.getItemIdList();

      Assert.assertEquals(1, result.size());
      Assert.assertTrue(result.contains(value.getOid()));
    }
    finally
    {
      TestFixtureFactory.delete(value);
    }
  }

  @Request
  @Test
  public void testClear()
  {
    TransientDAO transientDAO = TransientDAO.newInstance(this.getMdTransient().definesType());
    transientDAO.apply();

    AttributeMultiReference attribute = (AttributeMultiReference) transientDAO.getAttribute(this.getMdAttribute().definesAttribute());
    attribute.clearItems();
    transientDAO.apply();

    AttributeMultiReferenceIF attributeIF = (AttributeMultiReferenceIF) transientDAO.getAttributeIF(this.getMdAttribute().definesAttribute());
    Set<String> result = attributeIF.getItemIdList();

    Assert.assertEquals(0, result.size());
  }

  @Request
  @Test
  public void testRemove()
  {
    TransientDAO transientDAO = TransientDAO.newInstance(this.getMdTransient().definesType());
    transientDAO.apply();

    AttributeMultiReference attribute = (AttributeMultiReference) transientDAO.getAttribute(this.getMdAttribute().definesAttribute());
    attribute.removeItem(this.getDefaultValue().getOid());
    transientDAO.apply();

    AttributeMultiReferenceIF attributeIF = (AttributeMultiReferenceIF) transientDAO.getAttributeIF(this.getMdAttribute().definesAttribute());
    Set<String> result = attributeIF.getItemIdList();

    Assert.assertEquals(0, result.size());
  }

  @Request
  @Test
  public void testRemoveUnsetItem()
  {
    BusinessDAO value1 = BusinessDAO.newInstance(this.getMdTerm().definesType());
    value1.setStructValue(TermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Term 1");
    value1.apply();

    try
    {
      TransientDAO transientDAO = TransientDAO.newInstance(this.getMdTransient().definesType());
      transientDAO.apply();

      AttributeMultiReference attribute = (AttributeMultiReference) transientDAO.getAttribute(this.getMdAttribute().definesAttribute());
      attribute.removeItem(value1.getOid());
      transientDAO.apply();

      AttributeMultiReferenceIF attributeIF = (AttributeMultiReferenceIF) transientDAO.getAttributeIF(this.getMdAttribute().definesAttribute());
      Set<String> result = attributeIF.getItemIdList();

      Assert.assertEquals(1, result.size());
      Assert.assertEquals(this.getDefaultValue().getOid(), result.iterator().next());
    }
    finally
    {
      TestFixtureFactory.delete(value1);
    }
  }

  @Request
  @Test
  public void testDereference()
  {
    BusinessDAO value1 = BusinessDAO.newInstance(this.getMdTerm().definesType());
    value1.setStructValue(TermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Term 1");
    value1.apply();

    try
    {
      TransientDAO transientDAO = TransientDAO.newInstance(this.getMdTransient().definesType());
      AttributeMultiReference attribute = (AttributeMultiReference) transientDAO.getAttribute(this.getMdAttribute().definesAttribute());
      attribute.addItem(value1.getOid());
      transientDAO.apply();

      AttributeMultiReferenceIF attributeIF = (AttributeMultiReferenceIF) transientDAO.getAttributeIF(this.getMdAttribute().definesAttribute());
      BusinessDAOIF[] results = attributeIF.dereference();

      Assert.assertNotNull(results);
      Assert.assertEquals(2, results.length);
    }
    finally
    {
      TestFixtureFactory.delete(value1);
    }
  }

  @Request
  @Test
  public void testEmptyDereference()
  {
    TransientDAO transientDAO = TransientDAO.newInstance(this.getMdTransient().definesType());
    AttributeMultiReference attribute = (AttributeMultiReference) transientDAO.getAttribute(this.getMdAttribute().definesAttribute());
    attribute.clearItems();
    transientDAO.apply();

    AttributeMultiReferenceIF attributeIF = (AttributeMultiReferenceIF) transientDAO.getAttributeIF(this.getMdAttribute().definesAttribute());
    BusinessDAOIF[] results = attributeIF.dereference();

    Assert.assertNotNull(results);
    Assert.assertEquals(0, results.length);
  }

  @Request
  @Test
  public void testAddItem()
  {
    BusinessDAO value1 = BusinessDAO.newInstance(this.getMdTerm().definesType());
    value1.setStructValue(TermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Term 1");
    value1.apply();

    try
    {
      BusinessDAO value2 = BusinessDAO.newInstance(this.getMdTerm().definesType());
      value2.setStructValue(TermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Term 2");
      value2.apply();

      try
      {
        TransientDAO transientDAO = TransientDAO.newInstance(this.getMdTransient().definesType());
        transientDAO.addItem(this.getMdAttribute().definesAttribute(), value1.getOid());
        transientDAO.addItem(this.getMdAttribute().definesAttribute(), value2.getOid());
        transientDAO.apply();

        AttributeMultiReferenceIF attributeIF = (AttributeMultiReferenceIF) transientDAO.getAttributeIF(this.getMdAttribute().definesAttribute());
        Set<String> result = attributeIF.getItemIdList();

        Assert.assertEquals(3, result.size());
        Assert.assertTrue(result.contains(this.getDefaultValue().getOid()));
        Assert.assertTrue(result.contains(value1.getOid()));
        Assert.assertTrue(result.contains(value2.getOid()));
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

  @Request
  @Test
  public void testReplaceItems()
  {
    BusinessDAO value = BusinessDAO.newInstance(this.getMdTerm().definesType());
    value.setStructValue(TermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Term 1");
    value.apply();

    try
    {
      TransientDAO transientDAO = TransientDAO.newInstance(this.getMdTransient().definesType());
      transientDAO.replaceItems(this.getMdAttribute().definesAttribute(), Arrays.asList(value.getOid()));
      transientDAO.apply();

      AttributeMultiReferenceIF attributeIF = (AttributeMultiReferenceIF) transientDAO.getAttributeIF(this.getMdAttribute().definesAttribute());
      Set<String> result = attributeIF.getItemIdList();

      Assert.assertEquals(1, result.size());
      Assert.assertTrue(result.contains(value.getOid()));
    }
    finally
    {
      TestFixtureFactory.delete(value);
    }
  }

  @Request
  @Test
  public void testRemoveItem()
  {
    TransientDAO transientDAO = TransientDAO.newInstance(this.getMdTransient().definesType());
    transientDAO.apply();

    transientDAO.removeItem(this.getMdAttribute().definesAttribute(), this.getDefaultValue().getOid());
    transientDAO.apply();

    AttributeMultiReferenceIF attributeIF = (AttributeMultiReferenceIF) transientDAO.getAttributeIF(this.getMdAttribute().definesAttribute());
    Set<String> result = attributeIF.getItemIdList();

    Assert.assertEquals(0, result.size());
  }

  @Request
  @Test
  public void testClearItems()
  {
    TransientDAO transientDAO = TransientDAO.newInstance(this.getMdTransient().definesType());
    transientDAO.clearItems(this.getMdAttribute().definesAttribute());
    transientDAO.apply();

    AttributeMultiReferenceIF attributeIF = (AttributeMultiReferenceIF) transientDAO.getAttributeIF(this.getMdAttribute().definesAttribute());
    Set<String> result = attributeIF.getItemIdList();

    Assert.assertEquals(0, result.size());
  }

}
