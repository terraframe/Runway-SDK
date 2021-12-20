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

public class StatementSubSelectCondition extends Condition
{
  protected Statement statement1;
  
  protected Expression expression;

  protected boolean ignoreCase;
  
  protected StatementSubSelectCondition(Statement statement1, Expression expression, boolean ignoreCase)
  {
    super();
    this.statement1 = statement1;
    this.expression = expression;
    this.ignoreCase = ignoreCase;
  }

  /**
   * Returns the string representation of the operator used in this condition.
   * 
   * @return string representation of the operator used in this condition.
   */
  protected String getOperatorSymbol()
  {
    return " IN ";
  }
  
  /**
   * Returns the SQL representation of this condition.
   *
   * @return SQL representation of this condition.
   */
  public String getSQL()
  {
    String statementSQL1 = null;
    String statementSQL2 = null;;
    
    if (this.ignoreCase)
    {
      statementSQL1 = Database.toUpperFunction(this.statement1.getSQL());
      statementSQL2 = Database.toUpperFunction(this.expression.getSQL());
    }
    else
    {
      statementSQL1 = this.statement1.getSQL();
      statementSQL2 = this.expression.getSQL();
    }
    
    return statementSQL1+" "+this.getOperatorSymbol()+" "+statementSQL2;
  }
  
  /**
   * Visitor to traverse the query object structure.
   * @param visitor
   */
  public void accept(Visitor visitor)
  {
    statement1.accept(visitor);
    expression.accept(visitor);
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


}
