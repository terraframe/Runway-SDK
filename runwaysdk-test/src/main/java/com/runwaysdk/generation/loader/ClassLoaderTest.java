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
package com.runwaysdk.generation.loader;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;

import com.runwaysdk.ConfigurationException;
import com.runwaysdk.business.ClassLoaderException;
import com.runwaysdk.configuration.RunwayConfigurationException;
import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.util.IDGenerator;

public class ClassLoaderTest extends TestCase
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
    TestSuite suite = new TestSuite();
    suite.addTestSuite(ClassLoaderTest.class);

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
   * The setup done before the test suite is run
   */
  public static void classSetUp()
  {

  }

  /**
   * The tear down done after all the test in the test suite have run
   */
  public static void classTearDown()
  {
  }

  protected void tearDown() throws Exception
  {
  }
  
  public void testValidURL()
  {
    try
    {
      File clientBin = new File(LocalProperties.getClientGenBin());
      File commonBin = new File(LocalProperties.getCommonGenBin());
      File serverBin = new File(LocalProperties.getServerGenBin());
      
      assertTrue(clientBin.exists());
      assertTrue(commonBin.exists());
      assertTrue(serverBin.exists());
      
      LoaderDecorator.urlArray(clientBin, commonBin, serverBin);
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }
  
  public void testInvalidURL()
  {
    File invalidFile = new File("");
    
    assertFalse(invalidFile.exists());

    try
    {      
      LoaderDecorator.urlArray(invalidFile);
      
      fail("Expected an exception when entering in bad bin directories");      
    }
    catch (ClassLoaderException e)
    {
      // This is expected
    }    
  }

  public void testValidProperties()
  {
    try
    {
      File clientBin = new File(LocalProperties.getClientGenBin());
      File commonBin = new File(LocalProperties.getCommonGenBin());
      File serverBin = new File(LocalProperties.getServerGenBin());
      
      assertTrue(clientBin.exists());
      assertTrue(commonBin.exists());
      assertTrue(serverBin.exists());
      
      URL[] url = new URL[] { clientBin.toURI().toURL(), commonBin.toURI().toURL(), serverBin.toURI().toURL() };

      new RunwayClassLoader(url, null, false, clientBin, commonBin, serverBin);
    }
    catch (ConfigurationException e)
    {
      fail(e.getMessage());
    }
    catch (MalformedURLException e)
    {
      e.printStackTrace();
      
      fail(e.getLocalizedMessage());
    }
  }

  public void testInvalidClientProperties()
  {
    try
    {
      File clientBin = new File(LocalProperties.getClientGenBin());
      File commonBin = new File(LocalProperties.getCommonGenBin());
      File serverBin = new File(LocalProperties.getServerGenBin());
      File invalidFile = new File(IDGenerator.nextID());
            
      assertFalse(invalidFile.exists());
      
      URL[] url = new URL[] { clientBin.toURI().toURL(), commonBin.toURI().toURL(), serverBin.toURI().toURL() };

      new RunwayClassLoader(url, null, false, invalidFile, commonBin, serverBin);

      fail("Invalid properties throw bad error in RunwayClassLoader");
    }
    catch (ConfigurationException e)
    {
      // this is expected
    }
    catch (MalformedURLException e)
    {
      e.printStackTrace();
      
      fail(e.getLocalizedMessage());
    }
  }

  public void testInvalidServerProperties()
  {
    try
    {
      File clientBin = new File(LocalProperties.getClientGenBin());
      File commonBin = new File(LocalProperties.getCommonGenBin());
      File serverBin = new File(LocalProperties.getServerGenBin());
      File invalidFile = new File(IDGenerator.nextID());
            
      assertFalse(invalidFile.exists());
      
      URL[] url = new URL[] { clientBin.toURI().toURL(), commonBin.toURI().toURL(), serverBin.toURI().toURL() };

      new RunwayClassLoader(url, null, false, clientBin, commonBin, invalidFile);

      fail("Invalid properties throw bad error in RunwayClassLoader");
    }
    catch (ConfigurationException e)
    {
      // this is expected
    }
    catch (MalformedURLException e)
    {
      e.printStackTrace();
      
      fail(e.getLocalizedMessage());
    }
  }

  public void testInvalidCommonProperties()
  {
    try
    {
      File clientBin = new File(LocalProperties.getClientGenBin());
      File commonBin = new File(LocalProperties.getCommonGenBin());
      File serverBin = new File(LocalProperties.getServerGenBin());
      File invalidFile = new File(IDGenerator.nextID());
            
      assertFalse(invalidFile.exists());
      
      URL[] url = new URL[] { clientBin.toURI().toURL(), commonBin.toURI().toURL(), serverBin.toURI().toURL() };

      new RunwayClassLoader(url, null, false, clientBin, invalidFile, serverBin);

      fail("Invalid properties throw bad error in RunwayClassLoader");
    }
    catch (RunwayConfigurationException e)
    {
      // this is expected
    }
    catch (MalformedURLException e)
    {
      e.printStackTrace();
      
      fail(e.getLocalizedMessage());
    }
  }

}
