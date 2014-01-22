/**
 * 
 */
package com.runwaysdk.generation.loader;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.runwaysdk.configuration.ConfigurationManager;
import com.runwaysdk.configuration.ConfigurationManager.ConfigResolver;
import com.runwaysdk.constants.BusinessDTOInfo;
import com.runwaysdk.constants.LocalProperties;

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
public class TestLoaderDecorator
{
  @Before
  public void setUp() {
    ConfigurationManager.setConfigResolver(ConfigResolver.COMMONS_CONFIG);
    
    ConfigurationManager.getInMemoryConfigurator().setProperty("common.src", "${generated.root}/common");
    ConfigurationManager.getInMemoryConfigurator().setProperty("server.bin", "${generated.root}/server/bin");
    ConfigurationManager.getInMemoryConfigurator().setProperty("client.bin", "${generated.root}/client/bin");
  }
  
  @After
  public void tearDown() {
    ConfigurationManager.getInMemoryConfigurator().clear();
  }
  
  @Test
  public void testReloadableEnabled() {
    ConfigurationManager.getInMemoryConfigurator().setProperty("classloader.reloadable.enabled", "true");
    
    
    ConfigurationManager.getInMemoryConfigurator().setProperty("local.bin", "${project.basedir}/target/classes");
    
    ConfigurationManager.getInMemoryConfigurator().setProperty("environment", LocalProperties.DEVELOP);
    LoaderDecorator.reload();
    LoaderDecorator.load(BusinessDTOInfo.CLASS);
    
    ConfigurationManager.getInMemoryConfigurator().setProperty("environment", LocalProperties.DEPLOY);
    LoaderDecorator.reload();
    LoaderDecorator.load(BusinessDTOInfo.CLASS);
  }
  
  @Test
  public void testReloadableDisabled() {
    ConfigurationManager.getInMemoryConfigurator().setProperty("classloader.reloadable.enabled", "false");
    
    
    ConfigurationManager.getInMemoryConfigurator().setProperty("local.bin", "aBadValue");
    
    ConfigurationManager.getInMemoryConfigurator().setProperty("environment", LocalProperties.DEVELOP);
    LoaderDecorator.reload();
    LoaderDecorator.load(BusinessDTOInfo.CLASS);
    
    ConfigurationManager.getInMemoryConfigurator().setProperty("environment", LocalProperties.DEPLOY);
    ConfigurationManager.getInMemoryConfigurator().setProperty("local.bin", "aBadValue");
    LoaderDecorator.reload();
    LoaderDecorator.load(BusinessDTOInfo.CLASS);
  }
}
