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

import java.net.URL;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.event.ConfigurationEvent;
import org.apache.commons.configuration.event.ConfigurationListener;

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
public class CommonsConfigurationReader extends AbstractConfigurationReader implements ConfigurationReaderIF, ConfigurationListener
{
  private CompositeConfiguration cconfig;
  private Configuration interpolated;
  
  public CommonsConfigurationReader(ConfigGroupIF group, String config, CompositeConfiguration _cconfig)
  {
    this.cconfig = _cconfig;
    
    try
    {
      URL resource = ConfigurationManager.getResource(group, config);
      
      PropertiesConfiguration propConfig = new PropertiesConfiguration();
      propConfig.setDelimiterParsingDisabled(true);
      propConfig.load(resource);
      
      cconfig.addConfiguration(propConfig);
      interpolate();
      CommonsConfigurationResolver.getInMemoryConfigurator().addInterpolateDependency(this);
    }
    catch (ConfigurationException e)
    {
      throw new RunwayConfigurationException(e);
    }
  }

  /**
   * @see com.runwaysdk.configuration.ConfigurationReaderIF#getString(java.lang.String)
   */
  @Override
  public String getString(String key)
  {
    return interpolated.getString(key, null);
  }

  /**
   * @see com.runwaysdk.configuration.ConfigurationReaderIF#getBoolean(java.lang.String)
   */
  @Override
  public Boolean getBoolean(String key)
  {
    return interpolated.getBoolean(key, null);
  }

  /**
   * @see com.runwaysdk.configuration.ConfigurationReaderIF#getInteger(java.lang.String)
   */
  @Override
  public Integer getInteger(String key)
  {
    return interpolated.getInteger(key, null);
  }
  
  /**
   * @see com.runwaysdk.configuration.ConfigurationReaderIF#getString(java.lang.String, java.lang.String)
   */
  @Override
  public String getString(String key, String defaultValue)
  {
    return interpolated.getString(key, defaultValue);
  }

  /**
   * @see com.runwaysdk.configuration.ConfigurationReaderIF#getBoolean(java.lang.String, java.lang.Boolean)
   */
  @Override
  public Boolean getBoolean(String key, Boolean defaultValue)
  {
    return interpolated.getBoolean(key, defaultValue);
  }

  /**
   * @see com.runwaysdk.configuration.ConfigurationReaderIF#getInteger(java.lang.String, java.lang.Integer)
   */
  @Override
  public Integer getInteger(String key, Integer defaultValue)
  {
    return interpolated.getInteger(key, defaultValue);
  }
  
  /**
   * Sets the property on the underlying configuration. You must call interpolate after doing this to see the new value.
   */
  @Override
  public void setProperty(String key, Object value)
  {
    cconfig.setProperty(key, value);
  }
  
  public void interpolate()
  {
    interpolated = cconfig.interpolatedConfiguration();
  }

  /**
   * @see org.apache.commons.configuration.event.ConfigurationListener#configurationChanged(org.apache.commons.configuration.event.ConfigurationEvent)
   */
  @Override
  public void configurationChanged(ConfigurationEvent event)
  {
//    if (!event.isBeforeUpdate()) {
//      this.config = ((CompositeConfiguration) this.config).interpolatedConfiguration();
//    }
  }

  @Override
  public Long getLong(String key)
  {
    return interpolated.getLong(key, null);
  }

  @Override
  public Long getLong(String key, Long defaultVaule)
  {
    return interpolated.getLong(key, defaultVaule);
  }

  @Override
  public Float getFloat(String key)
  {
    return interpolated.getFloat(key, null);
  }

  @Override
  public Float getFloat(String key, Float defaultValue)
  {
    return interpolated.getFloat(key, defaultValue);
  }
}
