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
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.runwaysdk.business.Business;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.metadata.MdAttributeMultiReferenceDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdTermDAO;
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
public abstract class AbstractEntityMultiReferenceTest
{
  public abstract MdAttributeMultiReferenceDAO getMdAttribute();

  public abstract MdBusinessDAO getMdBusiness();

  public abstract MdTermDAO getMdTerm();

  public abstract BusinessDAO getDefaultValue();

  @Request
  @Test
  public void testApply()
  {
    Business business = new Business(this.getMdBusiness().definesType());
    business.apply();

    List<? extends Business> results = business.getMultiItems(this.getMdAttribute().definesAttribute());

    Assert.assertEquals(1, results.size());
    Assert.assertTrue(this.contains(results, this.getDefaultValue().getOid()));
  }

  @Request
  @Test
  public void testAddMultiple()
  {
    Business value1 = new Business(this.getMdTerm().definesType());
    value1.apply();

    try
    {
      Business value2 = new Business(this.getMdTerm().definesType());
      value2.apply();

      try
      {
        Business business = new Business(this.getMdBusiness().definesType());
        business.addMultiItem(this.getMdAttribute().definesAttribute(), value1.getOid());
        business.addMultiItem(this.getMdAttribute().definesAttribute(), value2.getOid());
        business.apply();

        List<? extends Business> results = business.getMultiItems(this.getMdAttribute().definesAttribute());

        Assert.assertEquals(3, results.size());
        Assert.assertTrue(contains(results, this.getDefaultValue().getOid()));
        Assert.assertTrue(contains(results, value1.getOid()));
        Assert.assertTrue(contains(results, value2.getOid()));
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
    Business business = new Business(this.getMdBusiness().definesType());
    business.addMultiItem(this.getMdAttribute().definesAttribute(), this.getDefaultValue().getOid());
    business.addMultiItem(this.getMdAttribute().definesAttribute(), this.getDefaultValue().getOid());
    business.apply();

    List<? extends Business> results = business.getMultiItems(this.getMdAttribute().definesAttribute());

    Assert.assertEquals(1, results.size());
    Assert.assertTrue(contains(results, this.getDefaultValue().getOid()));
  }

  @Request
  @Test
  public void testGet()
  {
    Business business = new Business(this.getMdBusiness().definesType());
    business.apply();

    Business test = Business.get(business.getOid());

    List<? extends Business> results = test.getMultiItems(this.getMdAttribute().definesAttribute());

    Assert.assertEquals(1, results.size());
    Assert.assertTrue(this.contains(results, this.getDefaultValue().getOid()));
  }

  @Request
  @Test
  public void testReplace()
  {
    Business value = new Business(this.getMdTerm().definesType());
    value.apply();

    try
    {
      Business business = new Business(this.getMdBusiness().definesType());
      business.apply();
      business.replaceMultiItems(this.getMdAttribute().definesAttribute(), Arrays.asList(value.getOid()));
      business.apply();

      List<? extends Business> results = business.getMultiItems(this.getMdAttribute().definesAttribute());

      Assert.assertEquals(1, results.size());
      Assert.assertTrue(contains(results, value.getOid()));
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
    Business business = new Business(this.getMdBusiness().definesType());
    business.apply();
    business.clearMultiItems(this.getMdAttribute().definesAttribute());
    business.apply();

    List<? extends Business> results = business.getMultiItems(this.getMdAttribute().definesAttribute());

    Assert.assertEquals(0, results.size());
  }

  @Request
  @Test
  public void testRemove()
  {
    Business business = new Business(this.getMdBusiness().definesType());
    business.apply();
    business.removeMultiItem(this.getMdAttribute().definesAttribute(), this.getDefaultValue().getOid());
    business.apply();

    List<? extends Business> results = business.getMultiItems(this.getMdAttribute().definesAttribute());

    Assert.assertEquals(0, results.size());
  }

  @Request
  @Test
  public void testRemoveUnsetItem()
  {
    Business value = new Business(this.getMdTerm().definesType());
    value.apply();

    try
    {
      Business business = new Business(this.getMdBusiness().definesType());
      business.apply();
      business.removeMultiItem(this.getMdAttribute().definesAttribute(), value.getOid());
      business.apply();

      List<? extends Business> results = business.getMultiItems(this.getMdAttribute().definesAttribute());

      Assert.assertEquals(1, results.size());
      Assert.assertTrue(contains(results, this.getDefaultValue().getOid()));
    }
    finally
    {
      TestFixtureFactory.delete(value);
    }
  }

  /**
   * @param results
   * @param oid
   * @return
   */
  private boolean contains(List<? extends Business> results, String oid)
  {
    for (Business result : results)
    {
      if (result.getOid().equals(oid))
      {
        return true;
      }
    }

    return false;
  }

}
