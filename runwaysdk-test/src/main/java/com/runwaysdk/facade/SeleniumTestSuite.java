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
package com.runwaysdk.facade;

import com.runwaysdk.constants.TestProperties;

import junit.framework.Test;
import junit.framework.TestSuite;

public class SeleniumTestSuite extends TestSuite
{
  public static final String FIREFOX = "firefox";
  
  public static final String OPERA = "opera";
  
  public static final String IEXPLORE = "iexplore";

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(SeleniumTestSuite.suite());
  }

  public static Test suite()
  {
    TestSuite suite = new TestSuite();
    
    String[] browsers = TestProperties.getSeleniumBrowser();
    for(String browser : browsers)
    {
      if(browser.contains(FIREFOX))
        suite.addTestSuite(FirefoxSeleniumTest.class);
      else if(browser.contains(OPERA))
        suite.addTestSuite(OperaSeleniumTest.class);
      else if(browser.contains(IEXPLORE))
        suite.addTestSuite(IESeleniumTest.class);
    }
    
    return suite;
  }
}
