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

public class SubSelectBasicConditionLtEq extends SubSelectBasicCondition
{

  public SubSelectBasicConditionLtEq(Selectable attribute1, Selectable attribute2)
  {
    super(attribute1, attribute2);
  }

  public SubSelectBasicConditionLtEq(Selectable attribute1, Selectable attribute2, boolean ignoreCase)
  {
    super(attribute1, attribute2, ignoreCase);
  }
  /**
   * Returns the string representation of the operator used in this condition.
   * 
   * @return string representation of the operator used in this condition.
   */
  public String getOperatorSymbol()
  {
    // This is not a type-o.  The operator symbol is correct
    return ">=";
  }
}
