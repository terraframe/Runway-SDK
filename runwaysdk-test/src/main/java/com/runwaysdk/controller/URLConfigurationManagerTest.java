/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
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
package com.runwaysdk.controller;

import java.io.InputStream;

import org.junit.Assert;
import org.junit.Test;

import com.runwaysdk.controller.URLConfigurationManager.ControllerWrapper;
import com.runwaysdk.controller.URLConfigurationManager.ControllerMapping;
import com.runwaysdk.controller.URLConfigurationManager.ControllerMapping.ActionMapping;
import com.runwaysdk.controller.URLConfigurationManager.UriForwardMapping;
import com.runwaysdk.controller.URLConfigurationManager.UriMapping;
import com.runwaysdk.mvc.MockController;
import com.runwaysdk.session.Request;

public class URLConfigurationManagerTest
{
  @Request
  @Test
  public void testRedirectMapping() throws Exception
  {
    InputStream istream = this.getClass().getResourceAsStream("/testmap.xml");

    Assert.assertNotNull(istream);

    try
    {
      URLConfigurationManager manager = new URLConfigurationManager();
      manager.clear();
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

  @Request
  @Test
  public void testUrlMapping() throws Exception
  {
    InputStream istream = this.getClass().getResourceAsStream("/testmap.xml");

    Assert.assertNotNull(istream);

    try
    {
      URLConfigurationManager manager = new URLConfigurationManager();
      manager.clear();
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

  @Request
  @Test
  public void testUrlAction() throws Exception
  {
    InputStream istream = this.getClass().getResourceAsStream("/testmap.xml");

    Assert.assertNotNull(istream);

    try
    {
      URLConfigurationManager manager = new URLConfigurationManager();
      manager.clear();
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

  @Request
  @Test
  public void testControllerWithBaseAndVersion() throws Exception
  {
    ControllerWrapper wrapper = new ControllerWrapper("api", "v1", "user");
    ControllerMapping sourceMapping = new ControllerMapping(wrapper, MockController.class.getName(), ControllerVersion.V2);
    sourceMapping.add("generateInteger", "generate", ControllerVersion.V2);
    
    URLConfigurationManager manager = new URLConfigurationManager();
    manager.clear();
    manager.addMapping(sourceMapping);

    UriMapping testMapping = manager.getMapping("api/v1/user/generate");

    Assert.assertNotNull(testMapping);
    Assert.assertTrue( ( testMapping instanceof ActionMapping ));

    ActionMapping actionMapping = (ActionMapping) testMapping;
    Assert.assertEquals("generate", actionMapping.getUri());
    Assert.assertEquals("generateInteger", actionMapping.getMethodName());
    Assert.assertEquals(ControllerVersion.V2, actionMapping.getVersion());

    ControllerMapping controllerMapping = actionMapping.getControllerMapping();
    Assert.assertEquals("api/v1/user", controllerMapping.getUri());
    Assert.assertEquals(MockController.class.getName(), controllerMapping.getControllerClassName());
  }
  
  @Request
  @Test
  public void testControllerWithBase() throws Exception
  {
    ControllerWrapper wrapper = new ControllerWrapper("api", "", "user");
    ControllerMapping sourceMapping = new ControllerMapping(wrapper, MockController.class.getName(), ControllerVersion.V2);
    sourceMapping.add("generateInteger", "generate", ControllerVersion.V2);
    
    URLConfigurationManager manager = new URLConfigurationManager();
    manager.clear();
    manager.addMapping(sourceMapping);
    
    UriMapping testMapping = manager.getMapping("api/user/generate");
    
    Assert.assertNotNull(testMapping);
    Assert.assertTrue( ( testMapping instanceof ActionMapping ));
    
    ActionMapping actionMapping = (ActionMapping) testMapping;
    Assert.assertEquals("generate", actionMapping.getUri());
    Assert.assertEquals("generateInteger", actionMapping.getMethodName());
    Assert.assertEquals(ControllerVersion.V2, actionMapping.getVersion());
    
    ControllerMapping controllerMapping = actionMapping.getControllerMapping();
    Assert.assertEquals("api/user", controllerMapping.getUri());
    Assert.assertEquals(MockController.class.getName(), controllerMapping.getControllerClassName());
  }
  
  @Request
  @Test
  public void testBasicController() throws Exception
  {
    ControllerWrapper wrapper = new ControllerWrapper("user");
    ControllerMapping sourceMapping = new ControllerMapping(wrapper, MockController.class.getName(), ControllerVersion.V2);
    sourceMapping.add("generateInteger", "generate", ControllerVersion.V2);
    
    URLConfigurationManager manager = new URLConfigurationManager();
    manager.clear();
    manager.addMapping(sourceMapping);
    
    UriMapping testMapping = manager.getMapping("user/generate");
    
    Assert.assertNotNull(testMapping);
    Assert.assertTrue( ( testMapping instanceof ActionMapping ));
    
    ActionMapping actionMapping = (ActionMapping) testMapping;
    Assert.assertEquals("generate", actionMapping.getUri());
    Assert.assertEquals("generateInteger", actionMapping.getMethodName());
    Assert.assertEquals(ControllerVersion.V2, actionMapping.getVersion());
    
    ControllerMapping controllerMapping = actionMapping.getControllerMapping();
    Assert.assertEquals("user", controllerMapping.getUri());
    Assert.assertEquals(MockController.class.getName(), controllerMapping.getControllerClassName());
  }
}