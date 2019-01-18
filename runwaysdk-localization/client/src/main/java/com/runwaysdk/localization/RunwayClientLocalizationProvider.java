/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK GIS(tm).
 *
 * Runway SDK GIS(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway SDK GIS(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK GIS(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.localization;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.runwaysdk.RunwayLocalizationProviderIF;

public class RunwayClientLocalizationProvider implements RunwayLocalizationProviderIF
{
  
  private Object invoke(String method, Class<?>[] argTypes, Object...args)
  {
    // TODO : Add support for RMI
    
    try
    {
      Class<?> clazz = RunwayClientLocalizationProvider.class.getClassLoader().loadClass("com.runwaysdk.localization.LocalizedValueStore");
      
      Method m = clazz.getMethod(method, argTypes);
      
      return (String) m.invoke(null, args);
    }
    catch (ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
    {
      return null;
    }
  }

  @Override
  public String localize(String key, Locale locale)
  {
    return (String) invoke("localize", new Class<?>[] {String.class, Locale.class}, key, locale);
  }

  @SuppressWarnings("unchecked")
  @Override
  public Map<String, String> getAll(Locale locale)
  {
    return (Map<String, String>) invoke("getAll", new Class<?>[] {Locale.class}, locale);
  }

  @Override
  public String localize(String key)
  {
    return (String) invoke("localize", new Class<?>[] {String.class}, key);
  }

  @SuppressWarnings("unchecked")
  @Override
  public Map<String, String> getAll()
  {
    return (Map<String, String>) invoke("getAll", new Class<?>[] {});
  }

  @Override
  public void install(Locale locale)
  {
    invoke("install", new Class<?>[] {Locale.class}, locale);
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<Locale> getInstalledLocales()
  {
    return (List<Locale>) invoke("getInstalledLocales", new Class<?>[] {});
  }
  
}
