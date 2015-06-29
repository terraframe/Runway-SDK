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

import com.runwaysdk.dataaccess.database.Database;

public abstract class BasicConditionSimple extends BasicCondition
{  
  protected Statement statement;
    
  protected BasicConditionSimple(Selectable attributeLeft, Statement statement)
  {
    super(attributeLeft);
    this.selectableLeft = attributeLeft;
    this.statement = statement;
  }
  
  protected BasicConditionSimple(Selectable attributeLeft, Statement statement, boolean ignoreCase)
  {
    super(attributeLeft, ignoreCase);
    this.selectableLeft = attributeLeft;
    this.statement = statement;
  }
  
  /**
   * Returns the string representation of the operator used in this condition.
   * 
   * @return string representation of the operator used in this condition.
   */
  protected abstract String getOperatorSymbol();
  
  /**
   * Returns the SQL representation of this condition.
   *
   * @return SQL representation of this condition.
   */
  public String getSQL()
  {
    String statementSQL1 = null;
    String statementSQL2 = null;
    
    if (this.ignoreCase)
    {
      statementSQL1 = Database.toUpperFunction(this.selectableLeft.getSQL());
      statementSQL2 = Database.toUpperFunction(this.statement.getSQL());
    }
    else
    {
      statementSQL1 = this.selectableLeft.getSQL();
      statementSQL2 = this.statement.getSQL();
    }
    
    return statementSQL1+" "+this.getOperatorSymbol()+" "+statementSQL2;
  }
  
  /**
   * Visitor to traverse the query object structure.
   * @param visitor
   */
  public void accept(Visitor visitor)
  {
    super.accept(visitor);
    statement.accept(visitor);
  }
}
