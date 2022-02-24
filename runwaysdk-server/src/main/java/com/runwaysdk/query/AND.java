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

public class AND extends CompositeCondition
{
  protected AND(Condition condition1, Condition condition2)
  {
    super(condition1, ConditionOperator.AND, condition2);
  }

  /**
   * Performs an AND to all given conditions.
   * @param conditions conditions to AND.
   * @return conditions ORed together
   */
  public static Condition get(Condition ... conditions)
  {   
    if (conditions == null)
    {
      String errMsg = "Condition is null.";
      throw new QueryException(errMsg);
    }
    else if (conditions.length == 0)
    {
      String errMsg = "No conditions provided";
      throw new QueryException(errMsg);
    }
    else if (conditions.length == 1)
    {
      if (conditions[0] == null)
      {
        String errMsg = "Condition is null.";
        throw new QueryException(errMsg);
      }
      else
      {
        return conditions[0];
      }
    }
    else
    {
      Condition loopCondition = conditions[0];

      for (int i=1; i<conditions.length; i++)
      {
        if (conditions[i] == null)
        {
          String errMsg = "Condition is null.";
          throw new QueryException(errMsg);
        }
        else
        {
          loopCondition  = new AND(loopCondition, conditions[i]);
        }
      }
      
      return loopCondition;
    }
  }
 
}
