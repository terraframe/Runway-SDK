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
package com.runwaysdk.constants;

public enum SupportedLocale
{

  /**
   * English (United States) locale
   */
  EN_US("20070322JN000000000000000000001020070322JN0000000000000000000009", "English (United States)");
  
  /**
   * 
   */
  public static final String LOCALE_LABEL = "localeLabel";
  
  /**
   * The id of the locale
   */
  private String id;
  
  /**
   * The label of the local
   */
  private String localeLabel;
  
  /**
   * Enum constructor
   * 
   * @param id
   */
  private SupportedLocale(String id, String localeLabel)
  {
    this.id = id;
    this.localeLabel = localeLabel;
  }
  
  /**
   * Returns the id 
   */
  public String getId()
  {
    return id;
  }
  
  /**
   * Returns the local label
   */
  public String getLocaleLabel()
  {
    return this.localeLabel;
  }
}
