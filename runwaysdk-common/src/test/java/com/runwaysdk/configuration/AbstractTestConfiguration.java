/**
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
 */
package com.runwaysdk.configuration;

/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved.
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
 ******************************************************************************/

import static org.junit.Assert.assertEquals;

import java.util.Locale;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.DeployProperties;
import com.runwaysdk.constants.LocalProperties;

abstract public class AbstractTestConfiguration
{
  abstract ConfigurationResolverIF getConfigResolver();

  @Before
  public void setUp()
  {
    ConfigurationManager.setConfigResolver(getConfigResolver());
    LegacyPropertiesSupport.dumpInstance();
    CommonProperties.dumpInstance();
    DeployProperties.dumpInstance();
  }

  @After
  public void tearDown()
  {
    CommonsConfigurationResolver.getInMemoryConfigurator().clear();
  }

  @Test
  public void testCommonProperties()
  {
    assertEquals("testExpansion", CommonProperties.getServerExpansionModules()[0]);
    assertEquals("testExpansion", CommonProperties.getClientExpansionModules()[0]);
    assertEquals("testModuleLoader", CommonProperties.getServerModulesLoader());
    assertEquals("java:com.runwaysdk.proxy.RemoteAdapter", CommonProperties.getRMIService());
    assertEquals(new Integer(1199), (Integer) CommonProperties.getRMIPort());
    assertEquals(Locale.ENGLISH, CommonProperties.getDefaultLocale());
    assertEquals("terraframe.com", CommonProperties.getDomain());
    assertEquals("tfadmin", CommonProperties.getDeployAppName());
  }

  @Test
  public void testDeployProperties()
  {
    String password = DeployProperties.getContainerPassword();

    assertEquals("framework", password);
  }

  public void testLocalProperties()
  {
    String src = LocalProperties.getCommonGenSrc();
    String jspDir = LocalProperties.getJspDir();

    assertEquals("testValue", src);
    assertEquals("testValue", jspDir);
  }
}
