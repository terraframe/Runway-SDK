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

import java.util.Date;

public interface SelectableMoment extends SelectablePrimitive
{
  // Equals
  /**
   * Moment Equals.
   * @param selectable
   * @return Basic Condition object
   */
  public Condition EQ(SelectableMoment selectable);

  /**
   * Date Equals.
   * @param date
   * @return Basic Condition object
   */
  public BasicCondition EQ(Date date);

  // Not Equals
  /**
   * Moment Not Equals.
   * @param selectable
   * @return Basic Condition object
   */
  public Condition NE(SelectableMoment selectable);

  /**
   * Date Equals.
   * @param date
   * @return Basic Condition object
   */
  public BasicCondition NE(Date date);

  // Greater Than
  /**
   * Date Greater Than.
   *
   * @param statement
   * @return Basic Condition object
   */
  public BasicCondition GT(String statement);

  /**
   * Date Greater Than.
   * @param date
   * @return Basic Condition object
   */
  public BasicCondition GT(Date date);

  /**
   * Moment Greater Than.
   * @param selectable
   * @return Basic Condition object
   */
  public Condition GT(SelectableMoment selectable);

  // Greater Than or Equal
  /**
   * Date Greater Than or Equals.
   *
   * @param statement
   * @return Basic Condition object
   */
  public BasicCondition GE(String statement);

  /**
   * Date Greater Than or Equals.
   * @param statement
   * @return Basic Condition object
   */
  public BasicCondition GE(Date date);

  /**
   * Moment Greater Than or Equals.
   * @param selectable
   * @return Basic Condition object
   */
  public Condition GE(SelectableMoment selectable);

  // Less Than
  /**
   * Date Less Than.
   *
   * @param statement
   * @return Basic Condition object
   */
  public BasicCondition LT(String statement);

  /**
   * Moment Less Than.
   * @param selectable
   * @return Basic Condition object
   */
  public Condition LT(SelectableMoment selectable);

  /**
   * Date Less Than.
   * @param date
   * @return Basic Condition object
   */
  public BasicCondition LT(Date date);

  // Less Than or Equal
  /**
   * Date Less Than or Equal.
   *
   * @param statement
   * @return Basic Condition object
   */
  public BasicCondition LE(String statement);

  /**
   * Moment Less Than or Equal.
   * @param simpleFunction
   * @return Basic Condition object
   */
  public Condition LE(SelectableMoment selectable);

  /**
   * Date Less Than or Equal.
   * @param statement
   * @return Basic Condition object
   */
  public BasicCondition LE(Date date);

}
