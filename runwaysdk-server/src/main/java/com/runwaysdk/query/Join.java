/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
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

public abstract class Join implements Component
{  
  protected Selectable selectable1;
  protected Selectable selectable2;
  
  protected String expression1;
  protected String expression2;
  
  protected String columnName1;
  protected String tableName1;
  protected String tableAlias1;
  protected String columnName2;
  protected String tableName2;
  protected String tableAlias2;

  /**
   * Represents a join between tables in the query.
   * @param columnName1  column name in the left side of the join
   * @param tableName1   name of the table that defines attribute1
   * @param tableAlias1  alias of the table that defines attribute1
   * @param columnName2  column name in the right side of the join
   * @param tableName2   name of the table that defines attribute2
   * @param tableAlias2  alias of the table that defines attribute2
   */
  protected Join(String columnName1, String tableName1, String tableAlias1, String columnName2, String tableName2, String tableAlias2)
  {
    super();
  
    this.columnName1  = columnName1;
    this.tableName1  = tableName1;
    this.tableAlias1 = tableAlias1;
    this.columnName2  = columnName2;
    this.tableName2  = tableName2;
    this.tableAlias2 = tableAlias2;
  
    this.expression1 = this.tableAlias1+"."+columnName1;
    this.expression2 = this.tableAlias2+"."+columnName2;
  }
  
  /**
   * Represents a join between tables in the query.
   * @param selectable1
   * @param selectable2
   */
  protected Join(Selectable selectable1, Selectable selectable2)
  {
    if (selectable1.isAggregateFunction())
    {
      String errMsg = "aggregates not allowed in JOIN conditions ["+selectable1.getColumnAlias()+"]";
      throw new QueryException(errMsg);
    }
    else if (selectable2.isAggregateFunction())
    {
      String errMsg = "aggregates not allowed in JOIN conditions ["+selectable2.getColumnAlias()+"]";
      throw new QueryException(errMsg);
    }
    
    this.selectable1 = selectable1;
    this.selectable2 = selectable2;

    this.columnName1  = selectable1.getAttribute().getDbColumnName();
    this.columnName2  = selectable2.getAttribute().getDbColumnName();

    this.tableName1  = this.selectable1.getDefiningTableName();
    this.tableAlias1 = this.selectable1.getDefiningTableAlias();
    this.tableName2  = this.selectable2.getDefiningTableName();
    this.tableAlias2 = this.selectable2.getDefiningTableAlias();
    
    this.expression1 = this.selectable1.getDefiningTableName()+"."+this.selectable1.getAttribute().getDbColumnName();
    this.expression2 = this.selectable2.getDefiningTableName()+"."+this.selectable2.getAttribute().getDbColumnName();
  }

  /**
   * Returns the alias of the table that defines attribute1.
   *  
   * @return alias of the table that defines attribute1.
   */
  protected String getTableAlias1()
  {
    return tableAlias1;
  }

  /**
   * Returns the alias of the table that defines attribute2.
   *  
   * @return alias of the table that defines attribute2.
   */
  protected String getTableAlias2()
  {
    return tableAlias2;
  }

  /**
   * Returns the name of the table that defines attribute1.
   *  
   * @return name of the table that defines attribute1.
   */
  protected String getTableName1()
  {
   return tableName1;
  }

  /**
   * Returns the name of the table that defines attribute2.
   *  
   * @return name of the table that defines attribute2.
   */
  protected String getTableName2()
  {
    return tableName2;
  }
  
  /**
   * Returns the name of the first attribute.
   *  
   * @return  name of the first attribute.
   */
  protected String getColumnName1()
  {
   return this.columnName1;
  }

  /**
   * Returns the name of the first attribute.
   *  
   * @return  name of the first attribute.
   */
  protected String getColumnName2()
  {
   return this.columnName2;
  }
  
  
  /**
   * Returns 0 if the given TableJoin represents the same join between
   * attributes and tables as this TableJoin object.
   * 
   * @param object TableJoin object
   * @return 0 if the given TableJoin represents the same join between
   * attributes and tables as this TableJoin object.
  */
  public boolean equals(Object object)
  {
    boolean returnValue = false;
    
    if ( !(object instanceof Join))
    {
      returnValue =  false;
    }
    else
    {
      Join tableJoin = (Join)object;

      if (
           (tableJoin.expression1.equals(this.expression1) &&
            tableJoin.expression2.equals(this.expression2)) 
           ||
           (tableJoin.expression2.equals(this.expression1) &&
            tableJoin.expression1.equals(this.expression2))
         )
      {
        returnValue = true;
      }
    }

    return returnValue;
  }
     
  public int hashCode()
  {
    return new String(this.expression1+this.expression2).hashCode();
  }

  
  /**
   * Returns a SQL String that represents the join.
   * @return SQL String that represents the join.
   */
  public abstract String getSQL();
  
  /**
   * Returns a SQL String that represents the join.
   * @return SQL String that represents the join.
   */
  public String toString()
  {
    return this.getSQL();
  }
  
  /**
   * Visitor to traverse the query object structure.
   * @param visitor
   */
  public void accept(Visitor visitor)
  {
    // Only get the criteria if it is an EntityQuery.  Otherwise, 
    // criteria in a nested select will also incorrectly appear in
    // the enclosing value query.
    if (this.selectable1 != null && this.selectable1.getRootQuery() instanceof EntityQuery)
    {
      this.selectable1.accept(visitor);
    }

    if (this.selectable2 != null && this.selectable2.getRootQuery() instanceof EntityQuery)
    {
      this.selectable2.accept(visitor);
    }
  }
  
  /**
   * Returns a Set of TableJoin objects that represent joins statements
   * that are required for this expression.
   * @return Set of TableJoin objects that represent joins statements
   * that are required for this expression.
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
