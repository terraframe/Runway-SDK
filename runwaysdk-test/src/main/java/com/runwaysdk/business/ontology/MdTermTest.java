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
package com.runwaysdk.business.ontology;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.runwaysdk.constants.EntityCacheMaster;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdTermInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.MdTermDAOIF;
import com.runwaysdk.dataaccess.metadata.MdTermDAO;
import com.runwaysdk.session.Request;

public class MdTermTest
{
  private static MdTermDAO mdTerm;

  /**
   * Set the testObject to a new Instance of the TEST type.
   */
  @Request
  @BeforeClass
  public static void classSetUp()
  {
    mdTerm = MdTermDAO.newInstance();
    mdTerm.setValue(MdTermInfo.NAME, "Term");
    mdTerm.setValue(MdTermInfo.PACKAGE, "com.test");
    mdTerm.setStructValue(MdTermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "JUnit Test Class");
    mdTerm.setStructValue(MdTermInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Test Class");
    mdTerm.setValue(MdTermInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdTerm.setValue(MdTermInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    mdTerm.setValue(MdTermInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_NOTHING.getId());
    mdTerm.setValue(MdTermInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdTerm.apply();
  }

  /**
   * If testObject was applied, it is removed from the database.
   * 
   * 
   */
  @Request
  @AfterClass
  public static void classTearDown()
  {
    mdTerm.delete();
  }

  @Request
  @Test
  public void testCreateAndGetMdTerm()
  {
    MdTermDAOIF result = MdTermDAO.getMdTermDAO(mdTerm.definesType());

    Assert.assertNotNull(result);
    Assert.assertEquals(result.getValue(MdTermInfo.NAME), mdTerm.getValue(MdTermInfo.NAME));
    Assert.assertEquals(result.getValue(MdTermInfo.PACKAGE), mdTerm.getValue(MdTermInfo.PACKAGE));
    Assert.assertEquals(result.getStructValue(MdTermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), mdTerm.getStructValue(MdTermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE));
    Assert.assertEquals(result.getStructValue(MdTermInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE), mdTerm.getStructValue(MdTermInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE));
  }

  @Request
  @Test
  public void testTermInstance()
  {
    BusinessDAO term = BusinessDAO.newInstance(mdTerm.definesType());
    term.setStructValue(MdTermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "expected value");
    term.apply();

    try
    {
      Assert.assertEquals("expected value", BusinessDAO.get(term.getId()).getStructValue(MdTermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE));
    }
    finally
    {
      term.delete();
    }
  }
}
