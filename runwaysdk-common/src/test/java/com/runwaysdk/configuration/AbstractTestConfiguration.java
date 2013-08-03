package com.runwaysdk.configuration;
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

import static org.junit.Assert.assertEquals;

import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import com.runwaysdk.configuration.ConfigurationManager.ConfigType;
import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.DeployProperties;
import com.runwaysdk.constants.LocalProperties;

abstract public class AbstractTestConfiguration
{
  abstract ConfigType getConfigType();
  
  @Before
  public void setUp() {
    ConfigurationManager.Singleton.INSTANCE.setConfigType(getConfigType());
  }
  
  @Test
  public void testCommonProperties() {
    int inty = CommonProperties.getRMIPort();
    Locale locale = CommonProperties.getDefaultLocale();
    
    assertEquals(1199, inty);
    assertEquals(Locale.ENGLISH, locale);
  }
  
  @Test
  public void testDeployProperties() {
    String password = DeployProperties.getContainerPassword();
    String url = DeployProperties.getApplicationURL();
    
    assertEquals("framework", password);
    assertEquals("testValue", url);
  }
  
  @Test
  public void testLocalProperties() {
    String src = LocalProperties.getCommonSrc();
    String jspDir = LocalProperties.getJspDir();
    
    assertEquals("testValue", src);
    assertEquals("testValue", jspDir);
  }
}
