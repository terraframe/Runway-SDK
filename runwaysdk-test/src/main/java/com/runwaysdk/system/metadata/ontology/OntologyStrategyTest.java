/**
*
*/
package com.runwaysdk.system.metadata.ontology;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;

import org.junit.Assert;

import com.runwaysdk.business.ontology.MdTermDAO;
import com.runwaysdk.constants.EntityCacheMaster;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.TermConstants;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.MdTermDAOIF;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;

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
public class OntologyStrategyTest extends TestCase
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
    TestSuite suite = new TestSuite(OntologyStrategyTest.class.getSimpleName());
    suite.addTestSuite(OntologyStrategyTest.class);

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
  }

  /**
   * The tear down done after all the test in the test suite have run
   */
  public static void classTearDown()
  {
  }

  /**
   * No setup needed non-Javadoc)
   * 
   * @see junit.framework.TestCase#setUp()
   */
  protected void setUp() throws Exception
  {

  }

  /**
   * Delete all MetaData objects which were created in the class
   * 
   * @see junit.framework.TestCase#tearDown()
   */
  protected void tearDown() throws Exception
  {
  }

  /**
  *
  *
  */
  public void testCRUD()
  {
    BusinessDAO strategyState = BusinessDAO.newInstance(DatabaseAllPathsStrategy.CLASS);
    strategyState.addItem(DatabaseAllPathsStrategy.STRATEGYSTATE, StrategyState.UNINITIALIZED.getId());
    strategyState.apply();

    try
    {
      BusinessDAOIF test = BusinessDAO.get(strategyState.getId());

      Assert.assertNotNull(test);
      Assert.assertEquals(strategyState.getId(), test.getId());
      Assert.assertEquals(strategyState.getValue(DatabaseAllPathsStrategy.STRATEGYSTATE), test.getValue(DatabaseAllPathsStrategy.STRATEGYSTATE));
    }
    finally
    {
      TestFixtureFactory.delete(strategyState);
    }
  }

  /**
   *
   *
   */
  public void testCreateAndGetMdTerm()
  {

    BusinessDAO state = BusinessDAO.newInstance(DatabaseAllPathsStrategy.CLASS);
    state.addItem(DatabaseAllPathsStrategy.STRATEGYSTATE, StrategyState.UNINITIALIZED.getId());
    state.apply();

    try
    {
      MdTermDAO mdTerm = MdTermDAO.newInstance();
      mdTerm.setValue(TermConstants.NAME, "Term");
      mdTerm.setValue(TermConstants.PACKAGE, "com.test");
      mdTerm.setStructValue(TermConstants.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "JUnit Test Class");
      mdTerm.setStructValue(TermConstants.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Test Class");
      mdTerm.setValue(TermConstants.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
      mdTerm.setValue(TermConstants.ABSTRACT, MdAttributeBooleanInfo.FALSE);
      mdTerm.setValue(TermConstants.CACHE_ALGORITHM, EntityCacheMaster.CACHE_NOTHING.getId());
      mdTerm.setValue(TermConstants.STRATEGY, state.getId());
      mdTerm.apply();

      try
      {
        MdTermDAOIF result = MdTermDAO.getMdTermDAO(mdTerm.definesType());

        Assert.assertNotNull(result);
        Assert.assertEquals(result.getValue(TermConstants.STRATEGY), mdTerm.getValue(TermConstants.STRATEGY));
      }
      finally
      {
        TestFixtureFactory.delete(mdTerm);
      }
    }
    finally
    {
      TestFixtureFactory.delete(state);
    }
  }
}
