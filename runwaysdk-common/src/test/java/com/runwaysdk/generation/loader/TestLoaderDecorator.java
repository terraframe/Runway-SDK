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
/**
 * 
 */
package com.runwaysdk.generation.loader;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.runwaysdk.configuration.CommonsConfigurationResolver;
import com.runwaysdk.configuration.ConfigurationManager;
import com.runwaysdk.configuration.InMemoryConfigurator;
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
  private InMemoryConfigurator inMemConfig = null;
  
  @Before
  public void setUp() {
    ConfigurationManager.setConfigResolver(new CommonsConfigurationResolver());
    
    inMemConfig = CommonsConfigurationResolver.getInMemoryConfigurator();
    inMemConfig.setProperty("common.src", "${generated.root}/common");
    inMemConfig.setProperty("server.bin", "${generated.root}/server/bin");
    inMemConfig.setProperty("client.bin", "${generated.root}/client/bin");
  }
  
  @After
  public void tearDown() {
    inMemConfig.clear();
  }
  
  @Test
  public void testEnabled() {
    inMemConfig.setProperty("classloader..enabled", "true");
    
    
    inMemConfig.setProperty("local.bin", "${project.basedir}/target/classes");
    
    inMemConfig.setProperty("environment", LocalProperties.DEVELOP);
    
    LoaderDecorator.load(BusinessDTOInfo.CLASS);
    
    inMemConfig.setProperty("environment", LocalProperties.DEPLOY);
    
    LoaderDecorator.load(BusinessDTOInfo.CLASS);
  }
  
  @Test
  public void testDisabled() {
    inMemConfig.setProperty("classloader..enabled", "false");
    
    
    inMemConfig.setProperty("local.bin", "aBadValue");
    
    inMemConfig.setProperty("environment", LocalProperties.DEVELOP);
    
    LoaderDecorator.load(BusinessDTOInfo.CLASS);
    
    inMemConfig.setProperty("environment", LocalProperties.DEPLOY);
    inMemConfig.setProperty("local.bin", "aBadValue");
    
    LoaderDecorator.load(BusinessDTOInfo.CLASS);
  }
}
