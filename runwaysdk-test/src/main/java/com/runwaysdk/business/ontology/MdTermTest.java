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

package com.runwaysdk.business.ontology;

import junit.extensions.TestSetup;

import org.junit.Assert;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;

import com.runwaysdk.constants.EntityCacheMaster;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.TermConstants;
import com.runwaysdk.dataaccess.MdTermDAOIF;

public class MdTermTest extends TestCase
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

  public static Test suite()
  {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(MdTermTest.class);

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
   * Set the testObject to a new Instance of the TEST type.
   */
  protected static void classSetUp()
  {
  }

  /**
   * If testObject was applied, it is removed from the database.
   * 
   * @see TestCase#tearDown()
   */
  protected static void classTearDown()
  {
  }

  public void testCreateAndGetMdTerm()
  {
    MdTermDAO mdTerm = MdTermDAO.newInstance();
    mdTerm.setValue(TermConstants.NAME, "Term");
    mdTerm.setValue(TermConstants.PACKAGE, "com.test");
    mdTerm.setStructValue(TermConstants.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "JUnit Test Class");
    mdTerm.setStructValue(TermConstants.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Test Class");
    mdTerm.setValue(TermConstants.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdTerm.setValue(TermConstants.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    mdTerm.setValue(TermConstants.CACHE_ALGORITHM, EntityCacheMaster.CACHE_NOTHING.getId());
    mdTerm.apply();

    try
    {
      MdTermDAOIF result = MdTermDAO.getMdTermDAO(mdTerm.definesType());

      Assert.assertNotNull(result);
      Assert.assertEquals(result.getValue(TermConstants.NAME), mdTerm.getValue(TermConstants.NAME));
      Assert.assertEquals(result.getValue(TermConstants.PACKAGE), mdTerm.getValue(TermConstants.PACKAGE));
      Assert.assertEquals(result.getStructValue(TermConstants.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), mdTerm.getStructValue(TermConstants.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE));
      Assert.assertEquals(result.getStructValue(TermConstants.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE), mdTerm.getStructValue(TermConstants.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE));
    }
    finally
    {
      mdTerm.delete();
    }
  }
}
