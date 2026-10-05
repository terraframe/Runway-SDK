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

import org.apache.commons.configuration2.BaseConfiguration;
import org.apache.commons.configuration2.CompositeConfiguration;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.event.ConfigurationEvent;
import org.apache.commons.configuration2.event.EventListener;
import org.apache.commons.configuration2.io.FileHandler;

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
public class CommonsConfigurationReader extends AbstractConfigurationReader implements ConfigurationReaderIF, EventListener<ConfigurationEvent>
{
  private CompositeConfiguration cconfig;
  
  /**
   * Values set through setProperty(). Kept as the first child of cconfig so they take precedence over
   * every other layer (including environment variables) when interpolated.
   */
  private BaseConfiguration overrides = new BaseConfiguration();
  
  private Configuration interpolated;
  
  public CommonsConfigurationReader(ConfigGroupIF group, String config, CompositeConfiguration _cconfig)
  {
    this.cconfig = _cconfig;
    this.cconfig.addConfigurationFirst(overrides);
    
    try
    {
      URL resource = ConfigurationManager.getResource(group, config);
      
      PropertiesConfiguration propConfig = new PropertiesConfiguration();
      new FileHandler(propConfig).load(resource);
      
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
    overrides.setProperty(key, value);
  }
  
  public void interpolate()
  {
    interpolated = cconfig.interpolatedConfiguration();
  }

  /**
   * @see org.apache.commons.configuration2.event.EventListener#onEvent(org.apache.commons.configuration2.event.Event)
   */
  @Override
  public void onEvent(ConfigurationEvent event)
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
