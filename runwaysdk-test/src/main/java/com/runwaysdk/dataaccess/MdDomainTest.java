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
package com.runwaysdk.dataaccess;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.runwaysdk.constants.ElementInfo;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.MdDomainInfo;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdDomainDAO;
import com.runwaysdk.session.Request;

public class MdDomainTest
{
  private static MdDomainDAO   mdDomain;

  private static MdBusinessDAO mdBusiness;

  @Request
  @BeforeClass
  public static void classSetUp()
  {
    mdDomain = MdDomainDAO.newInstance();
    mdDomain.setValue(MdDomainInfo.DOMAIN_NAME, "testDomain");
    mdDomain.setStructValue(MdDomainInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Domain");
    mdDomain.setStructValue(MdDomainInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "test domain");
    mdDomain.apply();

    mdBusiness = MdBusinessDAO.newInstance();
    mdBusiness.setValue(MdBusinessInfo.NAME, "TestBusiness");
    mdBusiness.setValue(MdBusinessInfo.PACKAGE, "com.test");
    mdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Business");
    mdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness.apply();
  }

  @Request
  @AfterClass
  public static void classTearDown()
  {
    mdBusiness.delete();
    mdDomain.delete();
  }

  @Request
  @Test
  public void testDomainName()
  {
    Assert.assertEquals("testDomain", mdDomain.getValue(MdDomainInfo.DOMAIN_NAME));
  }

  @Request
  @Test
  public void testSetDomain()
  {
    BusinessDAO businessDAO = BusinessDAO.newInstance(mdBusiness.definesType());
    businessDAO.setValue(ElementInfo.DOMAIN, mdDomain.getOid());
    businessDAO.apply();

    Assert.assertEquals(mdDomain.getOid(), BusinessDAO.get(businessDAO.getOid()).getValue(ElementInfo.DOMAIN));
  }

  @Request
  @Test
  public void testRemoveDomain()
  {
    BusinessDAO businessDAO = BusinessDAO.newInstance(mdBusiness.definesType());
    businessDAO.setValue(ElementInfo.DOMAIN, mdDomain.getOid());
    businessDAO.apply();

    BusinessDAO dao = BusinessDAO.get(businessDAO.getOid()).getBusinessDAO();
    dao.setValue(ElementInfo.DOMAIN, "");
    dao.apply();

    Assert.assertEquals("", BusinessDAO.get(businessDAO.getOid()).getValue(ElementInfo.DOMAIN));
  }
}
