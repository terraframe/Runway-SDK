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
/**
*
*/
package com.runwaysdk.dataaccess;

import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdAttributeMultiReferenceInfo;
import com.runwaysdk.constants.MdTermInfo;
import com.runwaysdk.constants.MdViewInfo;
import com.runwaysdk.constants.TermInfo;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.metadata.MdAttributeMultiReferenceDAO;
import com.runwaysdk.dataaccess.metadata.MdTermDAO;
import com.runwaysdk.dataaccess.metadata.MdTransientDAO;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestSuite;

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
public class TransientAttributeMultiReferenceTest extends AbstractTransientAttributeMultiReferenceTest
{
  private static MdTermDAO                    mdTerm;

  private static BusinessDAO                  defaultValue;

  private static MdTransientDAO               mdView;

  private static MdAttributeMultiReferenceDAO mdAttributeMultiReference;

  public static Test suite()
  {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(TransientAttributeMultiReferenceTest.class);

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
  * 
  */
  public static void classTearDown()
  {
    TestFixtureFactory.delete(mdView);
    TestFixtureFactory.delete(mdTerm);
  }

  /**
  * 
  */
  public static void classSetUp()
  {
    mdTerm = TestFixtureFactory.createMdTerm();
    mdTerm.setGenerateMdController(false);
    mdTerm.setValue(MdTermInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdTerm.apply();

    defaultValue = BusinessDAO.newInstance(mdTerm.definesType());
    defaultValue.setStructValue(TermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Term 1");
    defaultValue.apply();

    mdView = TestFixtureFactory.createMdView1();
    mdView.setValue(MdViewInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdView.apply();

    mdAttributeMultiReference = MdAttributeMultiReferenceDAO.newInstance();
    mdAttributeMultiReference.setValue(MdAttributeMultiReferenceInfo.NAME, "testMultiReference");
    mdAttributeMultiReference.setStructValue(MdAttributeMultiReferenceInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Term Test");
    mdAttributeMultiReference.setValue(MdAttributeMultiReferenceInfo.REF_MD_ENTITY, mdTerm.getId());
    mdAttributeMultiReference.setValue(MdAttributeMultiReferenceInfo.DEFINING_MD_CLASS, mdView.getId());
    mdAttributeMultiReference.setValue(MdAttributeMultiReferenceInfo.DEFAULT_VALUE, defaultValue.getId());
    mdAttributeMultiReference.apply();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.AbstractTransientAttributeMultiReferenceTest#
   * getMdAttribute()
   */
  @Override
  public MdAttributeMultiReferenceDAO getMdAttribute()
  {
    return mdAttributeMultiReference;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.dataaccess.AbstractTransientAttributeMultiReferenceTest#getMdView
   * ()
   */
  @Override
  public MdTransientDAO getMdTransient()
  {
    return mdView;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.dataaccess.AbstractTransientAttributeMultiReferenceTest#getMdTerm
   * ()
   */
  @Override
  public MdTermDAO getMdTerm()
  {
    return mdTerm;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.AbstractTransientAttributeMultiReferenceTest#
   * getDefaultValue()
   */
  @Override
  public BusinessDAO getDefaultValue()
  {
    return defaultValue;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.AbstractTransientAttributeMultiReferenceTest#
   * getAttribute(com.runwaysdk.dataaccess.BusinessDAO)
   */
  @Override
  public AttributeMultiReferenceIF getAttribute(BusinessDAO business)
  {
    return (AttributeMultiReferenceIF) business.getAttributeIF(this.getMdAttribute().definesAttribute());
  }
}
