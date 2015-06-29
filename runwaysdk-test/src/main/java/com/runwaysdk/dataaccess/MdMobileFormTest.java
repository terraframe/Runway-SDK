/**
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved.
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

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestSuite;

public class MdMobileFormTest extends MdFormDAOTest
{
  public static Test suite()
  {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(MdMobileFormTest.class);

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
  
  protected static void classSetUp()
  {
    defineMdClasses();
  }

  protected static void classTearDown()
  {
    destroyMdClasses();
  }
  
  /**
   * Tests the metadata on the MdForm instance.
   */
  public void testFormMetadata()
  {
    
  }
  
  /**
   * Tests the relationships between the form and its fields.
   */
  public void testFields()
  {
    
  }
  
  /**
   * Tests the character field metadata.
   */
  public void testCharacter()
  {
    
  }
}
