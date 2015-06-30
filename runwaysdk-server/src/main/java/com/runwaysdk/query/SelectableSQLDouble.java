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

import com.runwaysdk.constants.MdAttributeDoubleInfo;
import com.runwaysdk.dataaccess.attributes.AttributeValueException;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.query.sql.MdAttributeDouble_SQL;

public class SelectableSQLDouble extends SelectableSQLNumber implements SelectableDouble
{

  protected SelectableSQLDouble(boolean isAggregate, ValueQuery rootQuery, String attributeName, String sql)
  {
    super(isAggregate, rootQuery, attributeName, sql);
    this.init(attributeName, null);
  }

  protected SelectableSQLDouble(boolean isAggregate, ValueQuery rootQuery, String attributeName, String sql, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    super(isAggregate, rootQuery, attributeName, sql, userDefinedAlias, userDefinedDisplayLabel);
    this.init(attributeName, userDefinedDisplayLabel);
  }

  private void init(String attributeName, String userDefinedDisplayLabel)
  {
    String displayLabel = calculateDisplayLabel(attributeName, userDefinedDisplayLabel);

    this.mdAttributeConcrete_SQL = new MdAttributeDouble_SQL(attributeName, displayLabel);
  }

  // Equals
  /**
   * Double Equals.
   * @param value
   * @return Basic Condition object
   */
  public BasicCondition EQ(Double value)
  {
    StatementPrimitive statementPrimitive = this.getNullPrimitiveStatement(value);

    return new BasicConditionEq(this, statementPrimitive);
  }


  // Not Equals
  /**
   * Double Not Equals.
   * @param value
   * @return Basic Condition object
   */
  public BasicCondition NE(Double value)
  {
    StatementPrimitive statementPrimitive = this.getNullPrimitiveStatement(value);

    return new BasicConditionNotEq(this, statementPrimitive);
  }

  // Greater Than
  /**
   * Double Greater Than.
   * @param value
   * @return Basic Condition object
   */
  public BasicCondition GT(Double value)
  {
    StatementPrimitive statementPrimitive = this.getPrimitiveStatement(value);

    return new BasicConditionGt(this, statementPrimitive);
  }


  // Greater Than or Equal
  /**
   * Double Greater Than or Equals.
   * @param value
   * @return Basic Condition object
   */
  public BasicCondition GE(Double value)
  {
    StatementPrimitive statementPrimitive = this.getPrimitiveStatement(value);

    return new BasicConditionGtEq(this, statementPrimitive);
  }

  // Less Than
  /**
   * Double Less Than.
   * @param value
   * @return Basic Condition object
   */
  public BasicCondition LT(Double value)
  {
    StatementPrimitive statementPrimitive = this.getPrimitiveStatement(value);

    return new BasicConditionLt(this, statementPrimitive);
  }


  // Less Than or Equal
  /**
   * Double Less Than or Equal.
   * @param value
   * @return Basic Condition object
   */
  public BasicCondition LE(Double value)
  {
    StatementPrimitive statementPrimitive = this.getPrimitiveStatement(value);

    return new BasicConditionLtEq(this, statementPrimitive);
  }

  /**
   * Validates and formats the given string into a double format for the
   * current database.
   * @param statement
   * @return
   */
  protected StatementPrimitive formatAndValidate(String statement)
  {
    // Throw an exception if this value is invalid
    if (!com.runwaysdk.dataaccess.attributes.entity.AttributeDouble.isValid(statement))
    {
      String error = "Value [" + statement + "] is not valid for query attribute [" + this._getAttributeName() + "]";
      throw new AttributeValueException(error, statement);
    }

    String formattedValue = Database.formatJavaToSQL(statement, MdAttributeDoubleInfo.CLASS, false);
    return new StatementPrimitive(formattedValue);
  }

}
