/*******************************************************************************
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
 ******************************************************************************/
package com.runwaysdk.business;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestResult;
import junit.framework.TestSuite;

import com.runwaysdk.constants.MdUtilInfo;
import com.runwaysdk.dataaccess.metadata.MdSessionDAO;
import com.runwaysdk.dataaccess.metadata.MdUtilDAO;

public class UtilGenTest extends SessionComponentGenTest
{
  @Override
  public TestResult run()
  {
    return super.run();
  }

  @Override
  public void run(TestResult testResult)
  {
    super.run(testResult);
  }
  
  public static Test suite()
  {
    TestSuite suite = new TestSuite(UtilGenTest.class.getSimpleName());
    suite.addTestSuite(UtilGenTest.class);

    TestSetup wrapper = new TestSetup(suite)
    {
      protected void setUp()
      {
        collection = MdUtilDAO.newInstance();      
        superMdSessionAttributeNameHack = MdUtilInfo.SUPER_MD_UTIL;
        collectionSub = MdUtilDAO.newInstance();
        classSetUp();
      }

      protected void tearDown()
      {
        classTearDown();
      }
    };

    return wrapper;
  }

  protected MdSessionDAO newMdSession()
  {
    return MdUtilDAO.newInstance();
  }
  
  protected String buildGetMethod()
  {
    return "return (Collection) "+Util.class.getName()+".get(id);\n";
  }
}
