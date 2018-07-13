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
/**
*
*/
package com.runwaysdk.business;

import java.lang.reflect.Method;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.metadata.MdAttributeMultiReferenceDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdTermDAO;
import com.runwaysdk.generation.CommonGenerationUtil;
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
public abstract class AbstractEntityMultiReferenceGenTest
{
  public abstract MdAttributeMultiReferenceDAO getMdAttribute();

  public abstract MdBusinessDAO getMdBusiness();

  public abstract MdTermDAO getMdTerm();

  public abstract BusinessDAO getDefaultValue();

  private String getAttributeMethodName()
  {
    return CommonGenerationUtil.upperFirstCharacter(this.getMdAttribute().definesAttribute());
  }

  @SuppressWarnings("unchecked")
  @Request
  @Test
  public void testApply() throws Exception
  {
    Business business = BusinessFacade.newBusiness(this.getMdBusiness().definesType());
    business.apply();

    Method method = business.getClass().getMethod(CommonGenerationUtil.GET + this.getAttributeMethodName());
    List<? extends Business> results = (List<? extends Business>) method.invoke(business);

    Assert.assertEquals(1, results.size());
    Assert.assertTrue(this.contains(results, this.getDefaultValue().getId()));
  }

  @SuppressWarnings("unchecked")
  @Request
  @Test
  public void testAddMultiple() throws Exception
  {
    Business value1 = BusinessFacade.newBusiness(this.getMdTerm().definesType());
    value1.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Term 1");
    value1.apply();

    try
    {
      Business value2 = BusinessFacade.newBusiness(this.getMdTerm().definesType());
      value2.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Term 2");
      value2.apply();

      try
      {
        Business business = BusinessFacade.newBusiness(this.getMdBusiness().definesType());
        business.getClass().getMethod(CommonGenerationUtil.ADD + this.getAttributeMethodName(), value1.getClass()).invoke(business, value1);
        business.getClass().getMethod(CommonGenerationUtil.ADD + this.getAttributeMethodName(), value1.getClass()).invoke(business, value2);
        business.apply();

        List<? extends Business> results = (List<? extends Business>) business.getClass().getMethod(CommonGenerationUtil.GET + this.getAttributeMethodName()).invoke(business);

        Assert.assertEquals(3, results.size());
        Assert.assertTrue(contains(results, this.getDefaultValue().getId()));
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
  @Request
  @Test
  public void testAddDuplicates() throws Exception
  {
    Business value = BusinessFacade.get(this.getDefaultValue());

    Business business = BusinessFacade.newBusiness(this.getMdBusiness().definesType());
    business.getClass().getMethod(CommonGenerationUtil.ADD + this.getAttributeMethodName(), value.getClass()).invoke(business, value);
    business.getClass().getMethod(CommonGenerationUtil.ADD + this.getAttributeMethodName(), value.getClass()).invoke(business, value);
    business.apply();

    List<? extends Business> results = (List<? extends Business>) business.getClass().getMethod(CommonGenerationUtil.GET + this.getAttributeMethodName()).invoke(business);

    Assert.assertEquals(1, results.size());
    Assert.assertTrue(contains(results, this.getDefaultValue().getId()));
  }

  @SuppressWarnings("unchecked")
  @Request
  @Test
  public void testGet() throws Exception
  {
    Business business = BusinessFacade.newBusiness(this.getMdBusiness().definesType());
    business.apply();

    Business test = Business.get(business.getId());

    Assert.assertTrue(test.getClass().equals(business.getClass()));

    List<? extends Business> results = (List<? extends Business>) business.getClass().getMethod(CommonGenerationUtil.GET + this.getAttributeMethodName()).invoke(business);

    Assert.assertEquals(1, results.size());
    Assert.assertTrue(contains(results, this.getDefaultValue().getId()));
  }

  @SuppressWarnings("unchecked")
  @Request
  @Test
  public void testClear() throws Exception
  {
    Business business = BusinessFacade.newBusiness(this.getMdBusiness().definesType());
    business.apply();
    business.getClass().getMethod("clear" + this.getAttributeMethodName()).invoke(business);
    business.apply();

    List<? extends Business> results = (List<? extends Business>) business.getClass().getMethod(CommonGenerationUtil.GET + this.getAttributeMethodName()).invoke(business);

    Assert.assertEquals(0, results.size());
  }

  @SuppressWarnings("unchecked")
  @Request
  @Test
  public void testRemove() throws Exception
  {
    Business value = BusinessFacade.get(this.getDefaultValue());

    Business business = BusinessFacade.newBusiness(this.getMdBusiness().definesType());
    business.apply();
    business.getClass().getMethod("remove" + this.getAttributeMethodName(), value.getClass()).invoke(business, value);
    business.apply();

    List<? extends Business> results = (List<? extends Business>) business.getClass().getMethod(CommonGenerationUtil.GET + this.getAttributeMethodName()).invoke(business);

    Assert.assertEquals(0, results.size());
  }

  @SuppressWarnings("unchecked")
  @Request
  @Test
  public void testRemoveUnsetItem() throws Exception
  {
    Business value = BusinessFacade.newBusiness(this.getMdTerm().definesType());
    value.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Term 1");
    value.apply();

    try
    {
      Business business = BusinessFacade.newBusiness(this.getMdBusiness().definesType());
      business.apply();
      business.getClass().getMethod("remove" + this.getAttributeMethodName(), value.getClass()).invoke(business, value);
      business.apply();

      List<? extends Business> results = (List<? extends Business>) business.getClass().getMethod(CommonGenerationUtil.GET + this.getAttributeMethodName()).invoke(business);

      Assert.assertEquals(1, results.size());
      Assert.assertTrue(contains(results, this.getDefaultValue().getId()));
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
