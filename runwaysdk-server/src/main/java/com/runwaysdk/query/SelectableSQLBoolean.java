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

import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.query.sql.MdAttributeBoolean_SQL;

public class SelectableSQLBoolean extends SelectableSQLPrimitive implements SelectableBoolean
{

  protected SelectableSQLBoolean(boolean isAggregate, ValueQuery rootQuery, String attributeName, String sql)
  {
    super(isAggregate, rootQuery, attributeName, sql);
    this.init(attributeName, null);
  }

  protected SelectableSQLBoolean(boolean isAggregate, ValueQuery rootQuery, String attributeName, String sql, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    super(isAggregate, rootQuery, attributeName, sql, userDefinedAlias, userDefinedDisplayLabel);
    this.init(attributeName, userDefinedDisplayLabel);
  }

  private void init(String attributeName, String userDefinedDisplayLabel)
  {
    String displayLabel = calculateDisplayLabel(attributeName, userDefinedDisplayLabel);

    this.mdAttributeConcrete_SQL = new MdAttributeBoolean_SQL(attributeName, displayLabel);
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
   * Formats and validates a character string.
   * @param statement
   * @return
   */
  protected StatementPrimitive formatAndValidate(String statement)
  {
    String formattedValue = Database.formatJavaToSQL(statement, MdAttributeBooleanInfo.CLASS, false);
    return new StatementPrimitive(formattedValue);
  }

  /**
   * Returns a condition based on the String version of the operator
   * and the String version of the value.
   * @param operator
   * @param value
   * @return condition based on the String version of the operator
   * and the String version of the value.
   */
  @Override
  public Condition getCondition(String operator, String value)
  {
    return QueryUtil.getCondition(this, operator, value);
  }

}
