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
package com.runwaysdk.dataaccess.cache;

import static org.junit.Assert.assertEquals;
import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeIntegerInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.TypeInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.EntityMasterTestSetup;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.cache.globalcache.ehcache.TransactionDiskstore;
import com.runwaysdk.dataaccess.metadata.MdAttributeIntegerDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdTypeDAO;

public class BasicCacheTest extends TestCase
{
  /**
   * Container for package and class name of the new parent class
   */
  private static final TypeInfo parentInfo = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "Parent1");
  
  private static MdBusinessDAO parentMD;
  
  public static void main(String[] args)
  {
    junit.textui.TestRunner.run(BasicCacheTest.suite());
  }

  public static Test suite()
  {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(BasicCacheTest.class);

    TestSetup wrapper = new TestSetup(suite)
    {
      protected void setUp()
      {
        setUpClass();
      }
      protected void tearDown()
      {
        classTearDown();
      }
    };

    return wrapper;
  }
  
  public static void setUpClass()
  {
    parentMD = MdBusinessDAO.newInstance();
    parentMD.setValue(MdBusinessInfo.NAME,                 parentInfo.getTypeName());
    parentMD.setValue(MdBusinessInfo.PACKAGE,              parentInfo.getPackageName());
    parentMD.setValue(MdBusinessInfo.REMOVE,               MdAttributeBooleanInfo.TRUE);
    parentMD.setStructValue(MdBusinessInfo.DISPLAY_LABEL,  MdAttributeLocalInfo.DEFAULT_LOCALE, "JUnit Test Parent");
    parentMD.setStructValue(MdBusinessInfo.DESCRIPTION,    MdAttributeLocalInfo.DEFAULT_LOCALE,      "Temporary JUnit Parent Class");
    parentMD.setValue(MdBusinessInfo.EXTENDABLE,           MdAttributeBooleanInfo.TRUE);
    parentMD.setValue(MdBusinessInfo.ABSTRACT,             MdAttributeBooleanInfo.FALSE);
    parentMD.setValue(MdBusinessInfo.CACHE_SIZE,           "50");
    parentMD.apply();
    
    MdAttributeIntegerDAO topSpeed = MdAttributeIntegerDAO.newInstance();
    topSpeed.setValue(MdAttributeIntegerInfo.NAME, "testInt");
    topSpeed.setValue(MdAttributeIntegerInfo.DEFINING_MD_CLASS, parentMD.getId());
    topSpeed.setStructValue(MdAttributeIntegerInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test int");
    topSpeed.apply();
  }
  
  public static void classTearDown()
  {
    MdBusinessDAOIF parentMD = MdBusinessDAO.getMdBusinessDAO(parentInfo.getType());
    if (MdTypeDAO.isDefined(parentMD.definesType()))
    {
      parentMD.getBusinessDAO().delete();
    }
  }
  
  public void testTransactionDiskstore()
  {
    TransactionStore tds = new TransactionStore(15);
    
    int testNum = 10;
    String[] ids = new String[testNum];
    int[] javaId = new int[testNum];
    
    for (int i = 0; i < testNum; ++i)
    {
      BusinessDAO p = generateDAO("p" + i, i);
      ids[i] = p.getId();
      tds.putEntityDAOIFintoCache(p);
      javaId[i] = System.identityHashCode(p);
    }
    
    for (int i = 0; i < testNum; ++i)
    {
      BusinessDAO p = (BusinessDAO) tds.getEntityDAOIFfromCache(ids[i]);
      assertEquals(ids[i], p.getId());
      assertEquals(String.valueOf(i), p.getAttribute("testInt").getValue());
      assertEquals(System.identityHashCode(p), javaId[i]);
    }
  }
  
  public void testCreateDeleteMdBusiness()
  {
    MdBusinessDAO parentMD2 = MdBusinessDAO.newInstance();
    parentMD2.setValue(MdBusinessInfo.NAME,                 "OtherTest");
    parentMD2.setValue(MdBusinessInfo.PACKAGE,              parentInfo.getPackageName());
    parentMD2.setValue(MdBusinessInfo.REMOVE,               MdAttributeBooleanInfo.TRUE);
    parentMD2.setStructValue(MdBusinessInfo.DISPLAY_LABEL,  MdAttributeLocalInfo.DEFAULT_LOCALE, "JUnit Test Parent");
    parentMD2.setStructValue(MdBusinessInfo.DESCRIPTION,    MdAttributeLocalInfo.DEFAULT_LOCALE,      "Temporary JUnit Parent Class");
    parentMD2.setValue(MdBusinessInfo.EXTENDABLE,           MdAttributeBooleanInfo.TRUE);
    parentMD2.setValue(MdBusinessInfo.ABSTRACT,             MdAttributeBooleanInfo.FALSE);
    parentMD2.setValue(MdBusinessInfo.CACHE_SIZE,           "50");
    parentMD2.apply();
    
    parentMD2.delete();
  }
  
  private BusinessDAO generateDAO(String name, int i)
  {
    BusinessDAO p = BusinessDAO.newInstance(parentInfo.getType());
    p.setValue("testInt", String.valueOf(i));
    p.apply();
    
    return p;
  }
}
