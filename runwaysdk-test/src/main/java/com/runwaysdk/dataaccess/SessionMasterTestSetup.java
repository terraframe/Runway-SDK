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

import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.MdViewInfo;
import com.runwaysdk.constants.TypeInfo;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdTypeDAO;
import com.runwaysdk.dataaccess.metadata.MdViewDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;

/**
 * SessionMasterTestSetup is a wrapper for various Test classes. It contains
 * global setUp() and tearDown() methods, which creates a test class in the
 * database. After the tests are completed, tearDown() deletes the class from
 * the database entirely. If the tests do not <b>complete</b>, the test class is
 * not deleted, and has to be removed manually. Note that <b>passing</b> is not
 * <b>completing</b>. The test class and all of it's traces will be deleted so
 * long as the JUnit suite finished - with or without errors. If the suite is
 * closed or forced to quit prematurely, then the test class may not have been
 * removed from the database.
 * 
 * @author Nathan
 * @version $Revision 1.0 $
 * @since
 */
public class SessionMasterTestSetup
{
  public static final String     JUNIT_PACKAGE        = EntityMasterTestSetup.JUNIT_PACKAGE;

  public static final TypeInfo   PARENT_SESSION_CLASS = new TypeInfo(JUNIT_PACKAGE, "ParentSession");

  public static final TypeInfo   CHILD_SESSION_CLASS  = new TypeInfo(JUNIT_PACKAGE, "ChildSession");

  public static final TypeInfo   REFERENCE_CLASS      = new TypeInfo(JUNIT_PACKAGE, "Reference");

  public static final TypeInfo   SOME_CLASS           = new TypeInfo(JUNIT_PACKAGE, "SomeClass");

  /**
   * Child MdView
   */
  protected static MdViewDAO     mdView;

  /**
   * MdView that defines all of the attributes.
   */
  protected static MdViewDAO     attributeMdView;

  /**
   * MdBusiness class used for a reference attribute.
   */
  protected static MdBusinessDAO referenceMdBusiness;

  private TypeInfo               activeType;

  /**
   * Standard consturctor, with the addition of the parameter that specifies
   * which sesion class, the parent or the child, that should have the
   * attributes defined on it.
   * 
   * @param suite
   *          The test suite to wrap with setUp and TearDown
   * @param activeType
   *          Specifies the session type that the attributes will be defined on.
   */
  public SessionMasterTestSetup(TypeInfo activeType)
  {
    this.activeType = activeType;
  }

  /**
   * setUp() allows for global preparation before testing. It is called only
   * once, instead of before each individual test. In this case it establishes a
   * new class (MasterTestSetup.TEST_CLASS.type()) in the database, which can
   * then be used to run tests on.
   */
  @Transaction
  public void setUp()
  {
    try
    {
      MdViewDAO parentMdView = MdViewDAO.newInstance();
      parentMdView.setValue(MdViewInfo.NAME, PARENT_SESSION_CLASS.getTypeName());
      parentMdView.setValue(MdViewInfo.PACKAGE, PARENT_SESSION_CLASS.getPackageName());
      parentMdView.setValue(MdViewInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      parentMdView.setStructValue(MdViewInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Parent View");
      parentMdView.setStructValue(MdViewInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Parent View");
      parentMdView.setValue(MdViewInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
      parentMdView.setValue(MdViewInfo.ABSTRACT, MdAttributeBooleanInfo.TRUE);
      parentMdView.setValue(MdViewInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      parentMdView.apply();

      MdViewDAO childMdView = MdViewDAO.newInstance();
      childMdView.setValue(MdViewInfo.NAME, CHILD_SESSION_CLASS.getTypeName());
      childMdView.setValue(MdViewInfo.PACKAGE, CHILD_SESSION_CLASS.getPackageName());
      childMdView.setValue(MdViewInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      childMdView.setStructValue(MdViewInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test View");
      childMdView.setStructValue(MdViewInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "First ever defined view.");
      childMdView.setValue(MdViewInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
      childMdView.setValue(MdViewInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
      childMdView.setValue(MdViewInfo.SUPER_MD_VIEW, parentMdView.getOid());
      childMdView.setValue(MdViewInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      childMdView.apply();

      mdView = childMdView;

      if (this.activeType.getType().equals(PARENT_SESSION_CLASS))
      {
        attributeMdView = parentMdView;
      }
      else
      {
        attributeMdView = childMdView;
      }

      MdBusinessDAO mdBusiness = MdBusinessDAO.newInstance();
      mdBusiness.setValue(MdBusinessInfo.NAME, REFERENCE_CLASS.getTypeName());
      mdBusiness.setValue(MdBusinessInfo.PACKAGE, REFERENCE_CLASS.getPackageName());
      mdBusiness.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      mdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "JUnitRefType");
      mdBusiness.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "JUnit Reference Type");
      mdBusiness.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
      mdBusiness.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
      mdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      mdBusiness.apply();

      referenceMdBusiness = mdBusiness;

      mdBusiness = MdBusinessDAO.newInstance();
      mdBusiness.setValue(MdBusinessInfo.NAME, SOME_CLASS.getTypeName());
      mdBusiness.setValue(MdBusinessInfo.PACKAGE, SOME_CLASS.getPackageName());
      mdBusiness.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      mdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Some Class");
      mdBusiness.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Some JUnit Type");
      mdBusiness.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
      mdBusiness.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
      mdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      mdBusiness.apply();

    }
    catch (Exception e)
    {
      System.err.println(e.getMessage());
      e.printStackTrace();
    }
  }

  /**
   * tearDown(), like setUp(), is called only once, after all tests have been
   * completed. It deletes the MasterTestSetup.TEST_CLASS class, which
   * transitively deletes all of the attributes as well.
   */
  @Transaction
  public void tearDown()
  {
    try
    {
      TestFixtureFactory.delete(MdViewDAO.getMdViewDAO(PARENT_SESSION_CLASS.getType()));
      TestFixtureFactory.delete(MdTypeDAO.getMdTypeDAO(REFERENCE_CLASS.getType()));
      TestFixtureFactory.delete(MdTypeDAO.getMdTypeDAO(SOME_CLASS.getType()));
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
  }

}
