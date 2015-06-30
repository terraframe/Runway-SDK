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
package com.runwaysdk.query.sql;

import com.runwaysdk.dataaccess.MdAttributeDecDAOIF;
import com.runwaysdk.query.ValueQuery;

public abstract class MdAttributeDec_SQL extends MdAttributeNumber_SQL implements MdAttributeDecDAOIF
{

  /**
   * 
   */
  private static final long serialVersionUID = 8076551831984106418L;

  /**
   * Used to spoof metadata for writing SQL directly to the {@link ValueQuery} API.
   *
   * @param attributeName
   * @param displayLabel
   */
  public MdAttributeDec_SQL(String attributeName, String displayLabel)
  {
    super(attributeName, displayLabel);
  }

  /**
   *Returns the total maximum length of the decimal number (including number of decimal
   * places the right of the decimal).
   * @return total maximum length of the decimal number (including number of decimal
   * places the right of the decimal).
   */
  public String getLength()
  {
    throw new UnsupportedOperationException(this.unsupportedOperationMessage);
  }

  /**
   *Returns the number of decimal places to the right of the decimal.
   * @return number of decimal places to the right of the decimal.
   */
  public String getDecimal()
  {
    throw new UnsupportedOperationException(this.unsupportedOperationMessage);
  }
}
