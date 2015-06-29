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

public abstract class BasicConditionInOrNotIn extends BasicCondition
{  
  protected StatementPrimitive[] statementPrimitiveArray;
  
  protected BasicConditionInOrNotIn(Selectable attributeLeft, StatementPrimitive[] statementPrimitiveArray)
  {
    super(attributeLeft);

    this.statementPrimitiveArray = statementPrimitiveArray;
  }

  protected BasicConditionInOrNotIn(Selectable attributeLeft, StatementPrimitive[] statementPrimitiveArray, boolean ignoreCase)
  {
    super(attributeLeft, ignoreCase);

    this.statementPrimitiveArray = statementPrimitiveArray;
  }
  
  /**
   * Returns the SQL representation of this condition.
   * 
   * @return SQL representation of this condition.
   */
  public String getSQL()
  {
    String sqlString = null;
    
    if (this.ignoreCase)
    {
      sqlString = Database.toUpperFunction(this.selectableLeft.getSQL());
    }
    else
    {
      sqlString = this.selectableLeft.getSQL();
    }

    sqlString += " "+this.getOperatorSymbol()+" (";

    for (int i=0; i<this.statementPrimitiveArray.length; i++)
    {
      if (i != 0)
      {
        sqlString +=", ";
      }
      
      Statement statement = this.statementPrimitiveArray[i];
      
      if(this.ignoreCase)
      {
        sqlString += Database.toUpperFunction(statement.getSQL());
      }
      else
      {
        sqlString += statement.getSQL();        
      } 
    }
    
    return sqlString+")";
  }
  
  /**
   * Visitor to traverse the query object structure.
   * @param visitor
   */
  public void accept(Visitor visitor)
  {
    super.accept(visitor);
    
    for (Statement statement : statementPrimitiveArray)
    {
      statement.accept(visitor); 
    }
  }

  /**
   * Returns the string representation of the operator used in this condition.
   * 
   * @return string representation of the operator used in this condition.
   */
  protected abstract String getOperatorSymbol();
  
}
