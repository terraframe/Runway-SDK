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
package com.runwaysdk.query.sql;

import java.util.Locale;
import java.util.Map;

import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.dataaccess.MdAttributeBooleanDAOIF;
import com.runwaysdk.dataaccess.metadata.MdAttributeBooleanDAO;
import com.runwaysdk.query.ValueQuery;

public class MdAttributeBoolean_SQL extends MdAttributePrimitive_SQL implements MdAttributeBooleanDAOIF
{
  /**
   *
   */
  private static final long serialVersionUID = -593797680690098443L;

  /**
   * Used to spoof metadata for writing SQL directly to the {@link ValueQuery} API.
   *
   * @param attributeName
   * @param displayLabel
   */
  public MdAttributeBoolean_SQL(String attributeName, String displayLabel)
  {
    super(attributeName, displayLabel);
  }

  @Override
  public String getType()
  {
    return MdAttributeBooleanInfo.CLASS;
  }

  /**
   * Returns the positive display label of this metadata object
   *
   * @param locale
   *
   * @return the positive display label of this metadata object
   */
  public String getPositiveDisplayLabel(Locale locale)
  {
    throw new UnsupportedOperationException(this.unsupportedOperationMessage);
  }

  /**
   * Returns a map where the key is the locale and the value is the localized
   * String value.
   *
   * @return map where the key is the locale and the value is the localized
   *   String value.
   */
  public Map<String, String> getPositiveDisplayLabels()
  {
    throw new UnsupportedOperationException(this.unsupportedOperationMessage);
  }

  /**
   * Returns the negative display label of this metadata object
   *
   * @param locale
   *
   * @return the negative display label of this metadata object
   */
  public String getNegativeDisplayLabel(Locale locale)
  {
    throw new UnsupportedOperationException(this.unsupportedOperationMessage);
  }

  /**
   * Returns a map where the key is the locale and the value is the localized
   * String value.
   *
   * @return map where the key is the locale and the value is the localized
   *   String value.
   */
  public Map<String, String> getNegativeDisplayLabels()
  {
    throw new UnsupportedOperationException(this.unsupportedOperationMessage);
  }

  /* (non-Javadoc)
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdAttributeBooleanDAO getBusinessDAO()
  {
    throw new UnsupportedOperationException(this.unsupportedOperationMessage);
  }

}
