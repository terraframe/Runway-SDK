/**
*
*/
package com.runwaysdk.dataaccess;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestSuite;

import com.runwaysdk.business.ontology.MdTermDAO;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdAttributeMultiTermInfo;
import com.runwaysdk.constants.TermInfo;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.metadata.MdAttributeMultiReferenceDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeMultiTermDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;

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
public class EntityAttributeMultiTermTest extends AbstractEntityAttributeMultiReferenceTest
{
  private static MdTermDAO               mdTerm;

  private static BusinessDAO             defaultValue;

  private static MdBusinessDAO           mdBusiness;

  private static MdAttributeMultiTermDAO mdAttributeMultiTerm;

  public static Test suite()
  {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(EntityAttributeMultiTermTest.class);

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
    TestFixtureFactory.delete(mdBusiness);
    TestFixtureFactory.delete(mdTerm);
  }

  /**
  * 
  */
  public static void classSetUp()
  {
    mdTerm = TestFixtureFactory.createMdTerm();
    mdTerm.apply();

    defaultValue = BusinessDAO.newInstance(mdTerm.definesType());
    defaultValue.setStructValue(TermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Term");
    defaultValue.apply();

    mdBusiness = TestFixtureFactory.createMdBusiness1();
    mdBusiness.apply();

    mdAttributeMultiTerm = MdAttributeMultiTermDAO.newInstance();
    mdAttributeMultiTerm.setValue(MdAttributeMultiTermInfo.NAME, "testMultiTerm");
    mdAttributeMultiTerm.setStructValue(MdAttributeMultiTermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Term Test");
    mdAttributeMultiTerm.setValue(MdAttributeMultiTermInfo.REF_MD_ENTITY, mdTerm.getId());
    mdAttributeMultiTerm.setValue(MdAttributeMultiTermInfo.DEFINING_MD_CLASS, mdBusiness.getId());
    mdAttributeMultiTerm.setValue(MdAttributeMultiTermInfo.DEFAULT_VALUE, defaultValue.getId());
    mdAttributeMultiTerm.apply();
  }

  public MdAttributeMultiReferenceDAO getMdAttribute()
  {
    return mdAttributeMultiTerm;
  }

  public MdBusinessDAO getMdBusiness()
  {
    return mdBusiness;
  }

  public MdTermDAO getMdTerm()
  {
    return mdTerm;
  }

  public BusinessDAO getDefaultValue()
  {
    return defaultValue;
  }

  public AttributeMultiTermIF getAttribute(BusinessDAO business)
  {
    return (AttributeMultiTermIF) business.getAttributeIF(this.getMdAttribute().definesAttribute());
  }
}
