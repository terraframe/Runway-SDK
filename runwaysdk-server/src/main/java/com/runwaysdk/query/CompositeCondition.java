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

import java.util.Map;
import java.util.Set;

public abstract class CompositeCondition extends Condition
{
  protected Condition         condition1;
  protected ConditionOperator compositeOperator;
  protected Condition         condition2;
  
  protected CompositeCondition(Condition condition1, ConditionOperator compositeOperator, Condition condition2)
  {
    super();
    
    this.condition1        = condition1;
    this.compositeOperator = compositeOperator;
    this.condition2        = condition2;
  }
  
  /**
   * Returns the SQL representation of this condition.
   * @return SQL representation of this condition.
   */
  public String getSQL()
  {
    return "("+condition1.getSQL()+"\n"+compositeOperator.getOperatorString()+" "+condition2.getSQL()+")";
  }
  
  /**
   * Returns a Set of TableJoin objects that represent joins statements
   * that are required for this expression.
   * @return Set of TableJoin objects that represent joins statements
   * that are required for this expression, or null of there are none.
   */
  public Set<Join> getJoinStatements()
  {
    return null;
  }

  /**
   * Returns a Map representing tables that should be included in the from clause,
   * where the key is the table alias and the value is the name of the table.
   * @return Map representing tables that should be included in the from clause,
   * where the key is the table alias and the value is the name of the table.
   */
  public Map<String, String> getFromTableMap()
  {
    return null;
  }
  
  /**
   * Visitor to traverse the query object structure.
   * @param visitor
   */
  public void accept(Visitor visitor)
  {
    condition1.accept(visitor);
    condition2.accept(visitor);
  }
}
