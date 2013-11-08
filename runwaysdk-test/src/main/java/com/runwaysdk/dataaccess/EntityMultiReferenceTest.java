/**
*
*/
package com.runwaysdk.dataaccess;

import java.util.Arrays;
import java.util.List;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.junit.Assert;

import com.runwaysdk.business.Business;
import com.runwaysdk.business.ontology.MdTermDAO;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdAttributeMultiReferenceInfo;
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
public class EntityMultiReferenceTest extends TestCase
{
  private static MdTermDAO                    mdTerm;

  private static BusinessDAO                  defaultValue;

  private static MdBusinessDAO                mdBusiness;

  private static MdAttributeMultiReferenceDAO mdAttributeMultiReference;

  public static Test suite()
  {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(EntityMultiReferenceTest.class);

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

  public void testApply()
  {
    Business business = new Business(mdBusiness.definesType());
    business.apply();

    List<? extends Business> results = business.getMultiItems(mdAttributeMultiReference.definesAttribute());

    Assert.assertEquals(1, results.size());
    Assert.assertTrue(this.contains(results, defaultValue.getId()));
  }

  public void testAddMultiple()
  {
    Business value1 = new Business(mdTerm.definesType());
    value1.apply();

    try
    {
      Business value2 = new Business(mdTerm.definesType());
      value2.apply();

      try
      {
        Business business = new Business(mdBusiness.definesType());
        business.addMultiItem(mdAttributeMultiReference.definesAttribute(), value1.getId());
        business.addMultiItem(mdAttributeMultiReference.definesAttribute(), value2.getId());
        business.apply();

        List<? extends Business> results = business.getMultiItems(mdAttributeMultiReference.definesAttribute());

        Assert.assertEquals(3, results.size());
        Assert.assertTrue(contains(results, defaultValue.getId()));
        Assert.assertTrue(contains(results, value1.getId()));
        Assert.assertTrue(contains(results, value2.getId()));
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
    Business business = new Business(mdBusiness.definesType());
    business.addMultiItem(mdAttributeMultiReference.definesAttribute(), defaultValue.getId());
    business.addMultiItem(mdAttributeMultiReference.definesAttribute(), defaultValue.getId());
    business.apply();

    List<? extends Business> results = business.getMultiItems(mdAttributeMultiReference.definesAttribute());

    Assert.assertEquals(1, results.size());
    Assert.assertTrue(contains(results, defaultValue.getId()));
  }

  public void testGet()
  {
    Business business = new Business(mdBusiness.definesType());
    business.apply();

    Business test = Business.get(business.getId());

    List<? extends Business> results = test.getMultiItems(mdAttributeMultiReference.definesAttribute());

    Assert.assertEquals(1, results.size());
    Assert.assertTrue(this.contains(results, defaultValue.getId()));
  }

  public void testReplace()
  {
    Business value = new Business(mdTerm.definesType());
    value.apply();

    try
    {
      Business business = new Business(mdBusiness.definesType());
      business.apply();
      business.replaceMultiItems(mdAttributeMultiReference.definesAttribute(), Arrays.asList(value.getId()));
      business.apply();

      List<? extends Business> results = business.getMultiItems(mdAttributeMultiReference.definesAttribute());

      Assert.assertEquals(1, results.size());
      Assert.assertTrue(contains(results, value.getId()));
    }
    finally
    {
      TestFixtureFactory.delete(value);
    }
  }

  public void testClear()
  {
    Business business = new Business(mdBusiness.definesType());
    business.apply();
    business.clearMultiItems(mdAttributeMultiReference.definesAttribute());
    business.apply();

    List<? extends Business> results = business.getMultiItems(mdAttributeMultiReference.definesAttribute());

    Assert.assertEquals(0, results.size());
  }

  public void testRemove()
  {
    Business business = new Business(mdBusiness.definesType());
    business.apply();
    business.removeMultiItem(mdAttributeMultiReference.definesAttribute(), defaultValue.getId());
    business.apply();

    List<? extends Business> results = business.getMultiItems(mdAttributeMultiReference.definesAttribute());

    Assert.assertEquals(0, results.size());
  }

  public void testRemoveUnsetItem()
  {
    Business value = new Business(mdTerm.definesType());
    value.apply();

    try
    {
      Business business = new Business(mdBusiness.definesType());
      business.apply();
      business.removeMultiItem(mdAttributeMultiReference.definesAttribute(), value.getId());
      business.apply();

      List<? extends Business> results = business.getMultiItems(mdAttributeMultiReference.definesAttribute());

      Assert.assertEquals(1, results.size());
      Assert.assertTrue(contains(results, defaultValue.getId()));
    }
    finally
    {
      TestFixtureFactory.delete(value);
    }
  }

  /**
   * @param results
   * @param id
   * @return
   */
  private boolean contains(List<? extends Business> results, String id)
  {
    for (Business result : results)
    {
      if (result.getId().equals(id))
      {
        return true;
      }
    }

    return false;
  }

}
