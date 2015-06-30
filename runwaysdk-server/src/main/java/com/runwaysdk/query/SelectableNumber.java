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
package com.runwaysdk.query;

public interface SelectableNumber extends SelectablePrimitive
{
  // Equals
  /**
   * Number Equal comparison.
   * @param selectable
   * @return Basic Condition object
   */
  public Condition EQ(SelectableNumber selectable);


  // Not Equals
  /**
   * Number Not Equal comparison.
   * @param selectable
   * @return Basic Condition object
   */
  public Condition NE(SelectableNumber selectable);

  // Greater Than
  /**
   * Number Greater Than.
   *
   * @param statement
   * @return Basic Condition object
   */
  public BasicCondition GT(String statement);

  /**
   * Number Greater than comparison.
   * @param selectable
   * @return Basic Condition object
   */
  public Condition GT(SelectableNumber selectable);

  // Greater Than or Equal
  /**
   * Number Greater Than or Equals.
   *
   * @param statement
   * @return Basic Condition object
   */
  public BasicCondition GE(String statement);

  /**
   * Number Greater Equals than comparison.
   * @param selectable
   * @return Basic Condition object
   */
  public Condition GE(SelectableNumber selectable);

  // Less Than
  /**
   * Decimal Less Than or Equal.
   *
   * @param statement
   * @return Basic Condition object
   */
  public BasicCondition LE(String statement);

  /**
   * Number Less Than.
   * @param selectable
   * @return Basic Condition object
   */
  public Condition LT(SelectableNumber selectable);

  // Less Than or Equal
  /**
   * Number Less Than.
   *
   * @param statement
   * @return Basic Condition object
   */
  public BasicCondition LT(String statement);

  /**
   * Number Less Than or Equal.
   * @param selectable
   * @return Basic Condition object
   */
  public Condition LE(SelectableNumber selectable);

}
