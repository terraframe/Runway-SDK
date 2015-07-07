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
/*
 * Created on Aug 13, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.runwaysdk.dataaccess;

import java.util.Locale;
import java.util.Map;



/**
 * @author nathan
 *
 */
public interface MetadataDAOIF extends BusinessDAOIF
{
  /**
   * Name of the table used to store intances of this class.
   */
  public static final String TABLE                       = "metadata";

  /**
   * Returns the signature of the metadata.
   *
   * @return signature of the metadata.
   */
  public String getSignature();

  /**
   * Returns the display label of this metadata object
   *
   * @param locale
   *
   * @return
   */
  public String getDisplayLabel(Locale locale);

  /**
   * Returns a map where the key is the locale and the value is the localized
   * String value.
   *
   * @return map where the key is the locale and the value is the localized
   *   String value.
   */
  public Map<String, String> getDisplayLabels();
  
  /**
   * Returns true if the metadata can be removed, false otherwise.
   * @return true if the metadata can be removed, false otherwise.
   */
  public boolean isRemovable();

  /**
   * Returns a description of this metadata;
   *
   * @param SupportedLocale locale
   *
   * @return a description of this metadata;
   */
  public String getDescription(Locale locale);

  /**
   * Returns a map where the key is the locale and the value is the localized
   * String value.
   *
   * @return map where the key is the locale and the value is the localized
   *   String value.
   */
  public Map<String, String> getDescriptions();
  
  /**
   * @return Key used the permission hash map
   */
  public String getPermissionKey();
}
