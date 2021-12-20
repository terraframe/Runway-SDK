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

public class SubSelectExplicit_NOT_IN_Condition extends SubSelectExplicitCondition
{
  /**
   * Constructs a condition object that explicitly represents a subselect query.
   * @param attributeLeft
   * @param attributeRight
   */
  protected SubSelectExplicit_NOT_IN_Condition(Selectable attributeLeft, Selectable attributeRight)
  {
    super(attributeLeft, attributeRight);
  }

  /**
   * Returns string with a value that is either "IN" or "NOT IN", depedning on what is appropriate for the condition.
   *
   * @return string with a value that is either "IN" or "NOT IN", depedning on what is appropriate for the condition.
   */
  protected String getSubSelectOperatorSymbol()
  {
    return "NOT IN";
  }
}
