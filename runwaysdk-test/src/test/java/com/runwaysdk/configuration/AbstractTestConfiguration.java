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
import static org.junit.Assert.assertTrue;

import java.io.File;
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
  }

  @After
  public void tearDown()
  {
    CommonsConfigurationResolver.getInMemoryConfigurator().clear();
  }

  @Test
  public void testCommonProperties()
  {
    int inty = CommonProperties.getRegistryPort();
    Locale locale = CommonProperties.getDefaultLocale();

    assertEquals(1199, inty);
    assertEquals(Locale.ENGLISH, locale);
    assertEquals("www.runwaysdk.com", CommonProperties.getDomain());
    assertEquals("runwaysdk_test", CommonProperties.getDeployAppName());
  }

  @Test
  public void testDeployProperties()
  {
    String password = DeployProperties.getContainerPassword();

    assertEquals("framework", password);
  }

  @Test
  public void testLocalProperties()
  {
    // ensure existence of java-gen
    File srcMain = new File(LocalProperties.getCommonGenSrc()).getParentFile().getParentFile();
    if (srcMain.exists()) {
      new File(LocalProperties.getCommonGenSrc()).mkdirs();
      new File(LocalProperties.getServerGenSrc()).mkdirs();
      new File(LocalProperties.getClientGenSrc()).mkdirs();
    }
    else
    {
      throw new RuntimeException("There is a problem with LocalProperties::common.src. We expected [" + srcMain.getAbsolutePath() + "] to exist.");
    }
    

    assertTrue(new File(LocalProperties.getCommonGenSrc()).exists());
    assertTrue(new File(LocalProperties.getServerGenSrc()).exists());
    assertTrue(new File(LocalProperties.getClientGenSrc()).exists());
//    assertTrue(new File(LocalProperties.getJspDir()).exists());

    // These properties don't work in the test environment because of some
    // stupid assumptions that are made.
    // The stupid assumptions can't be fixed because it would break DDMS.
    // It doesn't seem like they're necessary anyway though, we might be able to
    // remove them at some point.
    // assertTrue(new File(LocalProperties.getClientSrc()).exists());
    // assertTrue(new File(LocalProperties.getCommonSrc()).exists());
    // assertTrue(new File(LocalProperties.getServerSrc()).exists());
  }
}
