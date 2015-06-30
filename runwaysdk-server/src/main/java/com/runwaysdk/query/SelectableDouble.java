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

public interface SelectableDouble extends SelectableNumber
{ 
  // Equals
  /**
   * Double Equals.
   * @param value
   * @return Basic Condition object
   */
  public BasicCondition EQ(Double value);
  
  // Not Equals
  /**
   * Double Not Equals.
   * @param statement
   * @return Basic Condition object
   */
  public BasicCondition NE(Double statement);
  
  // Greater Than
  /**
   * Double Greater Than.
   * @param value
   * @return Basic Condition object
   */
  public BasicCondition GT(Double value);

  
  // Greater Than or Equal
  /**
   * Double Greater Than or Equals.
   * @param values
   * @return Basic Condition object
   */
  public BasicCondition GE(Double values);
  
  // Less Than
  /**
   * Double Less Than.
   * @param value
   * @return Basic Condition object
   */
  public BasicCondition LT(Double value);


  // Less Than or Equal
  /**
   * Double Less Than or Equal.
   * @param value
   * @return Basic Condition object
   */
  public BasicCondition LE(Double value);
}
