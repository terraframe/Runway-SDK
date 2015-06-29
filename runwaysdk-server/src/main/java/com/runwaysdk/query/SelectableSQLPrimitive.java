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

public abstract class SelectableSQLPrimitive extends SelectableSQL
{

  protected SelectableSQLPrimitive(boolean isAggregate, ValueQuery rootQuery, String attributeName, String sql)
  {
    super(isAggregate, rootQuery, attributeName, sql);
  }

  protected SelectableSQLPrimitive(boolean isAggregate, ValueQuery rootQuery, String attributeName, String sql, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    super(isAggregate, rootQuery, attributeName, sql, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns a primitive statement object that may not represent a null comparrison.
   * @param statement
   * @return primitive statement object that may not represent a null comparrison.
   */
  protected StatementPrimitive getPrimitiveStatement(Object statement)
  {
    StatementPrimitive statementPrimitive;

    if (statement == null)
    {
      String error = "Parameter may not be null";
      throw new QueryException(error);
    }
    else
    {
      statementPrimitive = this.formatAndValidate(statement.toString());
    }
    return statementPrimitive;
  }

  /**
   * Returns a primitive statement object that may represent a null comparrison.
   * Assumes the given object is a reference to a primitive wrapper class.
   * @param statement
   * @return primitive statement object that may represent a null comparrison.
   */
  protected StatementPrimitive getNullPrimitiveStatement(Object statement)
  {
    StatementPrimitive statementPrimitive;

    if (statement == null)
    {
      statementPrimitive = new StatementPrimitive("NULL");
    }
    else
    {
      statementPrimitive = this.formatAndValidate(statement.toString());
    }

    return statementPrimitive;
  }

  /**
   * Validates and formats the given string for the current database.
   * @param statement
   * @return
   */
  protected abstract StatementPrimitive formatAndValidate(String statement);

}
