/**
*
*/
package com.runwaysdk.business;

import java.lang.reflect.Method;
import java.util.List;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.junit.Assert;

import com.runwaysdk.business.ontology.MdTermDAO;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdAttributeMultiReferenceInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
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
public class EntityMultiReferenceGenTest extends TestCase
{
  private static MdTermDAO                    mdTerm;

  private static BusinessDAO                  defaultValue;

  private static MdBusinessDAO                mdBusiness;

  private static MdAttributeMultiReferenceDAO mdAttributeMultiReference;

  public static Test suite()
  {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(EntityMultiReferenceGenTest.class);

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

  @SuppressWarnings("unchecked")
  public void testApply() throws Exception
  {
    Business business = BusinessFacade.newBusiness(mdBusiness.definesType());
    business.apply();

    Method method = business.getClass().getMethod("getTestMultiReference");
    List<? extends Business> results = (List<? extends Business>) method.invoke(business);

    Assert.assertEquals(1, results.size());
    Assert.assertTrue(this.contains(results, defaultValue.getId()));
  }

  @SuppressWarnings("unchecked")
  public void testAddMultiple() throws Exception
  {
    Business value1 = BusinessFacade.newBusiness(mdTerm.definesType());
    value1.apply();

    try
    {
      Business value2 = BusinessFacade.newBusiness(mdTerm.definesType());
      value2.apply();

      try
      {
        Business business = BusinessFacade.newBusiness(mdBusiness.definesType());
        business.getClass().getMethod("addTestMultiReference", value1.getClass()).invoke(business, value1);
        business.getClass().getMethod("addTestMultiReference", value1.getClass()).invoke(business, value2);
        business.apply();

        List<? extends Business> results = (List<? extends Business>) business.getClass().getMethod("getTestMultiReference").invoke(business);

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

  @SuppressWarnings("unchecked")
  public void testAddDuplicates() throws Exception
  {
    Business value = BusinessFacade.get(defaultValue);

    Business business = BusinessFacade.newBusiness(mdBusiness.definesType());
    business.getClass().getMethod("addTestMultiReference", value.getClass()).invoke(business, value);
    business.getClass().getMethod("addTestMultiReference", value.getClass()).invoke(business, value);
    business.apply();

    List<? extends Business> results = (List<? extends Business>) business.getClass().getMethod("getTestMultiReference").invoke(business);

    Assert.assertEquals(1, results.size());
    Assert.assertTrue(contains(results, defaultValue.getId()));
  }

  @SuppressWarnings("unchecked")
  public void testGet() throws Exception
  {
    Business business = BusinessFacade.newBusiness(mdBusiness.definesType());
    business.apply();

    Business test = Business.get(business.getId());

    Assert.assertTrue(test.getClass().equals(business.getClass()));

    List<? extends Business> results = (List<? extends Business>) business.getClass().getMethod("getTestMultiReference").invoke(business);

    Assert.assertEquals(1, results.size());
    Assert.assertTrue(contains(results, defaultValue.getId()));
  }

  @SuppressWarnings("unchecked")
  public void testClear() throws Exception
  {
    Business business = BusinessFacade.newBusiness(mdBusiness.definesType());
    business.apply();
    business.getClass().getMethod("clearTestMultiReference").invoke(business);
    business.apply();

    List<? extends Business> results = (List<? extends Business>) business.getClass().getMethod("getTestMultiReference").invoke(business);

    Assert.assertEquals(0, results.size());
  }

  @SuppressWarnings("unchecked")
  public void testRemove() throws Exception
  {
    Business value = BusinessFacade.get(defaultValue);

    Business business = BusinessFacade.newBusiness(mdBusiness.definesType());
    business.apply();
    business.getClass().getMethod("removeTestMultiReference", value.getClass()).invoke(business, value);
    business.apply();

    List<? extends Business> results = (List<? extends Business>) business.getClass().getMethod("getTestMultiReference").invoke(business);

    Assert.assertEquals(0, results.size());
  }

  @SuppressWarnings("unchecked")
  public void testRemoveUnsetItem() throws Exception
  {
    Business value = BusinessFacade.newBusiness(mdTerm.definesType());
    value.apply();

    try
    {
      Business business = BusinessFacade.newBusiness(mdBusiness.definesType());
      business.apply();
      business.getClass().getMethod("removeTestMultiReference", value.getClass()).invoke(business, value);
      business.apply();

      List<? extends Business> results = (List<? extends Business>) business.getClass().getMethod("getTestMultiReference").invoke(business);

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
