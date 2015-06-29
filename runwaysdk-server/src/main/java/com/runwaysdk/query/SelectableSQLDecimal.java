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

import java.math.BigDecimal;

import com.runwaysdk.constants.MdAttributeDecimalInfo;
import com.runwaysdk.dataaccess.attributes.AttributeValueException;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.query.sql.MdAttributeDecimal_SQL;

public class SelectableSQLDecimal extends SelectableSQLNumber implements SelectableDecimal
{
  protected SelectableSQLDecimal(boolean isAggregate, ValueQuery rootQuery, String attributeName, String sql)
  {
    super(isAggregate, rootQuery, attributeName, sql);
    this.init(attributeName, null);
  }

  protected SelectableSQLDecimal(boolean isAggregate, ValueQuery rootQuery, String attributeName, String sql, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    super(isAggregate, rootQuery, attributeName, sql, userDefinedAlias, userDefinedDisplayLabel);
    this.init(attributeName, userDefinedDisplayLabel);
  }

  private void init(String attributeName, String userDefinedDisplayLabel)
  {
    String displayLabel = calculateDisplayLabel(attributeName, userDefinedDisplayLabel);

    this.mdAttributeConcrete_SQL = new MdAttributeDecimal_SQL(attributeName, displayLabel);
  }

  // Equals
  /**
   * Decimal Equals.
   * @param bigDecimal
   * @return Basic Condition object
   */
  public BasicCondition EQ(BigDecimal bigDecimal)
  {
    StatementPrimitive statementPrimitive = this.getNullPrimitiveStatement(bigDecimal);

    return new BasicConditionEq(this, statementPrimitive);
  }

  // Not Equals
  /**
   * Decimal Not Equals.
   * @param bigDecimal
   * @return Basic Condition object
   */
  public BasicCondition NE(BigDecimal bigDecimal)
  {
    StatementPrimitive statementPrimitive = this.getNullPrimitiveStatement(bigDecimal);

    return new BasicConditionNotEq(this, statementPrimitive);
  }

  // Greater Than
  /**
   * Decimal Greater Than.
   * @param bigDecimal
   * @return Basic Condition object
   */
  public BasicCondition GT(BigDecimal bigDecimal)
  {
    StatementPrimitive statementPrimitive = this.getPrimitiveStatement(bigDecimal);

    return new BasicConditionGt(this, statementPrimitive);
  }

  // Greater Than or Equal
  /**
   * Decimal Greater Than or Equals.
   * @param bigDecimal
   * @return Basic Condition object
   */
  public BasicCondition GE(BigDecimal bigDecimal)
  {
    StatementPrimitive statementPrimitive = this.getPrimitiveStatement(bigDecimal);

    return new BasicConditionGtEq(this, statementPrimitive);
  }

  // Less Than
  /**
   * Decimal Less Than.
   * @param bigDecimal
   * @return Basic Condition object
   */
  public BasicCondition LT(BigDecimal bigDecimal)
  {
    StatementPrimitive statementPrimitive = this.getPrimitiveStatement(bigDecimal);

    return new BasicConditionLt(this, statementPrimitive);
  }

  // Less Than or Equal
  /**
   * Decimal Less Than or Equal.
   * @param bigDecimal
   * @return Basic Condition object
   */
  public BasicCondition LE(BigDecimal bigDecimal)
  {
    StatementPrimitive statementPrimitive = this.getPrimitiveStatement(bigDecimal);

    return new BasicConditionLtEq(this, statementPrimitive);
  }


  /**
   * Validates and formats the given string into a decimal format for the
   * current database.
   * @param statement
   * @return
   */
  protected StatementPrimitive formatAndValidate(String statement)
  {
    // Throw an exception if this value is invalid
    if (!com.runwaysdk.dataaccess.attributes.entity.AttributeDecimal.isValid(statement))
    {
      String error = "Value [" + statement + "] is not valid for query attribute [" + this._getAttributeName() + "]";
      throw new AttributeValueException(error, statement);
    }

    String formattedValue = Database.formatJavaToSQL(statement, MdAttributeDecimalInfo.CLASS, false);
    return new StatementPrimitive(formattedValue);
  }

}
