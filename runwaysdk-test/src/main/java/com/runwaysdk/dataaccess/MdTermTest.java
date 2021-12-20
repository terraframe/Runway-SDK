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

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.runwaysdk.constants.EntityCacheMaster;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdTermInfo;
import com.runwaysdk.dataaccess.metadata.MdTermDAO;
import com.runwaysdk.session.Request;

public class MdTermTest
{
  /**
   * Set the testObject to a new Instance of the TEST type.
   */
  @Request
  @BeforeClass
  public static void classSetUp()
  {
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
  }

  @Request
  @Test
  public void testCreateAndGetMdTerm()
  {
    MdTermDAO mdTerm = MdTermDAO.newInstance();
    mdTerm.setValue(MdTermInfo.NAME, "Term");
    mdTerm.setValue(MdTermInfo.PACKAGE, "com.test");
    mdTerm.setStructValue(MdTermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "JUnit Test Class");
    mdTerm.setStructValue(MdTermInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Test Class");
    mdTerm.setValue(MdTermInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdTerm.setValue(MdTermInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    mdTerm.setValue(MdTermInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_NOTHING.getOid());
    mdTerm.apply();

    try
    {
      MdTermDAOIF result = MdTermDAO.getMdTermDAO(mdTerm.definesType());

      Assert.assertNotNull(result);
      Assert.assertEquals(result.getValue(MdTermInfo.NAME), mdTerm.getValue(MdTermInfo.NAME));
      Assert.assertEquals(result.getValue(MdTermInfo.PACKAGE), mdTerm.getValue(MdTermInfo.PACKAGE));
      Assert.assertEquals(result.getStructValue(MdTermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), mdTerm.getStructValue(MdTermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE));
      Assert.assertEquals(result.getStructValue(MdTermInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE), mdTerm.getStructValue(MdTermInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE));
    }
    finally
    {
      mdTerm.delete();
    }
  }

  @Request
  @Test
  public void testCreateMultiples()
  {
    MdTermDAO mdTerm1 = MdTermDAO.newInstance();
    mdTerm1.setValue(MdTermInfo.NAME, "Term1");
    mdTerm1.setValue(MdTermInfo.PACKAGE, "com.test");
    mdTerm1.setStructValue(MdTermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "JUnit Test Class");
    mdTerm1.setStructValue(MdTermInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Test Class");
    mdTerm1.setValue(MdTermInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdTerm1.setValue(MdTermInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    mdTerm1.setValue(MdTermInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_NOTHING.getOid());
    mdTerm1.apply();

    try
    {
      MdTermDAO mdTerm2 = MdTermDAO.newInstance();
      mdTerm2.setValue(MdTermInfo.NAME, "Term2");
      mdTerm2.setValue(MdTermInfo.PACKAGE, "com.test");
      mdTerm2.setStructValue(MdTermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "JUnit Test Class");
      mdTerm2.setStructValue(MdTermInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Test Class");
      mdTerm2.setValue(MdTermInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
      mdTerm2.setValue(MdTermInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
      mdTerm2.setValue(MdTermInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_NOTHING.getOid());
      mdTerm2.apply();

      try
      {
        MdTermDAOIF result = MdTermDAO.getMdTermDAO(mdTerm2.definesType());

        Assert.assertNotNull(result);
        Assert.assertEquals(result.getValue(MdTermInfo.NAME), mdTerm2.getValue(MdTermInfo.NAME));
        Assert.assertEquals(result.getValue(MdTermInfo.PACKAGE), mdTerm2.getValue(MdTermInfo.PACKAGE));
        Assert.assertEquals(result.getStructValue(MdTermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), mdTerm2.getStructValue(MdTermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE));
        Assert.assertEquals(result.getStructValue(MdTermInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE), mdTerm2.getStructValue(MdTermInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE));
      }
      finally
      {
        mdTerm2.delete();
      }
    }
    finally
    {
      mdTerm1.delete();
    }
  }
}
