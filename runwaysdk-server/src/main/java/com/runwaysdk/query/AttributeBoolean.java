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

import java.util.Set;

import com.runwaysdk.dataaccess.MdAttributeBooleanDAOIF;
import com.runwaysdk.dataaccess.MdAttributeStructDAOIF;

public class AttributeBoolean extends AttributePrimitive implements SelectableBoolean
{
  protected AttributeBoolean(MdAttributeBooleanDAOIF mdAttributeIF, String attributeNamespace, String definingTableName,
      String definingTableAlias, ComponentQuery rootQuery, Set<Join> tableJoinSet, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    super(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel);
  }

  protected AttributeBoolean(MdAttributeBooleanDAOIF mdAttributeIF, String attributeNamespace, String definingTableName,
      String definingTableAlias, ComponentQuery rootQuery, Set<Join> tableJoinSet, String userDefinedAlias, String userDefinedDisplayLabel, MdAttributeStructDAOIF mdAttributeStructIF)
  {
    super(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, rootQuery, tableJoinSet, userDefinedAlias, userDefinedDisplayLabel, mdAttributeStructIF);
  }

  // Equals
  /**
   * boolean equal comparison.
   * @param statement
   * @return Basic Condition object
   */
  public BasicCondition EQ(String statement)
  {
    StatementPrimitive statementPrimitive = this.formatAndValidate(statement);
    return new BasicConditionEq(this, statementPrimitive);
  }

  /**
   * boolean equal comparison.
   * @param statement
   * @return Basic Condition object
   */
  public BasicCondition EQ(Boolean statement)
  {
    StatementPrimitive statementPrimitive = this.getNullPrimitiveStatement(statement);

    return new BasicConditionEq(this, statementPrimitive);
  }

  /**
   * Boolean Equals.
   *
   * @param selectable
   * @return Condition object
   */
  public Condition EQ(SelectableBoolean selectable)
  {
    return QueryUtil.EQ(this, selectable);
  }

  // Not Equals
  /**
   * boolean not equal comparison.
   * @param statement
   * @return Basic Condition object
   */
  public BasicCondition NE(String statement)
  {
    StatementPrimitive statementPrimitive = this.formatAndValidate(statement);
    return new BasicConditionNotEq(this, statementPrimitive);
  }

  /**
   * boolean not equal comparison.
   * @param statement
   * @return Basic Condition object
   */
  public BasicCondition NE(Boolean statement)
  {
    StatementPrimitive statementPrimitive = this.getNullPrimitiveStatement(statement);

    return new BasicConditionNotEq(this, statementPrimitive);
  }

  /**
   * Not Equals.
   *
   * @param selectable
   * @return Basic Condition object
   */
  public Condition NE(SelectableBoolean selectable)
  {
    return QueryUtil.NE(this, selectable);
  }

  /**
   * Validates and formats the given string into a boolean format for the
   * current database.
   * @param statement
   * @return StatementPrimitive
   */
  protected StatementPrimitive formatAndValidate(String statement)
  {
    return QueryUtil.formatAndValidateBoolean(statement);
  }


  /**
   * Returns a primitive statement object that may represent a null comparrison.
   * Assumes the given object is a reference to a primitive wrapper class.
   * @param statement
   * @return primitive statement object that may represent a null comparrison.
   */
  private StatementPrimitive getNullPrimitiveStatement(Boolean statement)
  {
    return QueryUtil.getNullPrimitiveStatement(statement);
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
