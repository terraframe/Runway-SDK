/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
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
package com.runwaysdk.configuration;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import com.runwaysdk.configuration.ConfigurationManager.ConfigGroup;
import com.runwaysdk.configuration.ConfigurationManager.ConfigGroupIF;

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
public class ProfileConfigurationResolver implements ConfigurationResolverIF
{
  public ProfileConfigurationResolver()
  {
    
  }
  
  public ConfigurationReaderIF getReader(ConfigGroupIF configGroup, String config)
  {
    return ProfileManager.getBundle(configGroup.getOidentifier() + "/" + config);
  }
  
  public URL getResource(ConfigGroupIF configGroup, String name)
  {
    String location = null;
    URL resource = null;
    
    if (configGroup.equals(ConfigGroup.XSD) || configGroup.equals(ConfigGroup.METADATA))
    {
      String path = configGroup.getPath() + name;
      location = "classpath:" + path;
      resource = ConfigurationManager.class.getClassLoader().getResource(path);
    }
    else
    {
      String profileName = ProfileManager.getProfileDir().getName();

      if (ProfileManager.getExplicitySpecifiedProfileHome() != null)
      {
        location = ProfileManager.getExplicitySpecifiedProfileHome();
        if (ProfileManager.isFlattened())
        {
          location += "/" + name;
        }
        else
        {
          location += "/" + profileName + "/" + configGroup.getOidentifier() + "/" + name;
        }

        File file = new File(location);
        if (file.exists())
        {
          try
          {
            return file.toURI().toURL();
          }
          catch (MalformedURLException e)
          {
          }
        }
      }
      else
      {
        String path;
        if (ProfileManager.isFlattened())
        {
          path = name;
        }
        else
        {
          path = profileName + "/" + configGroup.getOidentifier() + "/" + name;
        }
        location = "classpath:" + path;
        resource = ConfigurationManager.class.getClassLoader().getResource(path);
      }
    }
    
    if (resource == null)
    {
      String msg = "Unable to find configuration resource named [" + name + "] in config group [" + configGroup.getOidentifier() + "] at location [" + location + "] with configResolver [" + this.toString() + "].";
      throw new RunwayConfigurationException(msg);
    }
    
    return resource;
  }
  
  @Override
  public String toString()
  {
    return this.getClass().getName();
  }
}
