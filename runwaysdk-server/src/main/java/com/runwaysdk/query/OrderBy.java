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



public class OrderBy
{
  private Selectable selectable;
  
  private SortOrder sortOrder;
  
  private String sortAlias;

  public enum SortOrder
  {
    /**
     * Ascending order.
     */
    ASC("ASC"),

    /**
     * Descending order.
     */
    DESC("DESC");

    private String sortDirection;

    private SortOrder(String sortDirection)
    {
      this.sortDirection = sortDirection;
    }

    public String getSQL()
    {
      return this.sortDirection;
    }

    /**
     * Given a type-unsafe string that represents a sort direction,
     * this method returns the type-safe SortOrder enum item that
     * matches that sort direction. If no match is found, a QueryException
     * is thrown. Note that case is ignored in the comparison.
     *
     * @param order
     * @return
     */
    public static SortOrder getSortOrder(String order)
    {
      if(order.equalsIgnoreCase(ASC.sortDirection))
      {
        return ASC;
      }
      else if(order.equalsIgnoreCase(DESC.sortDirection))
      {
        return DESC;
      }
      else
      {
        String error = "The sort direction ["+order+"] is not supported.";
        throw new QueryException(error);
      }
    }
  }

  public OrderBy(Selectable selectable, SortOrder order)
  {
    this(selectable, order, selectable.getMdAttributeIF().definesAttribute());
  }

  public OrderBy(Selectable selectable, SortOrder order, String sortAlias)
  {
    super();
    this.selectable = selectable;
    this.sortOrder = order;
    this.sortAlias = sortAlias;
  }
  
  /**
   *
   * @return
   */
  public String getSQL()
  {
    if (this.selectable instanceof Function)
    {
      return this.selectable.getSQL()+" "+this.sortOrder.getSQL();
    }
    else
    {
      return this.selectable.getColumnAlias()+" "+this.sortOrder.getSQL();
    }  
  }

  /**
   *
   * @return
   */
  public String getSelectClauseString()
  {
    return this.selectable.getSQL() + " "+Database.formatColumnAlias(this.selectable.getColumnAlias());
  }

  /**
   * Returns the alias used in the query for this column.
   * @return alias used in the query for this column.
   */
  public String getColumnAlias()
  {
    return this.selectable.getColumnAlias();
  }

  /**
   * Returns the attribute query object used in this order by statement.
   * @return attribute query object used in this order by statement.
   */
  public Selectable getSelectable()
  {
    return selectable;
  }

  public SortOrder getSortOrder()
  {
    return sortOrder;
  }
  
  public String getSortAlias()
  {
    return this.sortAlias;
  }
}
