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
package com.runwaysdk;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public interface RunwayLocalizationProviderIF
{
  public String localize(String key);
  
  public String localize(String key, Locale locale);
  
  public Map<String, String> getAll();
  
  public Map<String, String> getAll(Locale locale);
  
  public void install(Locale locale);
  
  public void uninstall(Locale locale);
  
  public List<Locale> getInstalledLocales();
}
