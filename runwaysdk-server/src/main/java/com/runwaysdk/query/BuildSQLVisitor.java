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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BuildSQLVisitor extends Visitor
{
  /**
   * Key: TableAlias and Value: TableName
   */
  protected Map<String, String> tableFromMap;

  protected Set<Join>           tableJoinSet;

  protected Set<Selectable>     leftAttributeSubSelectSet;

  private boolean               containsSelectableSQL;

  public BuildSQLVisitor(ComponentQuery componentQuery)
  {
    super(componentQuery);

    this.tableFromMap = new HashMap<String, String>();
    this.tableJoinSet = new HashSet<Join>();

    this.leftAttributeSubSelectSet = new HashSet<Selectable>();
    this.containsSelectableSQL = false;
  }

  /**
   * Visits the given expression and gathers all data required to
   * build a SQL statement.
   *
   * @param component
   */
  public void visit(Component component)
  {
    if (component instanceof SelectableSQL)
    {
      this.containsSelectableSQL = true;
    }

    if (this.hasVisitedComponent(component))
    {
      return;
    }

    Map<String, String> expressionFromTableMap = component.getFromTableMap();
    if (expressionFromTableMap != null)
    {
      this.tableFromMap.putAll(expressionFromTableMap);

    }

    Set<Join> expressionTableJoinSet = component.getJoinStatements();
    if (expressionTableJoinSet != null)
    {
      this.tableJoinSet.addAll(expressionTableJoinSet);

      for (Join tableJoin : expressionTableJoinSet)
      {
        this.tableFromMap.put(tableJoin.getTableAlias1(), tableJoin.getTableName1());
        this.tableFromMap.put(tableJoin.getTableAlias2(), tableJoin.getTableName2());
      }
    }

    if (component instanceof SubSelectCondition)
    {
      SubSelectCondition subSelectCondition = (SubSelectCondition) component;
      this.leftAttributeSubSelectSet.add(subSelectCondition.selectableLeft);
    }

    this.addVisitedComponent(component);
  }

  public boolean containsSelectableSQL()
  {
    return containsSelectableSQL;
  }

  public void setContainsSelectableSQL(boolean containsSelectableSQL)
  {
    this.containsSelectableSQL = containsSelectableSQL;
  }
}
