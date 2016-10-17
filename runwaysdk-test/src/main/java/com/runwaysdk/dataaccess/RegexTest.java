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

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;

import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeIntegerInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.TypeInfo;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.metadata.MdAttributeIntegerDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.NameConventionException;

public class RegexTest extends TestCase
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

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(new EntityMasterTestSetup(RegexTest.suite()));
  }

  public static Test suite()
  {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(RegexTest.class);

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

  public static void classSetUp()
  {
  }

  public static void classTearDown()
  {
  }

  /**
   * Creates a bunch of valid class types and makes sure they pass regex
   * filtering.
   */
  public void testValidClassTypes()
  {
    MdBusinessDAO testMdBusiness = null;

    TypeInfo myClassInfo = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "MyClass");

    // MyClass
    try
    {
      testMdBusiness = MdBusinessDAO.newInstance();
      testMdBusiness.setValue(MdBusinessInfo.NAME, myClassInfo.getTypeName());
      testMdBusiness.setValue(MdBusinessInfo.PACKAGE, myClassInfo.getPackageName());
      testMdBusiness.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      testMdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, myClassInfo.getTypeName() + " Test Type");
      testMdBusiness.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Test Type");
      testMdBusiness.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
      testMdBusiness.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
      testMdBusiness.setGenerateMdController(false);
      testMdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      testMdBusiness.apply();
      testMdBusiness.delete();
    }
    catch (DataAccessException e)
    {
      fail(e.getMessage());
    }

    TypeInfo my_ClassInfo = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "My_Class");

    // My_Class
    try
    {
      testMdBusiness = MdBusinessDAO.newInstance();
      testMdBusiness.setValue(MdBusinessInfo.NAME, my_ClassInfo.getTypeName());
      testMdBusiness.setValue(MdBusinessInfo.PACKAGE, my_ClassInfo.getPackageName());
      testMdBusiness.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      testMdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, my_ClassInfo.getTypeName() + " Test Type");
      testMdBusiness.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Test Type");
      testMdBusiness.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
      testMdBusiness.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
      testMdBusiness.setGenerateMdController(false);
      testMdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      testMdBusiness.apply();
      testMdBusiness.delete();
    }
    catch (DataAccessException e)
    {
      fail(e.getMessage());
    }

    TypeInfo mYCLASSInfo = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "MYCLASS");

    // MYCLASS
    try
    {
      testMdBusiness = MdBusinessDAO.newInstance();
      testMdBusiness.setValue(MdBusinessInfo.NAME, mYCLASSInfo.getTypeName());
      testMdBusiness.setValue(MdBusinessInfo.PACKAGE, mYCLASSInfo.getPackageName());
      testMdBusiness.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      testMdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, mYCLASSInfo.getTypeName() + " Test Type");
      testMdBusiness.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Test Type");
      testMdBusiness.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
      testMdBusiness.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
      testMdBusiness.setGenerateMdController(false);
      testMdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      testMdBusiness.apply();
      testMdBusiness.delete();
    }
    catch (DataAccessException e)
    {
      fail(e.getMessage());
    }

    TypeInfo mClassInfo = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "L");

    // L
    try
    {
      testMdBusiness = MdBusinessDAO.newInstance();
      testMdBusiness.setValue(MdBusinessInfo.NAME, mClassInfo.getTypeName());
      testMdBusiness.setValue(MdBusinessInfo.PACKAGE, mClassInfo.getPackageName());
      testMdBusiness.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      testMdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, mClassInfo.getTypeName() + " Test Type");
      testMdBusiness.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Test Type");
      testMdBusiness.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
      testMdBusiness.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
      testMdBusiness.setGenerateMdController(false);
      testMdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      testMdBusiness.apply();
      testMdBusiness.delete();
    }
    catch (DataAccessException e)
    {
      fail(e.getMessage());
    }
  }

  /**
   * Creates a bunch of invalid class types and makes sure they fail regex
   * filtering.
   */
  public void ignoreInvalidClassNames()
  {
    MdBusinessDAO testMdBusiness = null;

    TypeInfo _MyClassInfo = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "_MyClass");

    // _MyClass
    try
    {
      testMdBusiness = MdBusinessDAO.newInstance();
      testMdBusiness.setValue(MdBusinessInfo.NAME, _MyClassInfo.getTypeName());
      testMdBusiness.setValue(MdBusinessInfo.PACKAGE, _MyClassInfo.getPackageName());
      testMdBusiness.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      testMdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, _MyClassInfo.getTypeName() + " Test Type");
      testMdBusiness.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Test Type");
      testMdBusiness.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
      testMdBusiness.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
      testMdBusiness.setGenerateMdController(false);
      testMdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      testMdBusiness.apply();
      testMdBusiness.delete();
      fail("MdBusiness.validate() failed to catch _MyClass as an invalid class type.");
    }
    catch (NameConventionException e)
    {
      // This is expected
    }

    TypeInfo MyClass_Info = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "MyClass_");

    // MyClass_
    try
    {
      testMdBusiness = MdBusinessDAO.newInstance();
      testMdBusiness.setValue(MdBusinessInfo.NAME, MyClass_Info.getTypeName());
      testMdBusiness.setValue(MdBusinessInfo.PACKAGE, MyClass_Info.getPackageName());
      testMdBusiness.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      testMdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MyClass_Info.getTypeName() + " Test Type");
      testMdBusiness.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Test Type");
      testMdBusiness.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
      testMdBusiness.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
      testMdBusiness.setGenerateMdController(false);
      testMdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      testMdBusiness.apply();
      testMdBusiness.delete();
      fail("MdBusiness.validate() failed to catch _MyClass as an invalid class type.");
    }
    catch (NameConventionException e)
    {
      // This is expected
    }

    TypeInfo myClassInfo = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "myClass");

    // myClass
    try
    {
      testMdBusiness = MdBusinessDAO.newInstance();
      testMdBusiness.setValue(MdBusinessInfo.NAME, myClassInfo.getTypeName());
      testMdBusiness.setValue(MdBusinessInfo.PACKAGE, myClassInfo.getPackageName());
      testMdBusiness.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      testMdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, myClassInfo.getTypeName() + " Test Type");
      testMdBusiness.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Test Type");
      testMdBusiness.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
      testMdBusiness.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
      testMdBusiness.setGenerateMdController(false);
      testMdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      testMdBusiness.apply();
      testMdBusiness.delete();
      fail("MdBusiness.validate() failed to catch myClass as an invalid class type.");
    }
    catch (NameConventionException e)
    {
      // This is expected
    }

    TypeInfo myPercentClassInfo = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "My%Class");

    // My%Class
    try
    {
      testMdBusiness = MdBusinessDAO.newInstance();
      testMdBusiness.setValue(MdBusinessInfo.NAME, myPercentClassInfo.getTypeName());
      testMdBusiness.setValue(MdBusinessInfo.PACKAGE, myPercentClassInfo.getPackageName());
      testMdBusiness.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      testMdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, myPercentClassInfo.getTypeName() + " Test Type");
      testMdBusiness.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Test Type");
      testMdBusiness.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
      testMdBusiness.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
      testMdBusiness.setGenerateMdController(false);
      testMdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      testMdBusiness.apply();
      testMdBusiness.delete();
      fail("MdBusiness.validate() failed to catch My%Class as an invalid class type.");
    }
    catch (NameConventionException e)
    {
      // This is expected
    }

    TypeInfo threeMyClassInfo = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "3MyClass");

    // 3MyClass
    try
    {
      testMdBusiness = MdBusinessDAO.newInstance();
      testMdBusiness.setValue(MdBusinessInfo.NAME, threeMyClassInfo.getTypeName());
      testMdBusiness.setValue(MdBusinessInfo.PACKAGE, threeMyClassInfo.getPackageName());
      testMdBusiness.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      testMdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, threeMyClassInfo.getTypeName() + " Test Type");
      testMdBusiness.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Test Type");
      testMdBusiness.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
      testMdBusiness.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
      testMdBusiness.setGenerateMdController(false);
      testMdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      testMdBusiness.apply();
      testMdBusiness.delete();
      fail("MdBusiness.validate() failed to catch 3MyClass as an invalid class type.");
    }
    catch (NameConventionException e)
    {
      // This is expected
    }
  }

  /**
   * Creates a bunch of valid attribute names and makes sure they pass regex
   * filtering.
   */
  public void testValidAttributeNames()
  {
    MdBusinessDAO testMdBusiness = null;

    TypeInfo myClassInfo = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "MyClass");

    // MyClass
    try
    {
      testMdBusiness = MdBusinessDAO.newInstance();
      testMdBusiness.setValue(MdBusinessInfo.NAME, myClassInfo.getTypeName());
      testMdBusiness.setValue(MdBusinessInfo.PACKAGE, myClassInfo.getPackageName());
      testMdBusiness.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      testMdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, myClassInfo.getTypeName() + " Test Type");
      testMdBusiness.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Test Type");
      testMdBusiness.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
      testMdBusiness.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
      testMdBusiness.setGenerateMdController(false);
      testMdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      testMdBusiness.apply();

      MdAttributeIntegerDAO attribute = null;
      attribute = MdAttributeIntegerDAO.newInstance();
      attribute.setValue(MdAttributeIntegerInfo.NAME, "int5");
      attribute.setStructValue(MdAttributeIntegerInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "int5");
      attribute.setValue(MdAttributeIntegerInfo.DEFAULT_VALUE, "");
      attribute.setValue(MdAttributeIntegerInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
      attribute.setValue(MdAttributeIntegerInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      attribute.setValue(MdAttributeIntegerInfo.DEFINING_MD_CLASS, testMdBusiness.getId());
      attribute.apply();

      attribute = MdAttributeIntegerDAO.newInstance();
      attribute.setValue(MdAttributeIntegerInfo.NAME, "int_");
      attribute.setStructValue(MdAttributeIntegerInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "int_");
      attribute.setValue(MdAttributeIntegerInfo.DEFAULT_VALUE, "");
      attribute.setValue(MdAttributeIntegerInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
      attribute.setValue(MdAttributeIntegerInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      attribute.setValue(MdAttributeIntegerInfo.DEFINING_MD_CLASS, testMdBusiness.getId());
      attribute.apply();

      attribute = MdAttributeIntegerDAO.newInstance();
      attribute.setValue(MdAttributeIntegerInfo.NAME, "intInt");
      attribute.setStructValue(MdAttributeIntegerInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "intInt");
      attribute.setValue(MdAttributeIntegerInfo.DEFAULT_VALUE, "");
      attribute.setValue(MdAttributeIntegerInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
      attribute.setValue(MdAttributeIntegerInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      attribute.setValue(MdAttributeIntegerInfo.DEFINING_MD_CLASS, testMdBusiness.getId());
      attribute.apply();

      attribute = MdAttributeIntegerDAO.newInstance();
      attribute.setValue(MdAttributeIntegerInfo.NAME, "i1nR_I6nt_");
      attribute.setStructValue(MdAttributeIntegerInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "complex");
      attribute.setValue(MdAttributeIntegerInfo.DEFAULT_VALUE, "");
      attribute.setValue(MdAttributeIntegerInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
      attribute.setValue(MdAttributeIntegerInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      attribute.setValue(MdAttributeIntegerInfo.DEFINING_MD_CLASS, testMdBusiness.getId());
      attribute.apply();
    }
    catch (DataAccessException e)
    {
      if (testMdBusiness != null)
        testMdBusiness.delete();
      fail(e.getMessage());
    }

    TestFixtureFactory.delete(testMdBusiness);
  }

  /**
   * Creates a bunch of invalid Attribute names and makes sure they fail regex
   * filtering.
   */
  public void testInvalidAttributeNames()
  {
    MdBusinessDAO testMdBusiness = null;

    TypeInfo myClassInfo = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "MyClass");

    // MyClass
    try
    {
      testMdBusiness = MdBusinessDAO.newInstance();
      testMdBusiness.setValue(MdBusinessInfo.NAME, myClassInfo.getTypeName());
      testMdBusiness.setValue(MdBusinessInfo.PACKAGE, myClassInfo.getPackageName());
      testMdBusiness.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      testMdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, myClassInfo.getTypeName() + " Test Type");
      testMdBusiness.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Test Type");
      testMdBusiness.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
      testMdBusiness.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
      testMdBusiness.setGenerateMdController(false);
      testMdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      testMdBusiness.apply();

      MdAttributeIntegerDAO attribute = null;
      attribute = MdAttributeIntegerDAO.newInstance();
      attribute.setValue(MdAttributeIntegerInfo.NAME, "MyVar");
      attribute.setStructValue(MdAttributeIntegerInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "_int");
      attribute.setValue(MdAttributeIntegerInfo.DEFAULT_VALUE, "");
      attribute.setValue(MdAttributeIntegerInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
      attribute.setValue(MdAttributeIntegerInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      attribute.setValue(MdAttributeIntegerInfo.DEFINING_MD_CLASS, testMdBusiness.getId());
      attribute.apply();

      attribute = MdAttributeIntegerDAO.newInstance();
      attribute.setValue(MdAttributeIntegerInfo.NAME, "My&Var");
      attribute.setStructValue(MdAttributeIntegerInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "int5");
      attribute.setValue(MdAttributeIntegerInfo.DEFAULT_VALUE, "");
      attribute.setValue(MdAttributeIntegerInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
      attribute.setValue(MdAttributeIntegerInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      attribute.setValue(MdAttributeIntegerInfo.DEFINING_MD_CLASS, testMdBusiness.getId());
      attribute.apply();

      attribute = MdAttributeIntegerDAO.newInstance();
      attribute.setValue(MdAttributeIntegerInfo.NAME, "7myVar");
      attribute.setStructValue(MdAttributeIntegerInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "int_");
      attribute.setValue(MdAttributeIntegerInfo.DEFAULT_VALUE, "");
      attribute.setValue(MdAttributeIntegerInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
      attribute.setValue(MdAttributeIntegerInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      attribute.setValue(MdAttributeIntegerInfo.DEFINING_MD_CLASS, testMdBusiness.getId());
      attribute.apply();
    }
    catch (DataAccessException e)
    {
      // we want it to fall in here!
      if (testMdBusiness != null)
      {
        TestFixtureFactory.delete(testMdBusiness);
      }
      return;
    }
    if (testMdBusiness != null)
    {
      TestFixtureFactory.delete(testMdBusiness);
    }
    fail("MdAttribute.validate() accepted incorrect attribute names.");
  }

  /**
   * Creates a bunch of valid package names and makes sure they pass regex
   * filtering.
   */
  public void testValidPackageNames()
  {
    MdBusinessDAO testMdBusiness = null;

    // mr.sparkle
    try
    {
      testMdBusiness = MdBusinessDAO.newInstance();
      testMdBusiness.setValue(MdBusinessInfo.NAME, "MyClass");
      testMdBusiness.setValue(MdBusinessInfo.PACKAGE, "mr.sparkle");
      testMdBusiness.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      testMdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "MyClass Test Type");
      testMdBusiness.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Test Type");
      testMdBusiness.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
      testMdBusiness.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
      testMdBusiness.setGenerateMdController(false);
      testMdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      testMdBusiness.apply();
      testMdBusiness.delete();
    }
    catch (DataAccessException e)
    {
      fail(e.getMessage());
    }

    // mr._sparkle_
    try
    {
      testMdBusiness = MdBusinessDAO.newInstance();
      testMdBusiness.setValue(MdBusinessInfo.NAME, "MyClass");
      testMdBusiness.setValue(MdBusinessInfo.PACKAGE, "mr._sparkle_");
      testMdBusiness.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      testMdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "MyClass Test Type");
      testMdBusiness.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Test Type");
      testMdBusiness.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
      testMdBusiness.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
      testMdBusiness.setGenerateMdController(false);
      testMdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      testMdBusiness.apply();
      testMdBusiness.delete();
    }
    catch (DataAccessException e)
    {
      fail("MdBusiness.validate() failed to valid the package, mr._sparkle_");
    }

    // mr
    try
    {
      testMdBusiness = MdBusinessDAO.newInstance();
      testMdBusiness.setValue(MdBusinessInfo.NAME, "MyClass");
      testMdBusiness.setValue(MdBusinessInfo.PACKAGE, "mr");
      testMdBusiness.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      testMdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "MyClass Test Type");
      testMdBusiness.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Test Type");
      testMdBusiness.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
      testMdBusiness.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
      testMdBusiness.setGenerateMdController(false);
      testMdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      testMdBusiness.apply();
      testMdBusiness.delete();
    }
    catch (DataAccessException e)
    {
      fail("MdBusiness.validate() failed to valid the package, mr.");
    }

    // stateBlah
    try
    {
      testMdBusiness = MdBusinessDAO.newInstance();
      testMdBusiness.setValue(MdBusinessInfo.NAME, "MyClass");
      testMdBusiness.setValue(MdBusinessInfo.PACKAGE, "stateBlah");
      testMdBusiness.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      testMdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "MyClass Test Type");
      testMdBusiness.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Test Type");
      testMdBusiness.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
      testMdBusiness.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
      testMdBusiness.setGenerateMdController(false);
      testMdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      testMdBusiness.apply();
      testMdBusiness.delete();
    }
    catch (NameConventionException e)
    {
      fail("Created a package with an invalid name state");
    }
  }

  /**
   * Creates a bunch of invalid package names and makes sure they fail regex
   * filtering.
   */
  public void testInvalidPackageNames()
  {
    MdBusinessDAO testMdBusiness = null;

    // (empty package)
    try
    {
      testMdBusiness = MdBusinessDAO.newInstance();
      testMdBusiness.setValue(MdBusinessInfo.NAME, "MyClass");
      testMdBusiness.setValue(MdBusinessInfo.PACKAGE, "");
      testMdBusiness.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      testMdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "MyClass Test Type");
      testMdBusiness.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Test Type");
      testMdBusiness.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
      testMdBusiness.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
      testMdBusiness.setGenerateMdController(false);
      testMdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      testMdBusiness.apply();
      testMdBusiness.delete();
      fail("MdBusiness.validate() failed to valid the package ''");
    }
    catch (NameConventionException e)
    {
      // expected
    }

    try
    {
      testMdBusiness = MdBusinessDAO.newInstance();
      testMdBusiness.setValue(MdBusinessInfo.NAME, "MyClass");
      testMdBusiness.setValue(MdBusinessInfo.PACKAGE, "mr..sparkle");
      testMdBusiness.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      testMdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "MyClass Test Type");
      testMdBusiness.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Test Type");
      testMdBusiness.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
      testMdBusiness.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
      testMdBusiness.setGenerateMdController(false);
      testMdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      testMdBusiness.apply();
      testMdBusiness.delete();
      fail("MdBusiness.validate() failed to valid the package, mr..sparkle");
    }
    catch (NameConventionException e)
    {
      // This is expected
    }

    // .mr.sparkle
    try
    {
      testMdBusiness = MdBusinessDAO.newInstance();
      testMdBusiness.setValue(MdBusinessInfo.NAME, "MyClass");
      testMdBusiness.setValue(MdBusinessInfo.PACKAGE, ".mr.sparkle");
      testMdBusiness.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      testMdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "MyClass Test Type");
      testMdBusiness.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Test Type");
      testMdBusiness.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
      testMdBusiness.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
      testMdBusiness.setGenerateMdController(false);
      testMdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      testMdBusiness.apply();
      testMdBusiness.delete();
      fail("MdBusiness.validate() failed to valid the package, .mr.sparkle");
    }
    catch (NameConventionException e)
    {
      // This is expected
    }

    // mr.sparkle.
    try
    {
      testMdBusiness = MdBusinessDAO.newInstance();
      testMdBusiness.setValue(MdBusinessInfo.NAME, "MyClass");
      testMdBusiness.setValue(MdBusinessInfo.PACKAGE, "mr.sparkle.");
      testMdBusiness.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      testMdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "MyClass Test Type");
      testMdBusiness.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Test Type");
      testMdBusiness.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
      testMdBusiness.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
      testMdBusiness.setGenerateMdController(false);
      testMdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      testMdBusiness.apply();
      testMdBusiness.delete();
      fail("MdBusiness.validate() failed to valid the package, mr.sparkle.");
    }
    catch (NameConventionException e)
    {
      // This is expected
    }

    // mr.spark&le
    try
    {
      testMdBusiness = MdBusinessDAO.newInstance();
      testMdBusiness.setValue(MdBusinessInfo.NAME, "MyClass");
      testMdBusiness.setValue(MdBusinessInfo.PACKAGE, "mr.spark&le");
      testMdBusiness.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      testMdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "MyClass Test Type");
      testMdBusiness.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Test Type");
      testMdBusiness.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
      testMdBusiness.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
      testMdBusiness.setGenerateMdController(false);
      testMdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      testMdBusiness.apply();
      testMdBusiness.delete();
      fail("MdBusiness.validate() failed to valid the package, mr.spark&le");
    }
    catch (NameConventionException e)
    {
      // This is expected
    }

    // 6mr5.1sparkle5
    try
    {
      testMdBusiness = MdBusinessDAO.newInstance();
      testMdBusiness.setValue(MdBusinessInfo.NAME, "MyClass");
      testMdBusiness.setValue(MdBusinessInfo.PACKAGE, "6mr5.1sparkle5");
      testMdBusiness.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      testMdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "MyClass Test Type");
      testMdBusiness.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Test Type");
      testMdBusiness.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
      testMdBusiness.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
      testMdBusiness.setGenerateMdController(false);
      testMdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      testMdBusiness.apply();
      testMdBusiness.delete();
      fail("Created a package with an invalid name 6mr5.1sparkle5");
    }
    catch (NameConventionException e)
    {
      // This is expected
    }

    // state
    try
    {
      testMdBusiness = MdBusinessDAO.newInstance();
      testMdBusiness.setValue(MdBusinessInfo.NAME, "MyClass");
      testMdBusiness.setValue(MdBusinessInfo.PACKAGE, "state");
      testMdBusiness.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      testMdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "MyClass Test Type");
      testMdBusiness.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Test Type");
      testMdBusiness.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
      testMdBusiness.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
      testMdBusiness.setGenerateMdController(false);
      testMdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      testMdBusiness.apply();
      testMdBusiness.delete();
      fail("Created a package with an invalid name state");
    }
    catch (NameConventionException e)
    {
      // this is expected
    }

    // state.test
    try
    {
      testMdBusiness = MdBusinessDAO.newInstance();
      testMdBusiness.setValue(MdBusinessInfo.NAME, "MyClass");
      testMdBusiness.setValue(MdBusinessInfo.PACKAGE, "state.test");
      testMdBusiness.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      testMdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "MyClass Test Type");
      testMdBusiness.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Test Type");
      testMdBusiness.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
      testMdBusiness.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
      testMdBusiness.setGenerateMdController(false);
      testMdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      testMdBusiness.apply();
      testMdBusiness.delete();
      fail("Created a package with an invalid name state");
    }
    catch (NameConventionException e)
    {
      // this is expected
    }
  }
}
