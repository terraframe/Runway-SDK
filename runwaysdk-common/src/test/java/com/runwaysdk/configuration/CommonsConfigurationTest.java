package com.runwaysdk.configuration;
import static org.junit.Assert.assertEquals;

import org.apache.commons.configuration.Configuration;
import org.junit.Test;

import com.runwaysdk.configuration.ConfigurationManager.ConfigType;
import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.LocalProperties;

/**
 * 
 */

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
public class CommonsConfigurationTest extends AbstractTestConfiguration
{
  @Override
  ConfigType getConfigType()
  {
    return ConfigType.COMMONS_CONFIG;
  }
  
  @Test
  public void testValueReplace() {
    String timeZone = CommonProperties.getJSONRMIService();
    
    assertEquals("testValue/testValue2/testValue3", timeZone);
  }
  
  @Test
  public void testSimpleInheritance() {
    String clientBin = LocalProperties.getClientBin();
    
    assertEquals("testValue", clientBin);
  }
  
  @Test
  public void testInheritance() {
    String serverBin = LocalProperties.getServerBin();
    
    assertEquals("testValue/testValue2/testValue3", serverBin);
  }
  
  @Test
  public void testInMemoryConfigurator() {
    Configuration bc = ConfigurationManager.getInMemoryConfigurator();
    bc.setProperty("test.prop.two", "overridden");
    
    String timeZone = CommonProperties.getJSONRMIService();
    
    assertEquals("overridden/testValue3", timeZone);
    
    bc.setProperty("test.prop.two", "${test.prop.one}/testValue2");
  }
}
