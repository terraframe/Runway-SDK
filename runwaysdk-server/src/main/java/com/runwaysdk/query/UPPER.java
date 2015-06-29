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

import com.runwaysdk.dataaccess.database.Database;

public class UPPER extends StringFunction
{
  /**
   * Uppercase selectable.  Converts the values of the character column to uppercase.
   *
   * @param selectable function result to trim.
   */
  protected UPPER(Selectable selectable)
  {
    this(selectable, null, null);
  }

  /**
   * Uppercase selectable.  Converts the values of the character column to uppercase.
   *
   * @param selectable function result to trim.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  protected UPPER(Selectable selectable, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    super(selectable, userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   *
   */
  public String getSQL()
  {
    return Database.buildUpperCaseFunctionCall(this.selectable.getSQL());
  }

  @Override
  protected String getFunctionName()
  {
    return "UPPER";
  }

  public UPPER clone() throws CloneNotSupportedException
  {
    return (UPPER)super.clone();
  }
}
