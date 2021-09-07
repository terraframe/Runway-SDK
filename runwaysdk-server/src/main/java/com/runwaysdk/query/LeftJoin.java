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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;


public abstract class LeftJoin extends Join
{
  /**
   * <code>ValueQuery</code> that has this left join.
   */
  private ValueQuery rootValueQuery;

  /**
   * Nested left joins that all join to a single table alias. This assumes that all of the
   * left joins join to the same table on the left.
   */
  private LeftJoin nestedLeftJoin;

  /**
   * Array of selectables that participate in the left hand side of the join.
   */
  private Selectable[] selectableArray2;

  /**
   * Represents a left outer join between tables in the query.
   * @param columnName1   attribute name in the left side of the join
   * @param tableName1   name of the table that defines attribute1
   * @param tableAlias1  alias of the table that defines attribute1
   * @param columnName2   attribute name in the right side of the join
   * @param tableName2   name of the table that defines attribute2
   * @param tableAlias2  alias of the table that defines attribute2
   */
  protected LeftJoin(String columnName1, String tableName1, String tableAlias1, String columnName2, String tableName2, String tableAlias2)
  {
    super(columnName1, tableName1, tableAlias1, columnName2, tableName2, tableAlias2);
    
    this.nestedLeftJoin = null;
  }

  /**
   * Represents a join between tables in the query.
   * @param selectable1
   * @param selectableArray2
   */
  protected LeftJoin(Selectable selectable1, Selectable... selectableArray2)
  {
    super(selectable1, selectableArray2[0]);

    this.selectableArray2 = selectableArray2;
    
    this.nestedLeftJoin = null;
  }

  protected LeftJoin(Selectable selectable1, Selectable selectable2)
  {
    super(selectable1, selectable2);

    this.selectableArray2 = new Selectable[1];

    this.selectableArray2[0] = selectable2;
    
    this.nestedLeftJoin = null;
  }
  /**
   * Represents a join between tables in the query.
   *
   * @param selectable1
   * @param entityQuery
   */
  protected LeftJoin(Selectable selectable1, EntityQuery entityQuery)
  {
    super(selectable1, entityQuery.oid());
    
    this.nestedLeftJoin = null;
  }

  /**
   * Represents a join between tables in the query.
   *
   * @param entityQuery
   * @param selectableArray2
   */
  protected LeftJoin(EntityQuery entityQuery, Selectable... selectableArray2)
  {
    super(entityQuery.oid(), selectableArray2[0]);
    
    this.nestedLeftJoin = null;
  }

  /**
   * Represents a join between tables in the query.
   *
   * @param entityQuery
   * @param selectable1
   */
  protected LeftJoin(EntityQuery entityQuery, Selectable selectable2)
  {
    super(entityQuery.oid(), selectable2);

    this.selectableArray2 = new Selectable[1];

    this.selectableArray2[0] = selectable2;
    
    this.nestedLeftJoin = null;
  }

  protected void setValueQuery(ValueQuery valueQuery)
  {
    this.rootValueQuery = valueQuery;

    if (this.selectable2 != null)
    {
      for (Selectable loopSelectable : selectableArray2)
      {
        ComponentQuery componentQuery = loopSelectable.getRootQuery();

        if (componentQuery instanceof EntityQuery)
        {
          this.rootValueQuery.setEntityQueryInLeftJoin((EntityQuery)componentQuery);
        }
      }
    }
  }

  protected List<ValueQuery> getValueObjectQueries()
  {
    List<ValueQuery> returnList = new LinkedList<ValueQuery>();

    if (this.selectable1 != null)
    {
      ComponentQuery componentQuery = this.selectable1.getRootQuery();

      if (componentQuery instanceof ValueQuery)
      {
        ValueQuery valueObjectQuery =  (ValueQuery)componentQuery;
        returnList.add(valueObjectQuery);
      }
    }

    if (this.selectable2 != null)
    {
      for (Selectable loopSelectable : selectableArray2)
      {
        ComponentQuery componentQuery = loopSelectable.getRootQuery();

        if (componentQuery instanceof ValueQuery)
        {
          ValueQuery valueObjectQuery =  (ValueQuery)componentQuery;
          returnList.add(valueObjectQuery);
        }
      }
    }

    return returnList;
  }

  protected Set<String> getLeftSideEntityQueryAlias()
  {
    Set<String> returnSet = new HashSet<String>();

    if (this.selectable2 != null)
    {
      for (Selectable loopSelectable : selectableArray2)
      {
        ComponentQuery componentQuery = loopSelectable.getRootQuery();

        if (componentQuery instanceof EntityQuery)
        {
          EntityQuery entityQuery =  (EntityQuery)componentQuery;
          returnSet.add(entityQuery.getAliasSeed());
        }
      }
    }

    return returnSet;
  }

