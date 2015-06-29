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

import java.util.Date;
import java.util.Set;

import com.runwaysdk.dataaccess.MdAttributeMomentDAOIF;
import com.runwaysdk.dataaccess.MdAttributeStructDAOIF;

public abstract class AttributeMoment extends AttributePrimitive implements SelectableMoment
{

  protected AttributeMoment(MdAttributeMomentDAOIF mdAttributeIF, String attributeNamespace,
      String definingTableName, String definingTableAlias, ComponentQuery rootQuery,
      Set<Join> tableJoinSet, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    super(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, rootQuery,
        tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
  }

  public AttributeMoment(MdAttributeMomentDAOIF mdAttributeIF, String attributeNamespace,
      String definingTableName, String definingTableAlias, ComponentQuery rootQuery,
      Set<Join> tableJoinSet, String userDefinedAlias, String userDefinedDisplayLabel, MdAttributeStructDAOIF mdAttributeStructIF)
  {
    super(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, rootQuery,
        tableJoinSet, userDefinedAlias, userDefinedDisplayLabel, mdAttributeStructIF);
  }

  /**
   * Formats the given moment into the string value.
   *
   * @param date
   * @return given moment into the string value.
   */
  protected abstract String formatMoment(Date date);

  // Equals
  /**
   * Date Equals.
   *
   * @param statement
   * @return Basic Condition object
   */
  public BasicCondition EQ(String statement)
  {
    StatementPrimitive statementPrimitive = this.getNullPrimitiveStatement(statement);
    return new BasicConditionEq(this, statementPrimitive);
  }

  /**
   * Date Equals.
   *
   * @param date
   * @return Basic Condition object
   */
  public BasicCondition EQ(Date date)
  {
    StatementPrimitive statementPrimitive = this.getNullPrimitiveStatement(date);

    return new BasicConditionEq(this, statementPrimitive);
  }

  /**
   * Moment Equals.
   *
   * @param selectable
   * @return Condition object
   */
  public Condition EQ(SelectableMoment selectable)
  {
    return QueryUtil.EQ(this, selectable);
  }

  // Not Equals
  /**
   * Date Not Equals.
   *
   * @param statement
   * @return Basic Condition object
   */
  public BasicCondition NE(String statement)
  {
    StatementPrimitive statementPrimitive = this.getNullPrimitiveStatement(statement);
    return new BasicConditionNotEq(this, statementPrimitive);
  }

  /**
   * Date Equals.
   *
   * @param date
   * @return Basic Condition object
   */
  public BasicCondition NE(Date date)
  {
    StatementPrimitive statementPrimitive = this.getNullPrimitiveStatement(date);

    return new BasicConditionNotEq(this, statementPrimitive);
  }

  /**
   * Not Equals.
   *
   * @param selectable
   * @return Basic Condition object
   */
  public Condition NE(SelectableMoment selectable)
  {
    return QueryUtil.NE(this, selectable);
  }

  // Greater Than
  /**
   * Date Greater Than.
   *
   * @param statement
   * @return Basic Condition object
   */
  public BasicCondition GT(String statement)
  {
    StatementPrimitive statementPrimitive = this.getNullPrimitiveStatement(statement);
    return new BasicConditionGt(this, statementPrimitive);
  }

  /**
   * Greater Than.
   *
   * @param selectable
   * @return Condition object
   */
  public Condition GT(SelectableMoment selectable)
  {
    return QueryUtil.GT(this, selectable);
  }

  /**
   * Date Greater Than.
   *
   * @param date
   * @return Basic Condition object
   */
  public BasicCondition GT(Date date)
  {
    StatementPrimitive statementPrimitive = this.getPrimitiveStatement(date);

    return new BasicConditionGt(this, statementPrimitive);
  }

  // Greater Than or Equal
  /**
   * Date Greater Than or Equals.
   *
   * @param statement
   * @return Basic Condition object
   */
  public BasicCondition GE(String statement)
  {
    StatementPrimitive statementPrimitive = this.getNullPrimitiveStatement(statement);
    return new BasicConditionGtEq(this, statementPrimitive);
  }

  // Greater Than or Equal
  /**
   * Date Greater Than or Equals.
   *
   * @param statement
   * @return Basic Condition object
   */
  public BasicCondition GE(Date date)
  {
    StatementPrimitive statementPrimitive = this.getPrimitiveStatement(date);

    return new BasicConditionGtEq(this, statementPrimitive);
  }

  /**
   * Greater Than or Equals.
   *
   * @param selectable
   * @return Condition object
   */
  public Condition GE(SelectableMoment selectable)
  {
    return QueryUtil.GE(this, selectable);
  }

  // Less Than
  /**
   * Date Less Than.
   *
   * @param statement
   * @return Basic Condition object
   */
  public BasicCondition LT(String statement)
  {
    StatementPrimitive statementPrimitive = this.getNullPrimitiveStatement(statement);
    return new BasicConditionLt(this, statementPrimitive);
  }

  /**
   * Date Less Than.
   *
   * @param date
   * @return Basic Condition object
   */
  public BasicCondition LT(Date date)
  {
    StatementPrimitive statementPrimitive = this.getPrimitiveStatement(date);

    return new BasicConditionLt(this, statementPrimitive);
  }

  /**
   * Less Than.
   *
   * @param selectable
   * @return Condition object
   */
  public Condition LT(SelectableMoment selectable)
  {
    return QueryUtil.LT(this, selectable);
  }

  // Less Than or Equal
  /**
   * Date Less Than or Equal.
   *
   * @param statement
   * @return Basic Condition object
   */
  public BasicCondition LE(String statement)
  {
    StatementPrimitive statementPrimitive = this.getNullPrimitiveStatement(statement);
    return new BasicConditionLtEq(this, statementPrimitive);
  }

  /**
   * Date Less Than or Equal.
   *
   * @param statement
   * @return Basic Condition object
   */
  public BasicCondition LE(Date date)
  {
    StatementPrimitive statementPrimitive = this.getPrimitiveStatement(date);

    return new BasicConditionLtEq(this, statementPrimitive);
  }

  /**
   * Less Than or Equal.
   *
   * @param selectable
   * @return Condition object
   */
  public Condition LE(SelectableMoment selectable)
  {
    return QueryUtil.LE(this, selectable);
  }

  /**
   * Returns a primitive statement object that may represent a null comparrison.
   * Assumes the given object is a reference to a primitive wrapper class.
   *
   * @param statement
   * @return primitive statement object that may represent a null comparrison.
   */
  private StatementPrimitive getNullPrimitiveStatement(Date date)
  {
    StatementPrimitive statementPrimitive;

    if (date == null)
    {
      statementPrimitive = new StatementPrimitive("NULL");
    }
    else
    {
      String simpleDateFormat = this.formatMoment(date);
      statementPrimitive = this.formatAndValidate(simpleDateFormat);
    }
    return statementPrimitive;
  }

  /**
   * Returns a primitive statement object that may represent a null comparrison.
   * Assumes the given object is a reference to a primitive wrapper class.
   * @param statement
   * @return primitive statement object that may represent a null comparrison.
   */
  protected StatementPrimitive getNullPrimitiveStatement(String statement)
  {
    StatementPrimitive statementPrimitive;

    if (statement == null)
    {
      statementPrimitive = new StatementPrimitive("NULL");
    }
    else
    {
      statementPrimitive = this.formatAndValidate(statement);
    }

    return statementPrimitive;
  }

  
  /**
   * Returns a primitive statement object that may not represent a null
   * comparrison.
   *
   * @param statement
   * @return primitive statement object that may not represent a null
   *         comparrison.
   */
  private StatementPrimitive getPrimitiveStatement(Date date)
  {
    StatementPrimitive statementPrimitive;

    if (date == null)
    {
      String error = "date parameter may not be null";
      throw new QueryException(error);
    }
    else
    {
      String simpleDateFormat = this.formatMoment(date);
      statementPrimitive = this.formatAndValidate(simpleDateFormat);
    }
    return statementPrimitive;
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
