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

import java.util.List;

import com.runwaysdk.dataaccess.MdClassDAOIF;


public abstract class GeneratedComponentQuery
{
  protected Integer _pageSize = null;
  protected Integer _pageNumber = null;

  protected Integer _limit = null;
  protected Integer _skip = null;

  /**
   *
   */
  protected GeneratedComponentQuery()
  {
    super();
  }

  /**
   * @return The metadata for the type that this query returns
   */
  public abstract MdClassDAOIF getMdClassIF();

  /**
   * @param attributeName
   * @return The query attribute correspond
   */
  public abstract Attribute get(String attributeName);
  
  public abstract Attribute get(String attributeName, String userDefinedAlias);
  
  /**
   * Retricts the query to return rows from the given page number
   * where each page has the given number of rows.
   * @param pageSize
   * @param pageNumber
   */
  public void restrictRows(int pageSize, int pageNumber)
  {
    _pageSize = pageSize;
    _pageNumber = pageNumber;

    _limit = ComponentQuery.rowLimit(pageSize);
    _skip = ComponentQuery.rowSkip(pageSize, pageNumber);
  }

  /**
   * Removes any specified row restriction.  Query will return all
   * rows that match the query criteria.
   */
  public void unrestrictRows()
  {
    _pageSize = null;
    _pageNumber = null;

    _limit = null;
    _skip = null;
  }

  /**
   * Returns the type of components that are queried by this object.
   * @return type of components that are queried by this object.
   */
  public abstract String getClassType();

  /**
   * Returns the query factory used by this Query object.
   * @return query factory used by this Query object.
   */
  protected QueryFactory getQueryFactory()
  {
    return this.getComponentQuery().getQueryFactory();
  }

  /**
   * Returns ComponentQuery that all generated query methods delegate to.
   * @return ComponentQuery that all generated query methods delegate to.
   */
  protected abstract ComponentQuery getComponentQuery();

  /**
   * Returns an attribute character statement object.
   * @param name name of the attribute.
   * @return Attribute character statement object.
   */
  protected abstract SelectableChar getId();

  /**
   * Adds a condition to this query.  Will perform an AND
   * with any prior condition previously added.
   * @param condition condition to add.
   */
  public void WHERE(Condition condition)
  {
    this.getComponentQuery().WHERE(condition);
  }

  /**
   * Adds a condition to this query.  Will perform an AND
   * with any prior condition previously added.
   * @param condition condition to add.
   */
  public void AND(Condition condition)
  {
    this.getComponentQuery().AND(condition);
  }

  /**
   * Adds a condition to this query.  Will perform an AND
   * with any prior condition previously added.
   * @param condition condition to add.
   */
  public void OR(Condition condition)
  {
    this.getComponentQuery().OR(condition);
  }

  /**
   * Sort in ascending order.
   * @param selectablePrimitive
   */
  public void ORDER_BY_ASC(SelectablePrimitive selectablePrimitive)
  {
    this.getComponentQuery().ORDER_BY_ASC(selectablePrimitive);
  }

  /**
   * Sort in ascending order.
   * @param selectablePrimitive
   */
  public void ORDER_BY_ASC(Selectable selectable, String sortAlias)
  {
    this.getComponentQuery().ORDER_BY_ASC(selectable, sortAlias);
  }
  
  /**
   * Sort in descending order.
   * @param selectablePrimitive
   */
  public void ORDER_BY_DESC(Selectable selectable)
  {
    this.getComponentQuery().ORDER_BY_DESC(selectable);
  }

  /**
   * Sort in descending order.
   * @param selectable
   */
  public void ORDER_BY_DESC(Selectable selectable, String sortAlias)
  {
    this.getComponentQuery().ORDER_BY_DESC(selectable, sortAlias);
  }
  

  /**
   * Adds an order by clause to this query.
   * @param selectable SelectablePrimitive query object.
   */
  public void ORDER_BY(Selectable selectable, OrderBy.SortOrder order)
  {
    this.getComponentQuery().ORDER_BY(selectable, order);
  }

  /**
   * Adds an order by clause to this query.
   * @param selectable SelectablePrimitive query object.
   */
  public void ORDER_BY(Selectable selectable, OrderBy.SortOrder order, String sortAlias)
  {
    this.getComponentQuery().ORDER_BY(selectable, order, sortAlias);
  }
  
  /**
   * Returns the count of the objects that match the specified criteria.
   * @return count of the objects that match the specified criteria.
   */
  public long getCount()
  {
    return this.getComponentQuery().getCount();
  }

  public int getPageSize()
  {
    if(_pageSize == null)
    {
      return 0;
    }

    return _pageSize;
  }

  public int getPageNumber()
  {
    if(_pageNumber == null)
    {
      return 0;
    }

    return _pageNumber;
  }

  public List<OrderBy> getOrderByList()
  {
    return this.getComponentQuery().orderByList;
  }

  /**
   * Returns the SQL representation of this query, including all attributes of the type in the select clause.
   * @return SQL representation of this query, including all attributes of the type in the select clause.
   */
  public String getSQL()
  {
    return this.getComponentQuery().getSQL();
  }
  
  /**
   * Returns the SQL representation of this query, for counting rows only
   * @return SQL representation of this query for counting
   */
  public String getCountSQL()
  {
    return this.getComponentQuery().getCountSQL();
  }
  
  /**
   * @return returns a rank function 
   */
  public RANK RANK()
  {
    return this.getComponentQuery().RANK();
  }
  
  /**
   * @return returns a rank function 
   */
  public RANK RANK(String userDefinedAlias)
  {
    return this.getComponentQuery().RANK(userDefinedAlias);
  }
}
