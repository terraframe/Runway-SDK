/*******************************************************************************
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
 ******************************************************************************/
package com.runwaysdk.query;


public abstract class SelectableSQLNumber extends SelectableSQLPrimitive implements SelectableNumber
{

  protected SelectableSQLNumber(boolean isAggregate, ValueQuery rootQuery, String attributeName, String sql)
  {
    super(isAggregate, rootQuery, attributeName, sql);
  }

  protected SelectableSQLNumber(boolean isAggregate, ValueQuery rootQuery, String attributeName, String sql, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    super(isAggregate, rootQuery, attributeName, sql, userDefinedAlias, userDefinedDisplayLabel);
  }


  // Equals
  /**
   * Number Equals.
   *
   * @param statement
   * @return Basic Condition object
   */
  public BasicCondition EQ(String statement)
  {
    StatementPrimitive statementPrimitive = this.formatAndValidate(statement);
    return new BasicConditionEq(this, statementPrimitive);
  }

  /**
   * Moment Equals.
   *
   * @param selectable
   * @return Condition object
   */
  public Condition EQ(SelectableNumber selectable)
  {
    return QueryUtil.EQ(this, selectable);
  }

  // Not Equals
  /**
   * Number Not Equals.
   *
   * @param statement
   * @return Basic Condition object
   */
  public BasicCondition NE(String statement)
  {
    StatementPrimitive statementPrimitive = this.formatAndValidate(statement);
    return new BasicConditionNotEq(this, statementPrimitive);
  }

  /**
   * Not Equals.
   *
   * @param selectable
   * @return Basic Condition object
   */
  public Condition NE(SelectableNumber selectable)
  {
    return QueryUtil.NE(this, selectable);
  }

  // Greater Than
  /**
   * Number Greater Than.
   *
   * @param statement
   * @return Basic Condition object
   */
  public BasicCondition GT(String statement)
  {
    StatementPrimitive statementPrimitive = this.formatAndValidate(statement);
    return new BasicConditionGt(this, statementPrimitive);
  }

  /**
   * Greater Than.
   *
   * @param selectable
   * @return Condition object
   */
  public Condition GT(SelectableNumber selectable)
  {
    return QueryUtil.GT(this, selectable);
  }

  // Greater Than or Equal
  /**
   * Number Greater Than or Equals.
   *
   * @param statement
   * @return Basic Condition object
   */
  public BasicCondition GE(String statement)
  {
    StatementPrimitive statementPrimitive = this.formatAndValidate(statement);
    return new BasicConditionGtEq(this, statementPrimitive);
  }

  /**
   * Greater Than or Equals.
   *
   * @param selectable
   * @return Condition object
   */
  public Condition GE(SelectableNumber selectable)
  {
    return QueryUtil.GE(this, selectable);
  }

  // Less Than
  /**
   * Number Less Than.
   *
   * @param statement
   * @return Basic Condition object
   */
  public BasicCondition LT(String statement)
  {
    StatementPrimitive statementPrimitive = this.formatAndValidate(statement);
    return new BasicConditionLt(this, statementPrimitive);
  }

  /**
   * Less Than.
   *
   * @param selectable
   * @return Condition object
   */
  public Condition LT(SelectableNumber selectable)
  {
    return QueryUtil.LT(this, selectable);
  }

  // Less Than or Equal
  /**
   * Decimal Less Than or Equal.
   *
   * @param statement
   * @return Basic Condition object
   */
  public BasicCondition LE(String statement)
  {
    StatementPrimitive statementPrimitive = this.formatAndValidate(statement);
    return new BasicConditionLtEq(this, statementPrimitive);
  }

  /**
   * Less Than or Equal.
   *
   * @param selectable
   * @return Condition object
   */
  public Condition LE(SelectableNumber selectable)
  {
    return QueryUtil.LE(this, selectable);
  }

  /**
   * Returns a condition based on the String version of the operator
   * and the String version of the value.
   * @param operator
   * @param value
   * @return condition based on the String version of the operator
   * and the String version of the value.
   */
  public Condition getCondition(String operator, String value)
  {
    return QueryUtil.getCondition(this, operator, value);
  }

}
