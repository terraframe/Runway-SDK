/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.query;

public abstract class BasicLeftJoin extends LeftJoin
{

  public BasicLeftJoin(EntityQuery entityQuery, Selectable... selectableArray2)
  {
    super(entityQuery, selectableArray2);

  }

  public BasicLeftJoin(EntityQuery entityQuery, Selectable selectable2)
  {
    super(entityQuery, selectable2);

  }

  public BasicLeftJoin(Selectable selectable1, EntityQuery entityQuery)
  {
    super(selectable1, entityQuery);

  }

  public BasicLeftJoin(Selectable selectable1, Selectable... selectableArray2)
  {
    super(selectable1, selectableArray2);

  }

  public BasicLeftJoin(Selectable selectable1, Selectable selectable2)
  {
    super(selectable1, selectable2);

  }

  public BasicLeftJoin(String columnName1, String tableName1, String tableAlias1, String columnName2, String tableName2, String tableAlias2)
  {
    super(columnName1, tableName1, tableAlias1, columnName2, tableName2, tableAlias2);
  }

  /**
   * Returns a SQL String that represents the join.
   * 
   * @return SQL String that represents the join.
   */
  public String getSQL()
  {
    if (this.selectable1 != null)
    {
      String leftJoinString = "";
      
      Selectable[] selectableArray2 = this.getSelectableArray2();

      for (Selectable loopSelectable : selectableArray2)
      {
        ComponentQuery componentQuery = loopSelectable.getRootQuery();

        if (componentQuery instanceof ValueQuery)
        {
          ValueQuery valueObjectQuery = (ValueQuery) componentQuery;

          leftJoinString += " LEFT JOIN\n(" + valueObjectQuery.getSQL() + ") " + valueObjectQuery.getTableAlias() + " ON " + this.selectable1.getSQL() + " " + this.getOperator() + " " + loopSelectable.getSQL() + "\n";

          continue;
        }

        leftJoinString += "\n LEFT JOIN " + loopSelectable.getDefiningTableName() + " " + loopSelectable.getDefiningTableAlias() + " ON " + this.selectable1.getSQL() + " " + this.getOperator() + " " + loopSelectable.getSQL() + "\n";

        continue;
      }

      return leftJoinString;
    }
    else
    {
      LeftJoin nestedLeftJoin = this.getNestedLeftJoin();
      
      String leftJoinString = " LEFT JOIN " + this.tableName2 + " " + this.tableAlias2 + " ON " + this.expression1 + " " + this.getOperator() + " " + this.expression2 + "\n";

      if (nestedLeftJoin != null)
      {
        leftJoinString += nestedLeftJoin.getSQL();
      }

      return leftJoinString;
    }
  }

  /**
   * Returns the comparison operator used for this join.
   * 
   * @return
   */
  protected abstract String getOperator();

}
