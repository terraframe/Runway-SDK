/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
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
 */
/**
*
*/
package com.runwaysdk.business;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import com.runwaysdk.ClasspathTestRunner;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdAttributeMultiTermInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.metadata.MdAttributeMultiTermDAO;
import com.runwaysdk.dataaccess.metadata.MdTermDAO;
import com.runwaysdk.dataaccess.metadata.MdTransientDAO;
import com.runwaysdk.session.Request;

@RunWith(ClasspathTestRunner.class)
public class TransientMultiTermGenTest extends AbstractTransientMultiReferenceGenTest
{
  private static MdTermDAO               mdTerm;

  private static BusinessDAO             defaultValue;

  private static MdTransientDAO          mdView;

  private static MdAttributeMultiTermDAO mdAttributeMultiTerm;

  /**
  * 
  */
  @Request
  @AfterClass
  public static void classTearDown()
  {
    TestFixtureFactory.delete(mdView);
    TestFixtureFactory.delete(mdTerm);
  }

  /**
  * 
  */
  @Request
  @BeforeClass
  public static void classSetUp()
  {
    mdTerm = TestFixtureFactory.createMdTerm();
    mdTerm.apply();

    defaultValue = BusinessDAO.newInstance(mdTerm.definesType());
    defaultValue.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Term 1");
    defaultValue.apply();

    mdView = TestFixtureFactory.createMdView1();
    mdView.apply();

    mdAttributeMultiTerm = MdAttributeMultiTermDAO.newInstance();
    mdAttributeMultiTerm.setValue(MdAttributeMultiTermInfo.NAME, "testMultiTerm");
    mdAttributeMultiTerm.setStructValue(MdAttributeMultiTermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Term Test");
    mdAttributeMultiTerm.setValue(MdAttributeMultiTermInfo.REF_MD_ENTITY, mdTerm.getOid());
    mdAttributeMultiTerm.setValue(MdAttributeMultiTermInfo.DEFINING_MD_CLASS, mdView.getOid());
    mdAttributeMultiTerm.setValue(MdAttributeMultiTermInfo.DEFAULT_VALUE, defaultValue.getOid());
    mdAttributeMultiTerm.apply();
  }

  @Override
  public MdAttributeMultiTermDAO getMdAttribute()
  {
    return mdAttributeMultiTerm;
  }

  @Override
  public MdTransientDAO getMdTransient()
  {
    return mdView;
  }

  @Override
  public MdTermDAO getMdTerm()
  {
    return mdTerm;
  }

  @Override
  public BusinessDAO getDefaultValue()
  {
    return defaultValue;
  }
}
