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

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeJsonInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.metadata.MdAttributeJsonDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.session.Request;

public class EntityAttributeJsonTest
{
  private static MdBusinessDAO           mdBusiness;

  private static MdAttributeJsonDAO mdAttributeJson;

  /**
  * 
  */
  @Request
  @AfterClass
  public static void classTearDown()
  {
    TestFixtureFactory.delete(mdBusiness);
  }

  /**
  * 
  */
  @Request
  @BeforeClass
  public static void classSetUp()
  {
    mdBusiness = TestFixtureFactory.createMdBusiness1();
    mdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness.apply();

    mdAttributeJson = MdAttributeJsonDAO.newInstance();
    mdAttributeJson.setValue(MdAttributeJsonInfo.NAME, "testJson");
    mdAttributeJson.setStructValue(MdAttributeJsonInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Term Test");
    mdAttributeJson.setValue(MdAttributeJsonInfo.DEFINING_MD_CLASS, mdBusiness.getOid());
    mdAttributeJson.apply();
  }

  public MdAttributeJsonDAO getMdAttribute()
  {
    return mdAttributeJson;
  }

  public MdBusinessDAO getMdBusiness()
  {
    return mdBusiness;
  }

  public AttributeIF getAttribute(BusinessDAO business)
  {
    return (AttributeIF) business.getAttributeIF(this.getMdAttribute().definesAttribute());
  }
  
  @Request
  @Test
  public void testGetAttribute()
  {
    BusinessDAO business = BusinessDAO.newInstance(this.getMdBusiness().definesType());
    AttributeIF attribute = business.getAttributeIF(this.getMdAttribute().definesAttribute());

    Assert.assertNotNull(attribute);
    Assert.assertTrue(attribute instanceof AttributeIF);
  }

  @Request
  @Test
  public void testApply()
  {
    JSONObject object = new JSONObject();
    object.put("testValue", "Test entry");
    
    BusinessDAO business = BusinessDAO.newInstance(this.getMdBusiness().definesType());
    business.setValue(this.getMdAttribute().definesAttribute(), object.toString());
    business.apply();

    AttributeIF attribute = getAttribute(business);

    Assert.assertTrue(attribute.getValue().contains(object.getString("testValue")));
  }

  @Request
  @Test
  public void testNull()
  {
    BusinessDAO business = BusinessDAO.newInstance(this.getMdBusiness().definesType());
    business.apply();
    
    AttributeIF attribute = getAttribute(business);
    
    Assert.assertTrue(StringUtils.isBlank(attribute.getValue()));
  }
  
  @Request
  @Test
  public void testUpdate()
  {
    JSONObject original = new JSONObject();
    original.put("testValue", "Test entry");
    
    BusinessDAO business = BusinessDAO.newInstance(this.getMdBusiness().definesType());
    business.setValue(this.getMdAttribute().definesAttribute(), original.toString());
    business.apply();
    
    JSONObject update = new JSONObject();
    update.put("testValue", "Updated");
    
    business.setValue(this.getMdAttribute().definesAttribute(), update.toString());
    business.apply();
    
    AttributeIF attribute = getAttribute(business);
    
    Assert.assertTrue(attribute.getValue().contains(update.getString("testValue")));
  }
  
  @Request
  @Test(expected = ProgrammingErrorException.class)
  public void testBadValue()
  {
    BusinessDAO business = BusinessDAO.newInstance(this.getMdBusiness().definesType());
    business.setValue(this.getMdAttribute().definesAttribute(), "BAD");
    business.apply();
    
    AttributeIF attribute = getAttribute(business);
    
    Assert.assertTrue(StringUtils.isBlank(attribute.getValue()));
  }
}