  protected String leftSideSQL()
  {

    if (this.selectable1 != null)
    {
      ComponentQuery componentQuery = this.selectable1.getRootQuery();

      if (componentQuery instanceof ValueQuery)
      {
        ValueQuery valueObjectQuery =  (ValueQuery)componentQuery;
        return "("+valueObjectQuery.getSQL()+") "+valueObjectQuery.getTableAlias();
      }
    }

    return this.getTableName1()+" "+this.getTableAlias1();

  }

  /**
   * Nested left joins that all join to a single table alias. This assumes that all of the
   * left joins join to the same table on the left.
   */
  public LeftJoin nest(LeftJoin leftJoin)
  {
    this.nestedLeftJoin = leftJoin;

    return this;
  }
  
  /**
   * Returns a SQL String that represents the join.
   * @return SQL String that represents the join.
   */
  public String getSQL()
  {
    if (this.selectable1 != null)
    {
      String leftJoinString = "";

      for (Selectable loopSelectable : selectableArray2)
      {
        ComponentQuery componentQuery = loopSelectable.getRootQuery();

        if (componentQuery instanceof ValueQuery)
        {
          ValueQuery valueObjectQuery =  (ValueQuery)componentQuery;

          leftJoinString += " LEFT JOIN\n("+valueObjectQuery.getSQL()+") "+valueObjectQuery.getTableAlias()+" ON "+
          this.selectable1.getSQL()+" "+this.getOperator()+" "+loopSelectable.getSQL()+"\n";

          continue;
        }

        else if (componentQuery instanceof EntityQuery)
        {
          EntityQuery entityQuery =  (EntityQuery)componentQuery;

          List<Selectable> selectableList = this.rootValueQuery.getRawSelectebleFromComponentQuery(entityQuery.getAliasSeed());

          if (selectableList != null)
          {
            List<Selectable> newSelectableList = new ArrayList<Selectable>(selectableList);

            // Add whatever attribute we are performing the join on
            newSelectableList.add(loopSelectable);

            MdAttributeConcreteDAOIF selectable2MdAttributeIf = loopSelectable.getMdAttributeIF();
            // We know this is an entity because this came from an entity query
            MdEntityDAOIF selectable2MdEntityIF = (MdEntityDAOIF)selectable2MdAttributeIf.definedByClass();
            String definingTableName = entityQuery.getMdEntityIF().getTableName();
            String definingTableAlias = entityQuery.getTableAlias("", definingTableName);

            // This will produce SQL that will reference the aliased attribute and table name in nested SELECT clause in the inner join.
            Selectable tempSelectable2 =
              ValueQuery.attributeFactory(entityQuery, loopSelectable, selectable2MdEntityIF, definingTableName, definingTableAlias,
                  selectable2MdAttributeIf, loopSelectable.getUserDefinedAlias(), loopSelectable.getUserDefinedDisplayLabel());

            leftJoinString += "\n LEFT JOIN\n("+entityQuery.getSQL(newSelectableList)+") "+entityQuery.getTableAlias("", entityQuery.getMdEntityIF().getTableName())+" ON "+
            this.selectable1.getSQL()+" "+this.getOperator()+" "+tempSelectable2.getSQL()+"\n";

            continue;
          }
        }

        leftJoinString += "\n LEFT JOIN "+loopSelectable.getDefiningTableName()+" "+loopSelectable.getDefiningTableAlias()+" ON "+
        this.selectable1.getSQL()+" "+this.getOperator()+" "+loopSelectable.getSQL()+"\n";

        continue;
      }

      return leftJoinString;
    }
    else
    {
      String leftJoinString = " LEFT JOIN "+this.tableName2+" "+this.tableAlias2+" ON "+
      this.expression1+" "+this.getOperator()+" "+this.expression2+"\n";
      
      if (nestedLeftJoin != null)
      {
        leftJoinString += nestedLeftJoin.getSQL();
      }
      
      return leftJoinString;
    }
  }

  /**
   * Returns the comparison operator used for this join.
   * @return
   */
  protected abstract String getOperator();
  
  protected ValueQuery getRootValueQuery()
  {
    return rootValueQuery;
  }
  
  protected Selectable[] getSelectableArray2()
  {
    return selectableArray2;
  }
  
  protected LeftJoin getNestedLeftJoin()
  {
    return nestedLeftJoin;
  }
    
}
