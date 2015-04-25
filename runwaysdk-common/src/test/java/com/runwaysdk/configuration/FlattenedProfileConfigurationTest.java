/**
 * 
 */
package com.runwaysdk.configuration;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.junit.Test;

import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.profile.ProfileFlattener;

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
public class FlattenedProfileConfigurationTest extends AbstractTestConfiguration
{
  private String baseDir;

  @Override
  ConfigurationResolverIF getConfigResolver()
  {
    baseDir = CommonProperties.getProjectBasedir();

    ProfileFlattener.main(new String[] { "flat" });

    ProfileManager.setProfileHome(baseDir + "/target/test-classes/flat");

    return new ProfileConfigurationResolver();
  } 

  @Test
  public void testActuallyUsingFlattenedProfile()
  {
    // Change a property so we know we're actually using the flattened ones and
    // not the unflattened
    try
    {
      PropertiesConfiguration tprops = new PropertiesConfiguration(new File(baseDir + "/target/test-classes/flat/terraframe.properties"));
      String oldValue = tprops.getString("deploy.appname");

      tprops.setProperty("deploy.appname", "Actually Using Flattened Profile");
      tprops.save();

      try
      {
        CommonProperties.dumpInstance();
        String appName = CommonProperties.getDeployAppName();

        assertEquals("Actually Using Flattened Profile", appName);
      }
      finally
      {
        tprops.setProperty("deploy.appname", oldValue);
        tprops.save();

      }
    }
    catch (ConfigurationException e)
    {
      throw new RunwayConfigurationException(e);
    }

    CommonProperties.dumpInstance();
  }
}
