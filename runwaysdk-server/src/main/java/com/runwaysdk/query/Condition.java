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

public abstract class Condition implements Expression
{

  public Condition()
  {
    super();
  }
  
  /**
   * Adds a condition to this query.  Will perform an AND
   * with any prior condition previously added.
   * @param condition condition to add.
   * @return new condition that is this condition with an AND performed on
   *   the given condition.
   */
  public Condition AND(Condition ... conditions)
  {   
    Condition loopCondition = this;
    
    for (Condition condition : conditions)
    {
      if (condition == null)
      {
        String errMsg = "Condition is null.";
        throw new QueryException(errMsg);
      }
      else
      {
        loopCondition  = new AND(loopCondition, condition);
      }
    }
    
    return loopCondition;
  }
  
  /**
   * Returns null by default since not all Conditions have a left selectable (e.g., unary conditions).
   * 
   * @return
   */
  protected Selectable getLeftSelectable()
  {
    return null;
  }
  
  /**
   * Adds a condition to this query.  Will perform an OR
   * with any prior condition previously added.
   * @param condition condition to add.
   * @return new condition that is this condition with an OR performed on
   *   the given condition.
   */
  public Condition OR(Condition ... conditions)
  {   
    Condition loopCondition = this;
    
    for (Condition condition : conditions)
    {
      if (condition == null)
      {
        String errMsg = "Condition is null.";
        throw new QueryException(errMsg);
      }
      else
      {
        loopCondition  = new OR(loopCondition, condition);
      }
    }
    
    return loopCondition;
  }
}
