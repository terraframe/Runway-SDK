/**
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
 */
package com.runwaysdk.dataaccess;

import java.util.Locale;
import java.util.Map;

import com.runwaysdk.dataaccess.metadata.MdParameterDAO;
import com.runwaysdk.dataaccess.metadata.ParameterMarker;
import com.runwaysdk.dataaccess.metadata.Type;

public interface MdParameterDAOIF extends MetadataDAOIF
{
  /**
   * Returns the MdMethod on which this parameter is defined.
   * @return MdMethod on which this parameter is defined.
   */
  public ParameterMarker getEnclosingMetadata();

  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAOIF#getBusinessDAO()
   */
  public MdParameterDAO getBusinessDAO();

  public String getParameterName();

  public Type getParameterType();

  public String getParameterOrder();

  /**
   * Returns the display label of this metadata object
   *
   * @param locale
   *
   * @return the display label of this metadata object
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
}
