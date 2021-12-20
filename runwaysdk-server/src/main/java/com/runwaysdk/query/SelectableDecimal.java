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
package com.runwaysdk.query;

import java.math.BigDecimal;

public interface SelectableDecimal extends SelectableNumber
{
  // Equals
  /**
   * Decimal Equals.
   * @param bigDecimal
   * @return Basic Condition object
   */
  public BasicCondition EQ(BigDecimal bigDecimal);

  // Not Equals
  /**
   * Decimal Not Equals.
   * @param bigDecimal
   * @return Basic Condition object
   */
  public BasicCondition NE(BigDecimal bigDecimal);

  // Greater Than
  /**
   * Decimal Greater Than.
   * @param bigDecimal
   * @return Basic Condition object
   */
  public BasicCondition GT(BigDecimal bigDecimal);


  // Greater Than or Equal
  /**
   * Decimal Greater Than or Equals.
   * @param bigDecimal
   * @return Basic Condition object
   */
  public BasicCondition GE(BigDecimal bigDecimal);

  // Less Than
  /**
   * Decimal Less Than.
   * @param bigDecimal
   * @return Basic Condition object
   */
  public BasicCondition LT(BigDecimal bigDecimal);

  // Less Than or Equal
  /**
   * Decimal Less Than or Equal.
   * @param bigDecimal
   * @return Basic Condition object
   */
  public BasicCondition LE(BigDecimal bigDecimal);

}
