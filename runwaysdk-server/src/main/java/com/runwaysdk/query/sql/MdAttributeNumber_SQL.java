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

import com.runwaysdk.dataaccess.MdAttributeNumberDAOIF;
import com.runwaysdk.query.ValueQuery;

public abstract class MdAttributeNumber_SQL extends MdAttributePrimitive_SQL implements MdAttributeNumberDAOIF
{

  /**
   * 
   */
  private static final long serialVersionUID = -1368663112770338521L;

  /**
   * Used to spoof metadata for writing SQL directly to the {@link ValueQuery}
   * API.
   * 
   * @param attributeName
   * @param displayLabel
   */
  public MdAttributeNumber_SQL(String attributeName, String displayLabel)
  {
    super(attributeName, displayLabel);
  }

  /**
   * Returns the value setting of whether or not positive numbers are rejected
   * by this attribute.
   * 
   * @return true if positive numbers are rejected, false otherwise.
   */
  public String isPositiveRejected()
  {
    throw new UnsupportedOperationException(this.unsupportedOperationMessage);
  }

  /**
   * Returns the value setting of whether or not zero is rejected by this
   * attribute.
   * 
   * @return true if zero is rejected, false otherwise.
   */
  public String isZeroRejected()
  {
    throw new UnsupportedOperationException(this.unsupportedOperationMessage);
  }

  /**
   * Returns the value setting of whether or not negative numbers are rejected
   * by this attribute.
   * 
   * @return true if negative numbers are rejected, false otherwise.
   */
  public String isNegativeRejected()
  {
    throw new UnsupportedOperationException(this.unsupportedOperationMessage);
  }

  @Override
  public Double getStartRange()
  {
    throw new UnsupportedOperationException(this.unsupportedOperationMessage);
  }

  @Override
  public Double getEndRange()
  {
    throw new UnsupportedOperationException(this.unsupportedOperationMessage);
  }
}
