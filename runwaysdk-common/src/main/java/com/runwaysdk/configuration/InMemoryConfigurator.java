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

import java.util.ArrayList;

import org.apache.commons.configuration.BaseConfiguration;
import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.SystemConfiguration;

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
public class InMemoryConfigurator implements ConfigurationReaderIF
{
  private CompositeConfiguration config;
  private Configuration interpolated;
  
  private ArrayList<CommonsConfigurationReader> dependencies = new ArrayList<CommonsConfigurationReader>();
  
  public InMemoryConfigurator() {
    config = new CompositeConfiguration();
    config.addConfiguration(new BaseConfiguration());
    config.addConfiguration(new SystemConfiguration());
    interpolate();
  }
  
  public void addInterpolateDependency(ConfigurationReaderIF reader) {
    if (reader instanceof CommonsConfigurationReader) {
      dependencies.add((CommonsConfigurationReader) reader);
    }
  }
  
  /**
   * @see com.runwaysdk.configuration.ConfigurationReaderIF#getString(java.lang.String)
   */
  @Override
  public String getString(String key)
  {
    return interpolated.getString(key);
  }

  /**
   * @see com.runwaysdk.configuration.ConfigurationReaderIF#getBoolean(java.lang.String)
   */
  @Override
  public Boolean getBoolean(String key)
  {
    return interpolated.getBoolean(key);
  }

  /**
   * @see com.runwaysdk.configuration.ConfigurationReaderIF#getInteger(java.lang.String)
   */
  @Override
  public Integer getInteger(String key)
  {
    return interpolated.getInt(key);
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
  
  public boolean containsKey(String key) {
    return interpolated.containsKey(key);
  }
  
  public void clear() {
    config.clear();
    interpolate();
  }
  
  /**
   * @see com.runwaysdk.configuration.ConfigurationReaderIF#setProperty(java.lang.String, java.lang.Object)
   */
  @Override
  public void setProperty(String key, Object value)
  {
    config.setProperty(key, value);
    interpolate();
  }
  
  private void interpolateDependencies() {
    for (int i = 0; i < dependencies.size(); ++i) {
      dependencies.get(i).interpolate();
    }
  }
  
  public void interpolate() {
    interpolateDependencies();
    interpolated = config.interpolatedConfiguration();
  }
  
  public CompositeConfiguration getImpl() {
    return config;
  }

  /**
   * @param string
   * @return
   */
  public Object getProperty(String string)
  {
    return config.getProperty(string);
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
