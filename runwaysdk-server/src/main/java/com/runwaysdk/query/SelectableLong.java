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
package com.runwaysdk.query;


public interface SelectableLong extends SelectableNumber
{ 
  // Equals
  /**
   * Long Equals.
   * @param value
   * @return Basic Condition object
   */
  public BasicCondition EQ(Long value);
  
  
  // Not Equals
  /**
   * Long Not Equals.
   * @param value
   * @return Basic Condition object
   */
  public BasicCondition NE(Long value);
  
  // Greater Than
  /**
   * Long Greater Than.
   * @param value
   * @return Basic Condition object
   */
  public BasicCondition GT(Long value);
  
  // Greater Than or Equal
  /**
   * Long Greater Than or Equals.
   * @param value
   * @return Basic Condition object
   */
  public BasicCondition GE(Long value);
  
  // Less Than
  /**
   * Long Less Than.
   * @param value
   * @return Basic Condition object
   */
  public BasicCondition LT(Long value);
  
  // Less Than or Equal
  /**
   * Long Less Than or Equal.
   * @param value
   * @return Basic Condition object
   */
  public BasicCondition LE(Long value);

}
