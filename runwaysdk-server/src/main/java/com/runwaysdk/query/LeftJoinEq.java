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

public class LeftJoinEq extends LeftJoin
{
  /**
   * Represents a left outer join between tables in the query.
   * @param columnName1   attribute name in the left side of the join
   * @param tableName1   name of the table that defines attribute1
   * @param tableAlias1  alias of the table that defines attribute1
   * @param columnName2   attribute name in the right side of the join
   * @param tableName2   name of the table that defines attribute2
   * @param tableAlias2  alias of the table that defines attribute2
   */
  public LeftJoinEq(String columnName1, String tableName1, String tableAlias1, String columnName2, String tableName2, String tableAlias2)
  {
    super(columnName1, tableName1, tableAlias1, columnName2, tableName2, tableAlias2);
  }

  /**
   * Represents a join between tables in the query.
   *
   * @param selectable1
   * @param selectableArray2
   */
  public LeftJoinEq(Selectable selectable1, Selectable... selectableArray2)
  {
    super(selectable1, selectableArray2);
  }

  /**
   * Represents a join between tables in the query.
   *
   * @param selectable1
   * @param selectable2
   */
  public LeftJoinEq(Selectable selectable1, Selectable selectable2)
  {
    super(selectable1, selectable2);
  }

  @Override
  protected String getOperator()
  {
    return "=";
  }
}
