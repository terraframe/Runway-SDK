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
import java.util.Map;

import com.runwaysdk.dataaccess.MdViewDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.metadata.MdViewDAO;

public abstract class GeneratedViewQuery extends GeneratedComponentQuery
{
  protected ValueQuery            valueQuery = null;

  protected MdViewDAOIF           mdViewIF   = null;

  private Map<String, Selectable> selectableMap;

  protected GeneratedViewQuery(QueryFactory componentQueryFactory)
  {
    super();
    this.selectableMap = new HashMap<String, Selectable>();
    ValueQuery valueQuery = componentQueryFactory.valueQuery();

    this.mdViewIF = MdViewDAO.getMdViewDAO(this.getClassType());

    this.setComponentQuery(valueQuery);
  }

  protected GeneratedViewQuery(QueryFactory componentQueryFactory, ViewQueryBuilder viewQueryBuilder)
  {
    this(componentQueryFactory);

    this.buildQuery(viewQueryBuilder);
  }

  public abstract String getClassType();

  /**
   *
   */
  protected GeneratedViewQuery()
  {
    super();
    this.selectableMap = new HashMap<String, Selectable>();
  }

  /**
   * Override this method to specify the attributes that will be included in the select clause.
   */
  protected void buildSelectClause()
  {
    this.valueQuery.SELECT(this.constructSelectClauseFromMap());
  }

  /**
   * Returns the MdViewIF of components that are queried by this object.
   * 
   * @return MdViewIF of components that are queried by this object.
   */
  public MdViewDAOIF getMdClassIF()
  {
    return this.mdViewIF;
  }

  @Override
  public ValueQuery getComponentQuery()
  {
    return valueQuery;
  }

  /**
   * Adds a left outer join to the query.
   * 
   * @param leftOuterJoint
   */
  public void WHERE(LeftJoin leftOuterJoin)
  {
    this.valueQuery.WHERE(leftOuterJoin);
  }

  /**
   * Adds a left outer join to the query.
   * 
   * @param leftOuterJoint
   */
  public void AND(LeftJoin leftOuterJoin)
  {
    this.valueQuery.AND(leftOuterJoin);
  }

  /**
   * Sets the ValueQuery that all generated query methods delegate to.
   */
  protected void setComponentQuery(ValueQuery valueQuery)
  {
    this.valueQuery = valueQuery;
  }

  /**
   * Maps the given selectable to the given alias.
   */
  public void map(String alias, Selectable selectabe)
  {
    selectabe.setUserDefinedAlias(alias);
    this.selectableMap.put(alias, selectabe);
  }

  /**
   * Builds the select clause and query criteria for this view.
   * 
   * @param viewQueryBuilder
   */
  public void buildQuery(ViewQueryBuilder viewQueryBuilder)
  {
    viewQueryBuilder.setGeneratedViewQuery(this);

    this.valueQuery.resetConditions();

    viewQueryBuilder.buildSelectClause();

    this.buildSelectClause();

    viewQueryBuilder.buildWhereClause();
  }

  /**
   * Returns the map of selectables that have been defined for the select clause of this view.
   * 
   * @return map of selectables that have been defined for the select clause of this view.
   */
  protected Map<String, Selectable> getSelectableMap()
  {
    return selectableMap;
  }

  /**
   * Returns the map of selectables that have been defined for the select clause of this view.
   *
   * @param userDefinedAlias
   *          new name for the selectable
   * @param userDefinedDisplayLabel
   *          new display label for the selectable
   *
   * @return map of selectables that have been defined for the select clause of this view.
   */
  protected Selectable getSelectable(String attributeName, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    try
    {
      Selectable selectable = selectableMap.get(attributeName).clone();

      if (userDefinedAlias != null)
      {
        selectable.setUserDefinedAlias(userDefinedAlias);
      }
      return selectable;
    }
    catch (Exception e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  /**
   * Builds the select clause from the selectable attribute map.
   */
  protected Selectable[] constructSelectClauseFromMap()
  {
    int numOfSelectable = this.selectableMap.size();
    Selectable[] selectableArray = new Selectable[numOfSelectable];

    int loopCount = 0;
    for (Selectable selectable : this.selectableMap.values())
    {
      selectableArray[loopCount] = selectable;
      loopCount++;
    }
    return selectableArray;
  }

  public Attribute get(String attributeName)
  {
    return this.getComponentQuery().get(attributeName);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.query.GeneratedComponentQuery#get(java.lang.String, java.lang.String)
   */
  @Override
  public Attribute get(String attributeName, String userDefinedAlias)
  {
    return this.getComponentQuery().get(attributeName, userDefinedAlias);
  }
}
