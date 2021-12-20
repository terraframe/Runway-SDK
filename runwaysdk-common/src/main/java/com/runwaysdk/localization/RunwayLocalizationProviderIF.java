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
package com.runwaysdk.localization;

import java.util.Locale;
import java.util.Map;
import java.util.Set;

public interface RunwayLocalizationProviderIF
{
  public String localize(String key);
  
  public String localize(String key, Locale locale);
  
  public LocalizedValueIF localizeAll(String key);
  
  public Map<String, String> getAll();
  
  public Map<String, String> getAll(Locale locale);
  
  /**
   * Installs a new locale into the system.
   * 
   * @return The newly created SupportedLocaleIF
   */
  public SupportedLocaleIF install(Locale locale);
  
  /**
   * Uninstalls a new locale into the system.
   */
  public void uninstall(Locale locale);
  
  /**
   * Returns all system installed locales, represented as {@link java.util.Locale}
   */
  public Set<Locale> getInstalledLocales();
  
  /**
   * Returns all system installed locales, represented as {@link com.runwaysdk.localization.SupportedLocaleIF}
   */
  public Set<SupportedLocaleIF> getSupportedLocales();

  /**
   * Returns the equivalent {@link com.runwaysdk.localization.SupportedLocaleIF} for a given {@link java.util.Locale}. If the given locale
   * is not installed, a {@link com.runwaysdk.dataaccess.cache.DataNotFoundException} is thrown.
   */
  public SupportedLocaleIF getSupportedLocale(Locale locale);
}
