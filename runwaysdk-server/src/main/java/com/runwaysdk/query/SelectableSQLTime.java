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

import java.text.SimpleDateFormat;
import java.util.Date;

import com.runwaysdk.constants.Constants;
import com.runwaysdk.constants.MdAttributeTimeInfo;
import com.runwaysdk.dataaccess.attributes.AttributeValueException;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.query.sql.MdAttributeTime_SQL;

public class SelectableSQLTime extends SelectableSQLMoment
{

  protected SelectableSQLTime(boolean isAggregate, ValueQuery rootQuery, String attributeName, String sql)
  {
    super(isAggregate, rootQuery, attributeName, sql);
    this.init(attributeName, null);
  }

  protected SelectableSQLTime(boolean isAggregate, ValueQuery rootQuery, String attributeName, String sql, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    super(isAggregate, rootQuery, attributeName, sql, userDefinedAlias, userDefinedDisplayLabel);
    this.init(attributeName, userDefinedDisplayLabel);
  }

  private void init(String attributeName, String userDefinedDisplayLabel)
  {
    String displayLabel = calculateDisplayLabel(attributeName, userDefinedDisplayLabel);

    this.mdAttributeConcrete_SQL = new MdAttributeTime_SQL(attributeName, displayLabel);
  }

  /**
   * Formats the given moment into the string value.
   * @param date
   * @return given moment into the string value.
   */
  @Override
  protected String formatMoment(Date date)
  {
    return new SimpleDateFormat(Constants.TIME_FORMAT).format(date);
  }

  /**
   * Validates and formats the given string into a time format for the
   * current database.
   * @param statement
   * @return
   */
  @Override
  protected StatementPrimitive formatAndValidate(String statement)
  {
    // Throw an exception if this value is invalid
    if (!com.runwaysdk.dataaccess.attributes.entity.AttributeTime.isValid(statement))
    {
      String error = "Value [" + statement + "] is not valid for query attribute [" + this._getAttributeName() + "]";

      throw new AttributeValueException(error, statement);
    }

    String formattedValue = Database.formatJavaToSQLForQuery(statement, MdAttributeTimeInfo.CLASS, false);
    return new StatementPrimitive(formattedValue);
  }

}
