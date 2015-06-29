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

import com.runwaysdk.constants.MdAttributeIntegerInfo;
import com.runwaysdk.dataaccess.attributes.AttributeValueException;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.query.sql.MdAttributeInteger_SQL;

public class SelectableSQLInteger extends SelectableSQLNumber implements SelectableInteger
{
  protected SelectableSQLInteger(boolean isAggregate, ValueQuery rootQuery, String attributeName, String sql)
  {
    super(isAggregate, rootQuery, attributeName, sql);
    this.init(attributeName, null);
  }

  protected SelectableSQLInteger(boolean isAggregate, ValueQuery rootQuery, String attributeName, String sql, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    super(isAggregate, rootQuery, attributeName, sql, userDefinedAlias, userDefinedDisplayLabel);
    this.init(attributeName, userDefinedDisplayLabel);
  }

  private void init(String attributeName, String userDefinedDisplayLabel)
  {
    String displayLabel = calculateDisplayLabel(attributeName, userDefinedDisplayLabel);

    this.mdAttributeConcrete_SQL = new MdAttributeInteger_SQL(attributeName, displayLabel);
  }

  // Equals
  /**
   * Integer Equals.
   * @param value
   * @return Basic Condition object
   */
  public BasicCondition EQ(Integer value)
  {
    StatementPrimitive statementPrimitive = this.getNullPrimitiveStatement(value);

    return new BasicConditionEq(this, statementPrimitive);
  }

  // Not Equals
  /**
   * Integer Not Equals.
   * @param value
   * @return Basic Condition object
   */
  public BasicCondition NE(Integer value)
  {
    StatementPrimitive statementPrimitive = this.getNullPrimitiveStatement(value);

    return new BasicConditionNotEq(this, statementPrimitive);
  }

  // Greater Than
  /**
   * Integer Greater Than.
   * @param value
   * @return Basic Condition object
   */
  public BasicCondition GT(Integer value)
  {
    StatementPrimitive statementPrimitive = this.getPrimitiveStatement(value);
    return new BasicConditionGt(this, statementPrimitive);
  }

  // Greater Than or Equal
  /**
   * Integer Greater Than or Equals.
   * @param value
   * @return Basic Condition object
   */
  public BasicCondition GE(Integer value)
  {
    StatementPrimitive statementPrimitive = this.getNullPrimitiveStatement(value);

    return new BasicConditionGtEq(this, statementPrimitive);
  }

  // Less Than
  /**
   * Integer Less Than.
   * @param value
   * @return Basic Condition object
   */
  public BasicCondition LT(Integer value)
  {
    StatementPrimitive statementPrimitive = this.getNullPrimitiveStatement(value);

    return new BasicConditionLt(this, statementPrimitive);
  }

  // Less Than or Equal
  /**
   * Number Less Than or Equal.
   * @param value
   * @return Basic Condition object
   */
  public BasicCondition LE(Integer value)
  {
    StatementPrimitive statementPrimitive = this.getNullPrimitiveStatement(value);

    return new BasicConditionLtEq(this, statementPrimitive);
  }

  /**
   * Validates and formats the given string into an integer format for the
   * current database.
   * @param statement
   * @return StatementPrimitive
   */
  protected StatementPrimitive formatAndValidate(String statement)
  {
    // Throw an exception if this value is invalid
    if (!com.runwaysdk.dataaccess.attributes.entity.AttributeInteger.isValid(statement))
    {
      String error = "Value [" + statement + "] is not valid for query attribute [" + this._getAttributeName() + "]";
      throw new AttributeValueException(error, statement);
    }

    String formattedValue = Database.formatJavaToSQL(statement, MdAttributeIntegerInfo.CLASS, false);
    return new StatementPrimitive(formattedValue);
  }

}
