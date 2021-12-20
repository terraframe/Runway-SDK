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

import java.util.Map;
import java.util.Set;

import com.runwaysdk.dataaccess.database.Database;

public abstract class ValueJoinCondition extends BasicCondition
{  
  /**
   * Attribute belonging to the subselect query.
   */
  protected Selectable attributeRight;
  
  
  public ValueJoinCondition(Selectable attributeLeft, Selectable attributeRight)
  {
    super(attributeLeft);
    this.attributeRight = attributeRight;
    SubSelectCondition.validateSubSelect(attributeLeft, this.attributeRight);
  }
  
  public ValueJoinCondition(Selectable attributeLeft, Selectable attributeRight, boolean ignoreCase)
  {
    super(attributeLeft, ignoreCase);
    this.attributeRight = attributeRight;
    SubSelectCondition.validateSubSelect(attributeLeft, this.attributeRight);
  }
  
  /**
   * Returns the SQL representation of this condition.
   *
   * @return SQL representation of this condition.
   */
  public String getSQL()
  {
    String sqlStmt = "";
    
    String additionalCriteria = "";
    
    ComponentQuery rightComponentQuery = this.attributeRight.getRootQuery();
    
    // Only get the criteria if it is an EntityQuery.  Otherwise, 
    // criteria in a nested select will also incorrectly appear in
    // the enclosing value query.
    if (rightComponentQuery instanceof TableClassQuery)
    {
      additionalCriteria = rightComponentQuery.getQueryConditionSQL();
    }

    String statementSQL1 = null;
    String statementSQL2 = null;
    
    if (this.ignoreCase)
    {
      statementSQL1 = Database.toUpperFunction(this.selectableLeft.getSQL());
      statementSQL2 = Database.toUpperFunction(this.attributeRight.getSQL());
    }
    else
    {
      statementSQL1 = this.selectableLeft.getSQL();
      statementSQL2 = this.attributeRight.getSQL();
    }

    sqlStmt += statementSQL1+" "+this.getOperatorSymbol()+" "+statementSQL2;

    if (additionalCriteria.toString().trim().length() != 0)
    {
      sqlStmt += " \nAND "+additionalCriteria;
    }
    
    return sqlStmt;
    
  }
  
  /**
   * Returns the string representation of the operator used in this condition.
   * 
   * @return string representation of the operator used in this condition.
   */
  public abstract String getOperatorSymbol();
  
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
    super.accept(visitor);
    this.attributeRight.accept(visitor);
  }
  
}
