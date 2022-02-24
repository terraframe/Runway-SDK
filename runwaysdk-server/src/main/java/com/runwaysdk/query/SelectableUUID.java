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

public interface SelectableUUID extends SelectablePrimitive
{
  // Equals

  /**
   * Character = comparison case sensitive.
   * @param statement
   * @return Condition object
   */
  public Condition EQ(String statement);

  /**
   * Character = comparison case sensitive. This method accepts any Attribute because
   * databases generally allow for comparisons between different types.
   * @param selectable
   * @return Condition object
   */
  public Condition EQ(SelectableUUID selectable);

  /**
   * Character = comparison case insensitive.
   * @param statement
   * @return Condition object
   */
  public Condition EQi(String statement);

  /**
   * Character = comparison case insensitive.
   * @param selectable
   * @return Condition object
   */
  public Condition EQi(SelectableUUID selectable);

  // Not Equals
  /**
   * Character != comparison case sensitive.
   * @param statement
   * @return Condition object
   */
  public Condition NE(String statement);

  /**
   * Character != comparison case sensitive.
   * @param selectable
   * @return Basic Condition object
   */
  public Condition NE(SelectableUUID selectable);

  /**
   * Character != comparison case insensitive.
   * @param statement
   * @return Condition object
   */
  public Condition NEi(String statement);

  /**
   * Character inequality case insensitive.
   * @param selectable
   * @return Condition object
   */
  public Condition NEi(SelectableUUID selectable);

  // IN
  /**
   * Character IN case sensitive comparison of statements.
   * @param statementArray
   * @return Condition object
   */
  public Condition IN(String ... statementArray);
  /**
   * Character IN case insensitive comparison of statements.
   * @param statementArray
   * @return Condition object
   */
  public Condition INi(String ... statementArray);

  // NOT IN
  /**
   * Character NOT IN case sensitive comparison of statements.
   * @param statementArray
   * @return Condition object
   */
  public Condition NI(String ... statementArray);

  /**
   * Character IN case insensitive comparison of statements.
   * @param statementArray
   * @return Condition object
   */
  public Condition NIi(String ... statementArray);
}
