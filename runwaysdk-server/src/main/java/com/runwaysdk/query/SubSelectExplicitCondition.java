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

import com.runwaysdk.dataaccess.database.Database;

public abstract class SubSelectExplicitCondition extends SubSelectCondition
{
  /**
   * Attribute belonging to the subselect query.
   */
  protected Selectable attributeRight;


  public SubSelectExplicitCondition(Selectable attributeLeft, Selectable attributeRight)
  {
    super(attributeLeft);
    this.attributeRight = attributeRight;
    SubSelectCondition.validateSubSelect(this.selectableLeft, this.attributeRight);
  }

  /**
   * Returns the SQL representation of this condition.
   *
   * @return SQL representation of this condition.
   */
  public String getSQL()
  {
    String sqlStmt = "";

    if (this.ignoreCase)
    {
      sqlStmt += Database.toUpperFunction(this.selectableLeft.getSQL());
    }
    else
    {
      sqlStmt += this.selectableLeft.getSQL();
    }

    String subSelect = this.attributeRight.getRootQuery().getSubSelectSQL(this.attributeRight, this.ignoreCase);

    sqlStmt +=  " "+ this.getSubSelectOperatorSymbol()+" \n("+subSelect+")";

    return sqlStmt;
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
    // Visit this subselect so that we can add an explicit join (if necessary)
    // between the table on the left hand side of the join and other tables that
    // are in its hierarchy.
    visitor.visit(this);
    super.accept(visitor);
    // do not visit the second attribute, as it belongs
    // to the other query object
  //attribute2.accept(visitor);
  }

//
//  private ValueQuery valueObjectQuery;
//
//  /**
//   * Constructs a condition object that explicitly represents a subselect query.
//   * @param attributeLeft
//   * @param valueObjectQuery
//   */
//  protected SubSelectExplicitCondition(Selectable attributeLeft, ValueQuery valueObjectQuery)
//  {
//    super(attributeLeft);
//    this.valueObjectQuery = valueObjectQuery;
//  }
//
//
//  /**
//   *
//   */
//  public String getSQL()
//  {
//    String sql = " ";
//
//    sql += attributeLeft.getSubSelectSQL();
//
//    sql += " "+this.getSubSelectOperatorSymbol()+" \n";
//    sql += "( "+ this.valueObjectQuery.getSQL() + ")";
//
//    return sql;
//  }
//
//
//  public Map<String, String> getFromTableMap()
//  {
//    return null;
//  }
//
//  public Set<Join> getJoinStatements()
//  {
//    return null;
//  }
//
//  public void accept(Visitor visitor)
//  {
//    super.accept(visitor);
//
//    // Do not visit the ValueQueryObject
//  }


}
