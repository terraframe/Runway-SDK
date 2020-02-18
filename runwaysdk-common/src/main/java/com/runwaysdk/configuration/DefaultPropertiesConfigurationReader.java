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
/**
 * 
 */
package com.runwaysdk.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

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
public class DefaultPropertiesConfigurationReader extends AbstractConfigurationReader implements ConfigurationReaderIF
{
  private Properties props;
  
  public DefaultPropertiesConfigurationReader(ConfigGroupIF group, String config) {
    props = new Properties();
    ClassLoader loader = DefaultPropertiesConfigurationReader.class.getClassLoader();           
    InputStream stream = loader.getResourceAsStream(group.getPath() + config);
    
    try
    {
      props.load(stream);
    }
    catch (IOException e)
    {
      throw new RunwayConfigurationException(e);
    }
  }
  
  public DefaultPropertiesConfigurationReader(String config) {
    this(ConfigGroup.ROOT, config);
  }

  /**
   * @see com.runwaysdk.configuration.ConfigurationReaderIF#getString(java.lang.String)
   */
  @Override
  public String getString(String key)
  {
    return props.getProperty(key);
  }

  /**
   * @see com.runwaysdk.configuration.ConfigurationReaderIF#getBoolean(java.lang.String)
   */
  @Override
  public Boolean getBoolean(String key)
  {
    return Boolean.parseBoolean(props.getProperty(key));
  }

  /**
   * @see com.runwaysdk.configuration.ConfigurationReaderIF#getInteger(java.lang.String)
   */
  @Override
  public Integer getInteger(String key)
  {
    return Integer.parseInt(props.getProperty(key));
  }
  
  /**
   * @see com.runwaysdk.configuration.ConfigurationReaderIF#setProperty(java.lang.String, java.lang.Object)
   */
  @Override
  public void setProperty(String key, Object value)
  {
    props.setProperty(key, value.toString());
  }

  @Override
  public Long getLong(String key)
  {
    return Long.parseLong(props.getProperty(key));
  }

  @Override
  public Float getFloat(String key)
  {
    return Float.parseFloat(props.getProperty(key));
  }
}
