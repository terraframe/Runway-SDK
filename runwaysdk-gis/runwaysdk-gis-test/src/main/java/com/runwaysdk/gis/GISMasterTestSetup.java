/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK GIS(tm).
 *
 * Runway SDK GIS(tm) is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Runway SDK GIS(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Runway SDK GIS(tm). If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.gis;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Assert;
import org.junit.Test;

import com.runwaysdk.constants.EntityCacheMaster;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdAttributeVirtualInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.MdViewInfo;
import com.runwaysdk.constants.TypeInfo;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.metadata.MdAttributeVirtualDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdViewDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.gis.constants.MdAttributeLineStringInfo;
import com.runwaysdk.gis.constants.MdAttributeMultiLineStringInfo;
import com.runwaysdk.gis.constants.MdAttributeMultiPointInfo;
import com.runwaysdk.gis.constants.MdAttributeMultiPolygonInfo;
import com.runwaysdk.gis.constants.MdAttributePointInfo;
import com.runwaysdk.gis.constants.MdAttributePolygonInfo;
import com.runwaysdk.gis.dataaccess.metadata.MdAttributeLineStringDAO;
import com.runwaysdk.gis.dataaccess.metadata.MdAttributeMultiLineStringDAO;
import com.runwaysdk.gis.dataaccess.metadata.MdAttributeMultiPointDAO;
import com.runwaysdk.gis.dataaccess.metadata.MdAttributeMultiPolygonDAO;
import com.runwaysdk.gis.dataaccess.metadata.MdAttributePointDAO;
import com.runwaysdk.gis.dataaccess.metadata.MdAttributePolygonDAO;
import com.runwaysdk.session.Request;

public class GISMasterTestSetup
{
  private static boolean                      isRunningInSuite = false;

  public static final String                  JUNIT_PACKAGE    = "temporary.junit.test.gis";

  public static final TypeInfo                TEST_CLASS       = new TypeInfo(JUNIT_PACKAGE, "TestClass");

  public static final TypeInfo                VIEW_CLASS       = new TypeInfo(JUNIT_PACKAGE, "ViewClass");

  public static final TypeInfo                VIRTUAL_CLASS    = new TypeInfo(JUNIT_PACKAGE, "VirtualClass");

  public static MdBusinessDAO                 testClassMdBusinessDAO;

  public static MdAttributePointDAO           mdAttributePointDAO;

  public static MdAttributeLineStringDAO      mdAttributeLineStringDAO;

  public static MdAttributePolygonDAO         mdAttributePolygonDAO;

  public static MdAttributeMultiPointDAO      mdAttributeMultiPointDAO;

  public static MdAttributeMultiLineStringDAO mdAttributeMultiLineStringDAO;

  public static MdAttributeMultiPolygonDAO    mdAttributeMultiPolygonDAO;

  public static MdViewDAO                     testClassMdViewDAO;

  public static MdAttributePointDAO           viewMdAttributePointDAO;

  public static MdAttributeLineStringDAO      viewMdAttributeLineStringDAO;

  public static MdAttributePolygonDAO         viewMdAttributePolygonDAO;

  public static MdAttributeMultiPointDAO      viewMdAttributeMultiPointDAO;

  public static MdAttributeMultiLineStringDAO viewMdAttributeMultiLineStringDAO;

  public static MdAttributeMultiPolygonDAO    viewMdAttributeMultiPolygonDAO;

  public static MdViewDAO                     testVirtualMdViewDAO;

  public static MdAttributeVirtualDAO         virtualMdAttributePointDAO;

  public static MdAttributeVirtualDAO         virtualMdAttributeLineStringDAO;

  public static MdAttributeVirtualDAO         virtualMdAttributePolygonDAO;

  public static MdAttributeVirtualDAO         virtualMdAttributeMultiPointDAO;

  public static MdAttributeVirtualDAO         virtualMdAttributeMultiLineStringDAO;

  public static MdAttributeVirtualDAO         virtualMdAttributeMultiPolygonDAO;

  /**
   * setUp() allows for global preparation before testing. It is called only
   * once, instead of before each individual test. In this case it establishes a
   * new class (EntityMasterTestSetup.TEST_CLASS.type()) in the database, which
   * can then be used to run tests on.
   * 
   * @throws Exception
   * 
   *           The reason this is split into 2 different methods is to force the
   *           annotation ordering. @Request must come before @Transaction.
   */
  @BeforeClass
  @Request
  public static void masterSetUp() throws Exception
  {
    doSetUp(false);
  }

  @Transaction
  public static void doSetUp(boolean calledFromSuite) throws Exception
  {
    if (calledFromSuite)
    {
      isRunningInSuite = true;
    }
    else if (isRunningInSuite == true)
    {
      return;
    }

    System.out.println("Creating test objects. (GISMasterTestSetup.setUp)");

    try
    {
      testClassMdBusinessDAO = MdBusinessDAO.newInstance();
      testClassMdBusinessDAO.setValue(MdBusinessInfo.NAME, TEST_CLASS.getTypeName());
      testClassMdBusinessDAO.setValue(MdBusinessInfo.PACKAGE, TEST_CLASS.getPackageName());
      testClassMdBusinessDAO.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, TEST_CLASS.getTypeName());
      testClassMdBusinessDAO.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, TEST_CLASS.getTypeName());
      testClassMdBusinessDAO.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      testClassMdBusinessDAO.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
      testClassMdBusinessDAO.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
      testClassMdBusinessDAO.setValue(MdBusinessInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_NOTHING.getOid());
      testClassMdBusinessDAO.apply();

      mdAttributePointDAO = MdAttributePointDAO.newInstance();
      mdAttributePointDAO.setValue(MdAttributePointInfo.NAME, "testPoint");
      mdAttributePointDAO.setStructValue(MdAttributePointInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Point");
      mdAttributePointDAO.setStructValue(MdAttributePointInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Point");
      mdAttributePointDAO.setValue(MdAttributePointInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
      mdAttributePointDAO.setValue(MdAttributePointInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
      mdAttributePointDAO.setValue(MdAttributePointInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
      mdAttributePointDAO.setValue(MdAttributePointInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
      mdAttributePointDAO.setValue(MdAttributePointInfo.SRID, "4326");
      // mdAttributePointDAO.setValue(MdAttributePointInfo.DIMENSION, "2");
      mdAttributePointDAO.setValue(MdAttributePointInfo.DEFINING_MD_CLASS, testClassMdBusinessDAO.getOid());
      mdAttributePointDAO.apply();

      mdAttributeLineStringDAO = MdAttributeLineStringDAO.newInstance();
      mdAttributeLineStringDAO.setValue(MdAttributeLineStringInfo.NAME, "testLineString");
      mdAttributeLineStringDAO.setStructValue(MdAttributeLineStringInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test LineString");
      mdAttributeLineStringDAO.setStructValue(MdAttributeLineStringInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test LineString");
      mdAttributeLineStringDAO.setValue(MdAttributeLineStringInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
      mdAttributeLineStringDAO.setValue(MdAttributeLineStringInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
      mdAttributeLineStringDAO.setValue(MdAttributeLineStringInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
      mdAttributeLineStringDAO.setValue(MdAttributeLineStringInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
      mdAttributeLineStringDAO.setValue(MdAttributeLineStringInfo.SRID, "4326");
      // mdAttributeLineStringDAO.setValue(MdAttributeLineStringInfo.DIMENSION,
      // "2");
      mdAttributeLineStringDAO.setValue(MdAttributeLineStringInfo.DEFINING_MD_CLASS, testClassMdBusinessDAO.getOid());
      mdAttributeLineStringDAO.apply();

      mdAttributePolygonDAO = MdAttributePolygonDAO.newInstance();
      mdAttributePolygonDAO.setValue(MdAttributePolygonInfo.NAME, "testPolygon");
      mdAttributePolygonDAO.setStructValue(MdAttributePolygonInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Polygon");
      mdAttributePolygonDAO.setStructValue(MdAttributePolygonInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Polygon");
      mdAttributePolygonDAO.setValue(MdAttributePolygonInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
      mdAttributePolygonDAO.setValue(MdAttributePolygonInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
      mdAttributePolygonDAO.setValue(MdAttributePolygonInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
      mdAttributePolygonDAO.setValue(MdAttributePolygonInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
      mdAttributePolygonDAO.setValue(MdAttributePolygonInfo.SRID, "4326");
      // mdAttributePolygonDAO.setValue(MdAttributePolygonInfo.DIMENSION, "2");
      mdAttributePolygonDAO.setValue(MdAttributePolygonInfo.DEFINING_MD_CLASS, testClassMdBusinessDAO.getOid());
      mdAttributePolygonDAO.apply();

      mdAttributeMultiPointDAO = MdAttributeMultiPointDAO.newInstance();
      mdAttributeMultiPointDAO.setValue(MdAttributeMultiPointInfo.NAME, "testMultiPoint");
      mdAttributeMultiPointDAO.setStructValue(MdAttributeMultiPointInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test MultiPoint");
      mdAttributeMultiPointDAO.setStructValue(MdAttributeMultiPointInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test MultiPoint");
      mdAttributeMultiPointDAO.setValue(MdAttributeMultiPointInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
      mdAttributeMultiPointDAO.setValue(MdAttributeMultiPointInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
      mdAttributeMultiPointDAO.setValue(MdAttributeMultiPointInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
      mdAttributeMultiPointDAO.setValue(MdAttributeMultiPointInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
      mdAttributeMultiPointDAO.setValue(MdAttributeMultiPointInfo.SRID, "4326");
      // mdAttributeMultiPointDAO.setValue(MdAttributeMultiPointInfo.DIMENSION,
      // "2");
      mdAttributeMultiPointDAO.setValue(MdAttributeMultiPointInfo.DEFINING_MD_CLASS, testClassMdBusinessDAO.getOid());
      mdAttributeMultiPointDAO.apply();

      mdAttributeMultiLineStringDAO = MdAttributeMultiLineStringDAO.newInstance();
      mdAttributeMultiLineStringDAO.setValue(MdAttributeMultiLineStringInfo.NAME, "testMultiLineString");
      mdAttributeMultiLineStringDAO.setStructValue(MdAttributeMultiLineStringInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test MultiLineString");
      mdAttributeMultiLineStringDAO.setStructValue(MdAttributeMultiLineStringInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test MultiLineString");
      mdAttributeMultiLineStringDAO.setValue(MdAttributeMultiLineStringInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
      mdAttributeMultiLineStringDAO.setValue(MdAttributeMultiLineStringInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
      mdAttributeMultiLineStringDAO.setValue(MdAttributeMultiLineStringInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
      mdAttributeMultiLineStringDAO.setValue(MdAttributeMultiLineStringInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
      mdAttributeMultiLineStringDAO.setValue(MdAttributeMultiLineStringInfo.SRID, "4326");
      // mdAttributeMultiLineStringDAO.setValue(MdAttributeMultiLineStringInfo.DIMENSION,
      // "2");
      mdAttributeMultiLineStringDAO.setValue(MdAttributeMultiLineStringInfo.DEFINING_MD_CLASS, testClassMdBusinessDAO.getOid());
      mdAttributeMultiLineStringDAO.apply();

      mdAttributeMultiPolygonDAO = MdAttributeMultiPolygonDAO.newInstance();
      mdAttributeMultiPolygonDAO.setValue(MdAttributeMultiPolygonInfo.NAME, "testMultiPolygon");
      mdAttributeMultiPolygonDAO.setStructValue(MdAttributeMultiPolygonInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test MultiPolygon");
      mdAttributeMultiPolygonDAO.setStructValue(MdAttributeMultiPolygonInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test MultiPolygon");
      mdAttributeMultiPolygonDAO.setValue(MdAttributeMultiPolygonInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
      mdAttributeMultiPolygonDAO.setValue(MdAttributeMultiPolygonInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
      mdAttributeMultiPolygonDAO.setValue(MdAttributeMultiPolygonInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
      mdAttributeMultiPolygonDAO.setValue(MdAttributeMultiPolygonInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
      mdAttributeMultiPolygonDAO.setValue(MdAttributeMultiPolygonInfo.SRID, "4326");
      // mdAttributeMultiPolygonDAO.setValue(MdAttributeMultiPolygonInfo.DIMENSION,
      // "2");
      mdAttributeMultiPolygonDAO.setValue(MdAttributeMultiPolygonInfo.DEFINING_MD_CLASS, testClassMdBusinessDAO.getOid());
      mdAttributeMultiPolygonDAO.apply();

      testClassMdViewDAO = MdViewDAO.newInstance();
      testClassMdViewDAO.setValue(MdViewInfo.NAME, VIEW_CLASS.getTypeName());
      testClassMdViewDAO.setValue(MdViewInfo.PACKAGE, VIEW_CLASS.getPackageName());
      testClassMdViewDAO.setStructValue(MdViewInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, VIEW_CLASS.getTypeName());
      testClassMdViewDAO.setStructValue(MdViewInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, VIEW_CLASS.getTypeName());
      testClassMdViewDAO.setValue(MdViewInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      testClassMdViewDAO.setValue(MdViewInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
      testClassMdViewDAO.setValue(MdViewInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
      testClassMdViewDAO.apply();

      viewMdAttributePointDAO = MdAttributePointDAO.newInstance();
      viewMdAttributePointDAO.setValue(MdAttributePointInfo.NAME, "testPoint");
      viewMdAttributePointDAO.setStructValue(MdAttributePointInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Point");
      viewMdAttributePointDAO.setStructValue(MdAttributePointInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Point");
      viewMdAttributePointDAO.setValue(MdAttributePointInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
      viewMdAttributePointDAO.setValue(MdAttributePointInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
      viewMdAttributePointDAO.setValue(MdAttributePointInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
      viewMdAttributePointDAO.setValue(MdAttributePointInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
      viewMdAttributePointDAO.setValue(MdAttributePointInfo.SRID, "4326");
      // viewMdAttributePointDAO.setValue(MdAttributePointInfo.DIMENSION, "2");
      viewMdAttributePointDAO.setValue(MdAttributePointInfo.DEFINING_MD_CLASS, testClassMdViewDAO.getOid());
      viewMdAttributePointDAO.apply();

      viewMdAttributeLineStringDAO = MdAttributeLineStringDAO.newInstance();
      viewMdAttributeLineStringDAO.setValue(MdAttributeLineStringInfo.NAME, "testLineString");
      viewMdAttributeLineStringDAO.setStructValue(MdAttributeLineStringInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test LineString");
      viewMdAttributeLineStringDAO.setStructValue(MdAttributeLineStringInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test LineString");
      viewMdAttributeLineStringDAO.setValue(MdAttributeLineStringInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
      viewMdAttributeLineStringDAO.setValue(MdAttributeLineStringInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
      viewMdAttributeLineStringDAO.setValue(MdAttributeLineStringInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
      viewMdAttributeLineStringDAO.setValue(MdAttributeLineStringInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
      viewMdAttributeLineStringDAO.setValue(MdAttributeLineStringInfo.SRID, "4326");
      // viewMdAttributeLineStringDAO.setValue(MdAttributeLineStringInfo.DIMENSION,
      // "2");
      viewMdAttributeLineStringDAO.setValue(MdAttributeLineStringInfo.DEFINING_MD_CLASS, testClassMdViewDAO.getOid());
      viewMdAttributeLineStringDAO.apply();

      viewMdAttributePolygonDAO = MdAttributePolygonDAO.newInstance();
      viewMdAttributePolygonDAO.setValue(MdAttributePolygonInfo.NAME, "testPolygon");
      viewMdAttributePolygonDAO.setStructValue(MdAttributePolygonInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Polygon");
      viewMdAttributePolygonDAO.setStructValue(MdAttributePolygonInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Polygon");
      viewMdAttributePolygonDAO.setValue(MdAttributePolygonInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
      viewMdAttributePolygonDAO.setValue(MdAttributePolygonInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
      viewMdAttributePolygonDAO.setValue(MdAttributePolygonInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
      viewMdAttributePolygonDAO.setValue(MdAttributePolygonInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
      viewMdAttributePolygonDAO.setValue(MdAttributePolygonInfo.SRID, "4326");
      // viewMdAttributePolygonDAO.setValue(MdAttributePolygonInfo.DIMENSION,
      // "2");
      viewMdAttributePolygonDAO.setValue(MdAttributePolygonInfo.DEFINING_MD_CLASS, testClassMdViewDAO.getOid());
      viewMdAttributePolygonDAO.apply();

      viewMdAttributeMultiPointDAO = MdAttributeMultiPointDAO.newInstance();
      viewMdAttributeMultiPointDAO.setValue(MdAttributeMultiPointInfo.NAME, "testMultiPoint");
      viewMdAttributeMultiPointDAO.setStructValue(MdAttributeMultiPointInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test MultiPoint");
      viewMdAttributeMultiPointDAO.setStructValue(MdAttributeMultiPointInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test MultiPoint");
      viewMdAttributeMultiPointDAO.setValue(MdAttributeMultiPointInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
      viewMdAttributeMultiPointDAO.setValue(MdAttributeMultiPointInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
      viewMdAttributeMultiPointDAO.setValue(MdAttributeMultiPointInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
      viewMdAttributeMultiPointDAO.setValue(MdAttributeMultiPointInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
      viewMdAttributeMultiPointDAO.setValue(MdAttributeMultiPointInfo.SRID, "4326");
      // viewMdAttributeMultiPointDAO.setValue(MdAttributeMultiPointInfo.DIMENSION,
      // "2");
      viewMdAttributeMultiPointDAO.setValue(MdAttributeMultiPointInfo.DEFINING_MD_CLASS, testClassMdViewDAO.getOid());
      viewMdAttributeMultiPointDAO.apply();

      viewMdAttributeMultiLineStringDAO = MdAttributeMultiLineStringDAO.newInstance();
      viewMdAttributeMultiLineStringDAO.setValue(MdAttributeMultiLineStringInfo.NAME, "testMultiLineString");
      viewMdAttributeMultiLineStringDAO.setStructValue(MdAttributeMultiLineStringInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test MultiLineString");
      viewMdAttributeMultiLineStringDAO.setStructValue(MdAttributeMultiLineStringInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test MultiLineString");
      viewMdAttributeMultiLineStringDAO.setValue(MdAttributeMultiLineStringInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
      viewMdAttributeMultiLineStringDAO.setValue(MdAttributeMultiLineStringInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
      viewMdAttributeMultiLineStringDAO.setValue(MdAttributeMultiLineStringInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
      viewMdAttributeMultiLineStringDAO.setValue(MdAttributeMultiLineStringInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
      viewMdAttributeMultiLineStringDAO.setValue(MdAttributeMultiLineStringInfo.SRID, "4326");
      // viewMdAttributeMultiLineStringDAO.setValue(MdAttributeMultiLineStringInfo.DIMENSION,
      // "2");
      viewMdAttributeMultiLineStringDAO.setValue(MdAttributeMultiLineStringInfo.DEFINING_MD_CLASS, testClassMdViewDAO.getOid());
      viewMdAttributeMultiLineStringDAO.apply();

      viewMdAttributeMultiPolygonDAO = MdAttributeMultiPolygonDAO.newInstance();
      viewMdAttributeMultiPolygonDAO.setValue(MdAttributeMultiPolygonInfo.NAME, "testMultiPolygon");
      viewMdAttributeMultiPolygonDAO.setStructValue(MdAttributeMultiPolygonInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test MultiPolygon");
      viewMdAttributeMultiPolygonDAO.setStructValue(MdAttributeMultiPolygonInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test MultiPolygon");
      viewMdAttributeMultiPolygonDAO.setValue(MdAttributeMultiPolygonInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
      viewMdAttributeMultiPolygonDAO.setValue(MdAttributeMultiPolygonInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
      viewMdAttributeMultiPolygonDAO.setValue(MdAttributeMultiPolygonInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
      viewMdAttributeMultiPolygonDAO.setValue(MdAttributeMultiPolygonInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
      viewMdAttributeMultiPolygonDAO.setValue(MdAttributeMultiPolygonInfo.SRID, "4326");
      // viewMdAttributeMultiPolygonDAO.setValue(MdAttributeMultiPolygonInfo.DIMENSION,
      // "2");
      viewMdAttributeMultiPolygonDAO.setValue(MdAttributeMultiPolygonInfo.DEFINING_MD_CLASS, testClassMdViewDAO.getOid());
      viewMdAttributeMultiPolygonDAO.apply();

      testVirtualMdViewDAO = MdViewDAO.newInstance();
      testVirtualMdViewDAO.setValue(MdViewInfo.NAME, VIRTUAL_CLASS.getTypeName());
      testVirtualMdViewDAO.setValue(MdViewInfo.PACKAGE, VIRTUAL_CLASS.getPackageName());
      testVirtualMdViewDAO.setStructValue(MdViewInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, VIRTUAL_CLASS.getTypeName());
      testVirtualMdViewDAO.setStructValue(MdViewInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, VIRTUAL_CLASS.getTypeName());
      testVirtualMdViewDAO.setValue(MdViewInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      testVirtualMdViewDAO.setValue(MdViewInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
      testVirtualMdViewDAO.setValue(MdViewInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
      testVirtualMdViewDAO.apply();

      virtualMdAttributePointDAO = MdAttributeVirtualDAO.newInstance();
      virtualMdAttributePointDAO.setValue(MdAttributeVirtualInfo.MD_ATTRIBUTE_CONCRETE, mdAttributePointDAO.getOid());
      virtualMdAttributePointDAO.setValue(MdAttributeVirtualInfo.DEFINING_MD_VIEW, testVirtualMdViewDAO.getOid());
      virtualMdAttributePointDAO.apply();

      virtualMdAttributeLineStringDAO = MdAttributeVirtualDAO.newInstance();
      virtualMdAttributeLineStringDAO.setValue(MdAttributeVirtualInfo.MD_ATTRIBUTE_CONCRETE, mdAttributeLineStringDAO.getOid());
      virtualMdAttributeLineStringDAO.setValue(MdAttributeVirtualInfo.DEFINING_MD_VIEW, testVirtualMdViewDAO.getOid());
      virtualMdAttributeLineStringDAO.apply();

      virtualMdAttributePolygonDAO = MdAttributeVirtualDAO.newInstance();
      virtualMdAttributePolygonDAO.setValue(MdAttributeVirtualInfo.MD_ATTRIBUTE_CONCRETE, mdAttributePolygonDAO.getOid());
      virtualMdAttributePolygonDAO.setValue(MdAttributeVirtualInfo.DEFINING_MD_VIEW, testVirtualMdViewDAO.getOid());
      virtualMdAttributePolygonDAO.apply();

      virtualMdAttributeMultiPointDAO = MdAttributeVirtualDAO.newInstance();
      virtualMdAttributeMultiPointDAO.setValue(MdAttributeVirtualInfo.MD_ATTRIBUTE_CONCRETE, mdAttributeMultiPointDAO.getOid());
      virtualMdAttributeMultiPointDAO.setValue(MdAttributeVirtualInfo.DEFINING_MD_VIEW, testVirtualMdViewDAO.getOid());
      virtualMdAttributeMultiPointDAO.apply();

      virtualMdAttributeMultiLineStringDAO = MdAttributeVirtualDAO.newInstance();
      virtualMdAttributeMultiLineStringDAO.setValue(MdAttributeVirtualInfo.MD_ATTRIBUTE_CONCRETE, mdAttributeMultiLineStringDAO.getOid());
      virtualMdAttributeMultiLineStringDAO.setValue(MdAttributeVirtualInfo.DEFINING_MD_VIEW, testVirtualMdViewDAO.getOid());
      virtualMdAttributeMultiLineStringDAO.apply();

      virtualMdAttributeMultiPolygonDAO = MdAttributeVirtualDAO.newInstance();
      virtualMdAttributeMultiPolygonDAO.setValue(MdAttributeVirtualInfo.MD_ATTRIBUTE_CONCRETE, mdAttributeMultiPolygonDAO.getOid());
      virtualMdAttributeMultiPolygonDAO.setValue(MdAttributeVirtualInfo.DEFINING_MD_VIEW, testVirtualMdViewDAO.getOid());
      virtualMdAttributeMultiPolygonDAO.apply();
    }
    catch (Throwable t)
    {

      System.out.println(t);
      t.printStackTrace(System.out);
    }
  }

  /**
   * tearDown(), like setUp(), is called only once, after all tests have been
   * completed. It deletes the MasterTestSetup.TEST_CLASS class, which
   * transitively deletes all of the attributes as well.
   * 
   * The reason this is split into 2 different methods is to force the
   * annotation ordering. @Request must come before @Transaction.
   */
  @Request
  @AfterClass
  public static void tearDown() throws Exception
  {
    doTearDown(false);
  }

  @Transaction
  public static void doTearDown(boolean calledFromSuite) throws Exception
  {
    if (calledFromSuite)
    {
      isRunningInSuite = true;
    }
    else if (isRunningInSuite == true)
    {
      return;
    }

    System.out.println("Destroying test objects. (GISMasterTestSetup.tearDown)");

    TestFixtureFactory.delete(testClassMdBusinessDAO);

    TestFixtureFactory.delete(testClassMdViewDAO);

    TestFixtureFactory.delete(testVirtualMdViewDAO);
  }
}
