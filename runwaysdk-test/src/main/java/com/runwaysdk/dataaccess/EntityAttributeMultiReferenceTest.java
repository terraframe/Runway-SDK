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
package com.runwaysdk.dataaccess;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdAttributeMultiReferenceInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.MdTermInfo;
import com.runwaysdk.constants.TermInfo;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.metadata.MdAttributeMultiReferenceDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdTermDAO;
import com.runwaysdk.session.Request;

public class EntityAttributeMultiReferenceTest extends AbstractEntityAttributeMultiReferenceTest
{
  private static MdTermDAO                    mdTerm;

  private static BusinessDAO                  defaultValue;

  private static MdBusinessDAO                mdBusiness;

  private static MdAttributeMultiReferenceDAO mdAttributeMultiReference;

  /**
  * 
  */
  @Request
  @AfterClass
  public static void classTearDown()
  {
    TestFixtureFactory.delete(mdBusiness);
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
    mdTerm.setValue(MdTermInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdTerm.apply();

    defaultValue = BusinessDAO.newInstance(mdTerm.definesType());
    defaultValue.setStructValue(TermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Term");
    defaultValue.apply();

    mdBusiness = TestFixtureFactory.createMdBusiness1();
    mdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusiness.apply();

    mdAttributeMultiReference = MdAttributeMultiReferenceDAO.newInstance();
    mdAttributeMultiReference.setValue(MdAttributeMultiReferenceInfo.NAME, "testMultiReference");
    mdAttributeMultiReference.setStructValue(MdAttributeMultiReferenceInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Term Test");
    mdAttributeMultiReference.setValue(MdAttributeMultiReferenceInfo.REF_MD_ENTITY, mdTerm.getOid());
    mdAttributeMultiReference.setValue(MdAttributeMultiReferenceInfo.DEFINING_MD_CLASS, mdBusiness.getOid());
    mdAttributeMultiReference.setValue(MdAttributeMultiReferenceInfo.DEFAULT_VALUE, defaultValue.getOid());
    mdAttributeMultiReference.apply();
  }

  public MdAttributeMultiReferenceDAO getMdAttribute()
  {
    return mdAttributeMultiReference;
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

  public AttributeMultiReferenceIF getAttribute(BusinessDAO business)
  {
    return (AttributeMultiReferenceIF) business.getAttributeIF(this.getMdAttribute().definesAttribute());
  }

}
