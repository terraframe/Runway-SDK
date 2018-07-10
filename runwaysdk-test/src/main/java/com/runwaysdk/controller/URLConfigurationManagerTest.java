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
package com.runwaysdk.controller;

import java.io.InputStream;

import org.junit.Assert;

import com.runwaysdk.controller.URLConfigurationManager.ControllerMapping;
import com.runwaysdk.controller.URLConfigurationManager.ControllerMapping.ActionMapping;
import com.runwaysdk.controller.URLConfigurationManager.UriForwardMapping;
import com.runwaysdk.controller.URLConfigurationManager.UriMapping;
import com.runwaysdk.mvc.MockController;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;

public class URLConfigurationManagerTest extends TestCase
{
  public static Test suite()
  {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(URLConfigurationManagerTest.class);

    TestSetup wrapper = new TestSetup(suite);

    return wrapper;
  }

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

  public void testRedirectMapping() throws Exception
  {
    InputStream istream = this.getClass().getResourceAsStream("/testmap.xml");

    Assert.assertNotNull(istream);

    try
    {
      URLConfigurationManager manager = new URLConfigurationManager();
      manager.readMappings(istream);

      UriMapping mapping = manager.getMapping("");

      Assert.assertNotNull(mapping);
      Assert.assertTrue( ( mapping instanceof UriForwardMapping ));

      UriForwardMapping forward = (UriForwardMapping) mapping;
      Assert.assertEquals("", forward.getUri());
      Assert.assertEquals("/index.jsp", forward.getUriEnd());
      Assert.assertEquals(ControllerVersion.V1, forward.getVersion());
    }
    finally
    {
      istream.close();
    }
  }

  public void testUrlMapping() throws Exception
  {
    InputStream istream = this.getClass().getResourceAsStream("/testmap.xml");

    Assert.assertNotNull(istream);

    try
    {
      URLConfigurationManager manager = new URLConfigurationManager();
      manager.readMappings(istream);

      UriMapping mapping = manager.getMapping("test/testMethod");

      Assert.assertNotNull(mapping);
      Assert.assertTrue( ( mapping instanceof ActionMapping ));

      ActionMapping actionMapping = (ActionMapping) mapping;
      Assert.assertEquals("testMethod", actionMapping.getUri());
      Assert.assertEquals("testMethod", actionMapping.getMethodName());
      Assert.assertEquals(ControllerVersion.V2, actionMapping.getVersion());

      ControllerMapping controllerMapping = actionMapping.getControllerMapping();
      Assert.assertEquals("test", controllerMapping.getUri());
      Assert.assertEquals(MockController.class.getName(), controllerMapping.getControllerClassName());
    }
    finally
    {
      istream.close();
    }
  }

  public void testUrlAction() throws Exception
  {
    InputStream istream = this.getClass().getResourceAsStream("/testmap.xml");

    Assert.assertNotNull(istream);

    try
    {
      URLConfigurationManager manager = new URLConfigurationManager();
      manager.readMappings(istream);

      UriMapping mapping = manager.getMapping("test/generate");

      Assert.assertNotNull(mapping);
      Assert.assertTrue( ( mapping instanceof ActionMapping ));

      ActionMapping actionMapping = (ActionMapping) mapping;
      Assert.assertEquals("generate", actionMapping.getUri());
      Assert.assertEquals("generateInteger", actionMapping.getMethodName());
      Assert.assertEquals(ControllerVersion.V2, actionMapping.getVersion());

      ControllerMapping controllerMapping = actionMapping.getControllerMapping();
      Assert.assertEquals("test", controllerMapping.getUri());
      Assert.assertEquals(MockController.class.getName(), controllerMapping.getControllerClassName());
    }
    finally
    {
      istream.close();
    }
  }
}