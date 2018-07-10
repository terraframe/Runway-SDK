/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
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

import com.runwaysdk.constants.ElementInfo;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.MdDomainInfo;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdDomainDAO;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;

public class MdDomainTest extends TestCase
{
  private static MdDomainDAO mdDomain;

  private static MdBusinessDAO mdBusiness;

  public static Test suite()
  {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(MdDomainTest.class);

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

  protected static void classSetUp()
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
    mdBusiness.setGenerateMdController(false);
    mdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness.apply();
  }

  protected static void classTearDown()
  {
    mdBusiness.delete();
    mdDomain.delete();
  }

  @Override
  public TestResult run()
  {
    return super.run();
  }

  @Override
  public void run(TestResult testResult)
  {
    super.run(testResult);
  }

  @Override
  protected void setUp() throws Exception
  {
  }

  @Override
  protected void tearDown() throws Exception
  {
  }

  public void testDomainName()
  {
    assertEquals("testDomain", mdDomain.getValue(MdDomainInfo.DOMAIN_NAME));
  }

  public void testSetDomain()
  {
    BusinessDAO businessDAO = BusinessDAO.newInstance(mdBusiness.definesType());
    businessDAO.setValue(ElementInfo.DOMAIN, mdDomain.getId());
    businessDAO.apply();

    assertEquals(mdDomain.getId(), BusinessDAO.get(businessDAO.getId()).getValue(ElementInfo.DOMAIN));
  }


  public void testRemoveDomain()
  {
    BusinessDAO businessDAO = BusinessDAO.newInstance(mdBusiness.definesType());
    businessDAO.setValue(ElementInfo.DOMAIN, mdDomain.getId());
    businessDAO.apply();

    BusinessDAO dao = BusinessDAO.get(businessDAO.getId()).getBusinessDAO();
    dao.setValue(ElementInfo.DOMAIN, "");
    dao.apply();


    assertEquals("", BusinessDAO.get(businessDAO.getId()).getValue(ElementInfo.DOMAIN));
  }
}
