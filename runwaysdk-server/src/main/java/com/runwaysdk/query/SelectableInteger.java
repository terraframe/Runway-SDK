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


public interface SelectableInteger extends SelectableNumber
{
  // Equals
  /**
   * Integer Equals.
   * @param value
   * @return Basic Condition object
   */
  public BasicCondition EQ(Integer value);

  // Not Equals
  /**
   * Integer Not Equals.
   * @param value
   * @return Basic Condition object
   */
  public BasicCondition NE(Integer value);

  // Greater Than
  /**
   * Integer Greater Than.
   * @param value
   * @return Basic Condition object
   */
  public BasicCondition GT(Integer value);

  // Greater Than or Equal
  /**
   * Integer Greater Than or Equals.
   * @param value
   * @return Basic Condition object
   */
  public BasicCondition GE(Integer value);

  // Less Than
  /**
   * Integer Less Than.
   * @param value
   * @return Basic Condition object
   */
  public BasicCondition LT(Integer value);

  // Less Than or Equal
  /**
   * Number Less Than or Equal.
   * @param value
   * @return Basic Condition object
   */
  public BasicCondition LE(Integer value);

}
