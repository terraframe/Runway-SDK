/**
 * 
 */
package com.runwaysdk.classloader;

import java.io.IOException;
import java.net.URL;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.runwaysdk.configuration.ConfigurationManager;
import com.runwaysdk.configuration.ConfigurationManager.ConfigGroup;
import com.runwaysdk.configuration.ConfigurationManager.ConfigType;
import com.runwaysdk.constants.BusinessDTOInfo;
import com.runwaysdk.generation.loader.LoaderDecorator;

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
    ConfigurationManager.setConfigType(ConfigType.COMMONS_CONFIG);
  }
  
  @After
  public void tearDown() {
    ConfigurationManager.getInMemoryConfigurator().clear();
  }
  
  @Test
  public void testBasicLoad() {
    ConfigurationManager.getInMemoryConfigurator().setProperty("local.bin", "aBadValue");
    ConfigurationManager.getInMemoryConfigurator().setProperty("classloader.reloadable.enabled", "false");
    LoaderDecorator.load(BusinessDTOInfo.CLASS);
    
    ConfigurationManager.getInMemoryConfigurator().setProperty("local.bin", "${project.basedir}/target/classes");
    ConfigurationManager.getInMemoryConfigurator().setProperty("classloader.reloadable.enabled", "true");
    LoaderDecorator.load(BusinessDTOInfo.CLASS);
  }
}
