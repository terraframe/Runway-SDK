package com.runwaysdk.configuration;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.runwaysdk.configuration.ConfigurationManager.ConfigResolver;
import com.runwaysdk.constants.ServerProperties;

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
  ConfigResolver getConfigResolver()
  {
    return ConfigResolver.COMMONS_CONFIG;
  }
  
  @Test
  public void testServerProperties() {
    String name = ServerProperties.getTransactionCacheName();
    int size = ServerProperties.getTransationIdBucketSize();
    
    assertEquals("transactionCache", name);
    assertEquals(200, size);
  }
}
