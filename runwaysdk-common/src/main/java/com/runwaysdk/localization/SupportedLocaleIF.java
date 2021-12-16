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
package com.runwaysdk.localization;

import java.util.Locale;
import java.util.Map;

public interface SupportedLocaleIF
{
  /**
   * Pass-through to the Runway object.
   */
  public void apply();
  
  /**
   * Pass-through to the Runway object.
   */
  public void delete();
  
  /**
   * Pass-through to the Runway object.
   */
  public void lock();
  
  /**
   * Pass-through to the Runway object.
   */
  public void unlock();
  
  /**
   * Pass-through to the Runway object.
   */
  public void appLock();
  
  /**
   * Returns the locale's DisplayLabel, which is a localized value stored
   * in Runway. This label can be set by the end user to localize a user-friendly
   * display label for this locale.
   */
  public LocalizedValueIF getDisplayLabel();
  
  /**
   * Returns the Java {@link java.util.Locale} representation of this locale. 
   */
  public Locale getLocale();
  
  /**
   * Returns the Runway (enum) name of this locale. This is equivalent to doing: 
   * 
   * getLocale().toString()
   * 
   * TODO : The locale.toString method is documented to only be used for debug purposes.
   *   For some reason we happen to use it in our core.
   */
  public String getName();
  
  /**
   * Equivalent to calling getLocale().getCountry()
   */
  public String getCountry();
  
  /**
   * Equivalent to calling getLocale().getLanguage()
   */
  public String getLanguage();
  
  /**
   * Equivalent to calling getLocale().getVariant()
   */
  public String getVariant();
}
