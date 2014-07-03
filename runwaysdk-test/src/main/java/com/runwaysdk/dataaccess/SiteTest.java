/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved. 
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
 ******************************************************************************/
package com.runwaysdk.dataaccess;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.DatabaseProperties;
import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.MdEntityInfo;
import com.runwaysdk.constants.TestConstants;
import com.runwaysdk.constants.UserInfo;
import com.runwaysdk.dataaccess.io.XMLImporter;
import com.runwaysdk.dataaccess.metadata.MdAttributeCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeConcreteDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdElementDAO;

public class SiteTest extends TestCase
{
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

  private static MdBusinessDAO mdBusiness = null;

  public static Test suite()
  {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(SiteTest.class);

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
   * The setup done before the test suite is run
   */
  public static void classSetUp()
  {
    mdBusiness = MdBusinessDAO.newInstance();
    mdBusiness.setValue(MdBusinessInfo.PACKAGE, "test.xmlclasses");
    mdBusiness.setValue(MdBusinessInfo.NAME, "Class1");
    mdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Importer Class 1");
    mdBusiness.apply();

    MdAttributeConcreteDAO mdAttribute = MdAttributeCharacterDAO.newInstance();
    mdAttribute.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, mdBusiness.getId());
    mdAttribute.setStructValue(MdAttributeCharacterInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Character");
    mdAttribute.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL,  MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Character");
    mdAttribute.setValue(MdAttributeCharacterInfo.NAME, "testChar");
    mdAttribute.setValue(MdAttributeCharacterInfo.SIZE, Integer.toString(MdAttributeCharacterInfo.MAX_CHARACTER_SIZE));
    mdAttribute.apply();
  }

  /**
   * The tear down done after all the test in the test suite have run
   */
  public static void classTearDown()
  {
    mdBusiness.delete();
  }

  /**
   * Test to ensure that an EntityDAO can be updated from the same domain
   */
  public void testInsert()
  {
    BusinessDAO businessDAO = BusinessDAO.newInstance(mdBusiness.definesType());
    businessDAO.setValue("testChar", "firstValue");
    businessDAO.apply();

    businessDAO.setValue("testChar", "secondValue");
    businessDAO.apply();

    assertEquals("secondValue", businessDAO.getValue("testChar"));
  }

  /**
   * Test to ensure that an EntityDAO cannot be updated from a different domain if a non-system attribute is modified.
   */
  public void testInvalidSiteNonSystemAttributes()
  {   
    MdElementDAO mdEntity = MdBusinessDAO.getMdBusinessDAO(UserInfo.CLASS).getBusinessDAO();

    String originalDomain = CommonProperties.getDomain();
    
    String originalCacheSize = mdEntity.getValue(MdEntityInfo.CACHE_SIZE);
    
    boolean fail = false;
    try
    {
      CommonProperties.setDomain("some_other_domain");
      // If a non-system attribute is modified, then this should fail. It is OK for runway itself
      // to modify and maintain system attributes.
      mdEntity.setValue(MdEntityInfo.CACHE_SIZE, "100");
      mdEntity.apply();

      fail = true;
      fail("Able to update an entity from a different site");
    }
    catch(SiteException e)
    {
      // Ensure this does not blow up
      e.getLocalizedMessage();
      //Expect to be here
    }
    finally
    {
      CommonProperties.setDomain(originalDomain);
      if (fail)
      {
        mdEntity.setValue(MdEntityInfo.CACHE_SIZE, originalCacheSize);
        mdEntity.apply();
      }
    }
  }
  
  /**
   * Test to ensure that an EntityDAO can be updated from a different domain if only system attributes are modified.
   */
  public void testInvalidSiteSystemAttributes()
  {   
    MdElementDAO mdEntity = MdBusinessDAO.getMdBusinessDAO(UserInfo.CLASS).getBusinessDAO();

    String originalDomain = CommonProperties.getDomain();
        
    try
    {
      CommonProperties.setDomain("some_other_domain");
      // If a non-system attribute is modified, then this should fail. It is OK for runway itself
      // to modify and maintain system attributes.
      mdEntity.apply();
    }
    catch(SiteException e)
    {
      fail("Unable to update an entity from a different site where only system attributes were modifed.");
    }
    finally
    {
      CommonProperties.setDomain(originalDomain);
    }
  }

  public static void main(String args[])
  {
    if (DatabaseProperties.getDatabaseClass().equals("hsqldb"))
    {
      XMLImporter.main(new String[] { TestConstants.Path.schema_xsd, TestConstants.Path.metadata_xml });
    }

    TestSuite suite = new TestSuite();

    suite.addTest(SiteTest.suite());

    TestRunner.run(suite);
  }
}
