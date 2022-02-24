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
package com.runwaysdk.dataaccess;

public interface MdAttributeNumberDAOIF extends MdAttributePrimitiveDAOIF
{
  /**
   * Name of the table used to store intances of this class.
   */
  public static final String TABLE = "md_attribute_number";

  /**
   * Returns the value setting of whether or not positive numbers are rejected
   * by this attribute.
   * 
   * @return true if positive numbers are rejected, false otherwise.
   */
  public String isPositiveRejected();

  /**
   * Returns the value setting of whether or not zero is rejected by this
   * attribute.
   * 
   * @return true if zero is rejected, false otherwise.
   */
  public String isZeroRejected();

  /**
   * Returns the value setting of whether or not negative numbers are rejected
   * by this attribute.
   * 
   * @return true if negative numbers are rejected, false otherwise.
   */
  public String isNegativeRejected();

  public Double getStartRange();

  public Double getEndRange();

}
