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
/*
 * Created on Jun 15, 2005
 */
package com.runwaysdk.dataaccess;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestResult;

import com.runwaysdk.constants.EntityCacheMaster;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.MdElementInfo;
import com.runwaysdk.constants.TypeInfo;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;

/**
 * EntityMasterTestSetup is a wrapper for various Test classes. It contains
 * global setUp() and tearDown() methods, which creates a test class in the
 * database. After the tests are completed, tearDown() deletes the class from
 * the database entirely. If the tests do not <b>complete</b>, the test class is
 * not deleted, and has to be removed manually. Note that <b>passing</b> is not
 * <b>completing</b>. The test class and all of it's traces will be deleted so
 * long as the JUnit suite finished - with or without errors. If the suite is
 * closed or forced to quit prematurely, then the test class may not have been
 * removed from the database.
 * 
 * @author Eric
 * @version $Revision 1.0 $
 * @since
 */
public class EntityMasterTestSetup extends TestSetup
{
  @Override
  public void run(TestResult testResult)
  {
    super.run(testResult);
  }

  public static final String   JUNIT_PACKAGE   = "temporary.junit.test";

  public static final TypeInfo TEST_CLASS      = new TypeInfo(JUNIT_PACKAGE, "Test");

  public static final TypeInfo REFERENCE_CLASS = new TypeInfo(JUNIT_PACKAGE, "Reference");

  /**
   * <code>testMdBusinessId</code> is the ID for the metadata object that
   * describes the Test class. It's used to delete the type after tests are
   * completed.
   */
  private String               testMdBusinessId;

  /**
   * <code>referenceMdBusinessId</code> is the ID for the metadata object that
   * describes the MasterTestSetup.REFERENCE_CLASS class. It's used to delete
   * the type after tests are completed.
   */
  private String               referenceMdBusinessId;

  private int                  cache_code;

  /**
   * Simply the inherited constructor. Caches Everything on the
   * EntityMasterTestSetup.TEST_CLASS class.
   * 
   * @param suite
   */
  public EntityMasterTestSetup(Test suite)
  {
    super(suite);
    cache_code = EntityCacheMaster.CACHE_EVERYTHING.getCacheCode();
  }

  /**
   * Standard constructor, with the addition of the parameter that specifies the
   * caching on the EntityMasterTestSetup.TEST_CLASS datatypes. Invalid codes
   * will default to CACHE_EVERYTHING.
   * 
   * @param suite
   *          The test suite to wrap with setUp and TearDown
   * @param c
   *          Specifies the desired caching on the
   *          EntityMasterTestSetup.TEST_CLASS class
   */
  public EntityMasterTestSetup(Test suite, int c)
  {
    super(suite);
    cache_code = c;
  }

  /**
   * setUp() allows for global preparation before testing. It is called only
   * once, instead of before each individual test. In this case it establishes a
   * new class (EntityMasterTestSetup.TEST_CLASS.type()) in the database, which
   * can then be used to run tests on.
   */
  @Transaction
  protected void setUp() throws Exception
  {

    try
    {
      // Create an new class (MasterTestSetup.REFERENCE_CLASS) that we can
      // reference with Reference Fields
      MdBusinessDAO referenceBusiness = MdBusinessDAO.newInstance();
      referenceBusiness.setValue(MdBusinessInfo.NAME, EntityMasterTestSetup.REFERENCE_CLASS.getTypeName());
      referenceBusiness.setValue(MdBusinessInfo.PACKAGE, EntityMasterTestSetup.REFERENCE_CLASS.getPackageName());
      referenceBusiness.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      referenceBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "JUnitRefType");
      referenceBusiness.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "JUnit Reference Type");
      referenceBusiness.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
      referenceBusiness.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
      referenceBusiness.setGenerateMdController(false);
      referenceBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);

      referenceMdBusinessId = referenceBusiness.apply();

      // Create the MasterTestSetup.TEST_CLASS class.
      MdBusinessDAO testMdBusiness = MdBusinessDAO.newInstance();
      testMdBusiness.setValue(MdBusinessInfo.NAME, EntityMasterTestSetup.TEST_CLASS.getTypeName());
      testMdBusiness.setValue(MdBusinessInfo.PACKAGE, EntityMasterTestSetup.TEST_CLASS.getPackageName());
      testMdBusiness.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      testMdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "JUnit Test Type");
      testMdBusiness.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Test Type");
      testMdBusiness.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
      testMdBusiness.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
      testMdBusiness.setGenerateMdController(false);
      testMdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);

      // Switching on cache_code determines the caching of the class
      if (cache_code == EntityCacheMaster.CACHE_EVERYTHING.getCacheCode())
      {
        testMdBusiness.setValue(MdElementInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_EVERYTHING.getId());
      }
      else if (cache_code == EntityCacheMaster.CACHE_NOTHING.getCacheCode())
      {
        testMdBusiness.setValue(MdElementInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_NOTHING.getId());
      }
      else if (cache_code == EntityCacheMaster.CACHE_MOST_RECENTLY_USED.getCacheCode())
      {
        testMdBusiness.setValue(MdElementInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_MOST_RECENTLY_USED.getId());
      }
      testMdBusinessId = testMdBusiness.apply();
    }
    catch (Exception e)
    {
      System.err.println(e.getMessage());
      e.printStackTrace();

      throw new RuntimeException(e);
    }
  }

  /**
   * tearDown(), like setUp(), is called only once, after all tests have been
   * completed. It deletes the MasterTestSetup.TEST_CLASS class, which
   * transitively deletes all of the attributes as well.
   */
  @Transaction
  protected void tearDown()
  {
    try
    {
      TestFixtureFactory.delete(MdBusinessDAO.get(referenceMdBusinessId));
      TestFixtureFactory.delete(MdBusinessDAO.get(testMdBusinessId));
    }
    catch (Exception e)
    {
      System.err.println(e.getMessage());
      e.printStackTrace();

      throw new RuntimeException(e);
    }
  }
}
