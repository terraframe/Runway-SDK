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
import org.junit.BeforeClass;
import org.junit.Test;

import com.runwaysdk.session.Request;

public class MdMobileFormTest extends MdFormDAOTest
{
  @Request
  @BeforeClass
  public static void classSetUp()
  {
    defineMdClasses();
  }

  @Request
  @AfterClass
  public static void classTearDown()
  {
    destroyMdClasses();
  }

  /**
   * Tests the metadata on the MdForm instance.
   */
  @Request
  @Test
  public void testFormMetadata()
  {

  }

  /**
   * Tests the relationships between the form and its fields.
   */
  @Request
  @Test
  public void testFields()
  {

  }

  /**
   * Tests the character field metadata.
   */
  @Request
  @Test
  public void testCharacter()
  {

  }
}
