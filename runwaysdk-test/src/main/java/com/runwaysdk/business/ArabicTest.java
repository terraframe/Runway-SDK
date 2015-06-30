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
package com.runwaysdk.business;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;

import com.runwaysdk.constants.DatabaseProperties;
import com.runwaysdk.constants.EnumerationMasterInfo;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.TestConstants;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.io.XMLImporter;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdPackage;

public class ArabicTest extends TestCase {
    
    private static String                  pack = "test.arabic";
    private static MdBusinessDAO           testMdBusiness;
    
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
	
    public static Test suite()
    {
        TestSuite suite = new TestSuite();
        suite.addTestSuite(ArabicTest.class);
    
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
	   * Launch-point for the standalone textui JUnit tests in this class.
	   *
	   * @param args
	   */
	  public static void main(String[] args)
	  {
	    if (DatabaseProperties.getDatabaseClass().equals("hsqldb"))
	      XMLImporter.main(new String[] { TestConstants.Path.schema_xsd, TestConstants.Path.metadata_xml });

	    junit.textui.TestRunner.run(ArabicTest.suite());
	  }

	protected static void classSetUp() {
	    MdBusinessDAOIF enumMasterMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(EnumerationMasterInfo.CLASS);
	    
	    testMdBusiness = MdBusinessDAO.newInstance();
	    //testMdBusiness.setValue(MdBusinessInfo.NAME,              "ArabicTest");
	    testMdBusiness.setValue(MdBusinessInfo.NAME,              "سلام");
//	    String tableName = testMdBusiness.getValue(MdBusinessInfo.NAME);
	    testMdBusiness.setValue(MdBusinessInfo.PACKAGE,           pack);
	    testMdBusiness.setValue(MdBusinessInfo.REMOVE,            MdAttributeBooleanInfo.TRUE);
	    testMdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL,  MdAttributeLocalInfo.DEFAULT_LOCALE, "ArabicTest");
	    testMdBusiness.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE,      "ArabicTest");
	    testMdBusiness.setValue(MdBusinessInfo.EXTENDABLE,        MdAttributeBooleanInfo.FALSE);
	    testMdBusiness.setValue(MdBusinessInfo.ABSTRACT,          MdAttributeBooleanInfo.FALSE);
	    testMdBusiness.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, enumMasterMdBusinessIF.getId());
	    testMdBusiness.apply();
	}

	protected static void classTearDown() {
	    new MdPackage(pack).delete();
	}
	
	
	public void testBusinessValueExistence()
	{
	    /*
	    BusinessDAO businessDAO = BusinessDAO.newInstance(testMdBusiness.definesType());
	    businessDAO.setValue(EnumerationMasterInfo.NAME, "FICTION");
	    businessDAO.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "EnglishTest");
	    businessDAO.apply();
	    */
	    assertEquals("سلام", testMdBusiness.getTableName());
	}

}

