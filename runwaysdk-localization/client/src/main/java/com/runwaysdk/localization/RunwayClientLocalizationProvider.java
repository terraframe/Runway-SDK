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
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * This class is provided as an example of client-side support for the {@link com.runwaysdk.localization.RunwayLocalizationProviderIF}
 * service. This class is currently NOT USED due to the fact that it doesn't actually support RMI, and there's no point to
 * it since you should just use the {@link com.runwaysdk.localization.RunwayServerLocalizationProvider} if you have access
 * to the server jar anyway. For this reason, this client-side jar does not currently provide a META-INF service file for
 * the {@link com.runwaysdk.localization.RunwayLocalizationProviderIF} interface since that would unnecessarily complicate the service
 * loading. 
 * 
 * @author rrowlands
 */
public class RunwayClientLocalizationProvider implements RunwayLocalizationProviderIF
{
  
  public static final String SERVER_LOCALIZATION_PROVIDER = "com.runwaysdk.localization.RunwayServerLocalizationProvider";
  
  private Object invoke(String method, Class<?>[] argTypes, Object...args)
  {
    // TODO : Add support for RMI
    
    try
    {
      Class<?> clazz = RunwayClientLocalizationProvider.class.getClassLoader().loadClass(SERVER_LOCALIZATION_PROVIDER);
      
      Method m = clazz.getMethod(method, argTypes);
      
      return (String) m.invoke(null, args);
    }
    catch (ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
    {
      return null;
    }
  }
  
  @Override
  public String localize(String key)
  {
    return (String) invoke("localize", new Class<?>[] {String.class}, key);
  }

  @Override
  public String localize(String key, Locale locale)
  {
    return (String) invoke("localize", new Class<?>[] {String.class, Locale.class}, key, locale);
  }
  
  @Override
  public LocalizedValueIF localizeAll(String key)
  {
    return (LocalizedValueIF) invoke("localizeAll", new Class<?>[] {java.lang.String.class}); // TODO : This object would have to be converted to a DTO to actually work in a client context
  }

  @SuppressWarnings("unchecked")
  @Override
  public Map<String, String> getAll(Locale locale)
  {
    return (Map<String, String>) invoke("getAll", new Class<?>[] {Locale.class}, locale);
  }

  @SuppressWarnings("unchecked")
  @Override
  public Map<String, String> getAll()
  {
    return (Map<String, String>) invoke("getAll", new Class<?>[] {});
  }

  @Override
  public SupportedLocaleIF install(Locale locale)
  {
    return (SupportedLocaleIF) invoke("install", new Class<?>[] {Locale.class}, locale);
  }
  
  @Override
  public void uninstall(Locale locale)
  {
    invoke("uninstall", new Class<?>[] {Locale.class}, locale);
  }

  @SuppressWarnings("unchecked")
  @Override
  public Set<Locale> getInstalledLocales()
  {
    return (Set<Locale>) invoke("getInstalledLocales", new Class<?>[] {});
  }

  @SuppressWarnings("unchecked")
  @Override
  public Set<SupportedLocaleIF> getSupportedLocales()
  {
    return (Set<SupportedLocaleIF>) invoke("getInstalledLocales", new Class<?>[] {}); // TODO : This object would have to be converted to a DTO to actually work in a client context
  }
  
  @Override
  public SupportedLocaleIF getSupportedLocale(Locale locale)
  {
    return (SupportedLocaleIF) invoke("getSupportedLocale", new Class<?>[] {Locale.class}, locale); // TODO : This object would have to be converted to a DTO to actually work in a client context
  }
  
}
