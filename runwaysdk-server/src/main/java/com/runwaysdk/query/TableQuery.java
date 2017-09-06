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

import com.runwaysdk.business.Business;
import com.runwaysdk.dataaccess.MdTableDAOIF;

public class TableQuery extends TableClassQuery
{  
  /**
   * 
   * @param queryFactory
   * @param type
   */
  protected TableQuery(QueryFactory queryFactory, String type)
  {
    super(queryFactory, type);
  }
  
  /**
   * 
   * @param valueQuery
   * @param type
   */
  protected TableQuery(ValueQuery valueQuery, String type)
  {
    super(valueQuery, type);
  }
  
  /**
   * Returns the {@link MdTableDAOIF} that defines the type of objects that are queried from
   * this object.
   * 
   * @return {@link MdTableDAOIF} that defines the type of objects that are queried from
   *         this object.
   */
  public MdTableDAOIF getMdTableDAOIF()
  {
    return (MdTableDAOIF)this.getMdTableClassIF();
  }
  
  /**
   * Returns the type that this object queries.
   * 
   * @return type that this object queries.
   */
  public String getType()
  {
    return this.type;
  }

  /**
   * Returns an iterator of Business objects that match the query criteria specified
   * on this query object.
   * @return iterator of Business objects that match the query criteria specified
   * on this query object.
   */
  public OIterator<Business> getIterator()
  {
    throw new UnsupportedOperationException();
  }
 
  /**
   * Returns an iterator of Business objects that match the query criteria specified
   * on this query object.
   * @param pageSize   number of results per page
   * @param pageNumber page number
   * @return iterator of Business objects that match the query criteria specified
   * on this query object.
   */
  public OIterator<Business> getIterator(int pageSize, int pageNumber)
  {
    throw new UnsupportedOperationException();
  }  


  /**
   * @return returns a rank function 
   */
  public RANK RANK()
  {
    SelectableSpoof selectableSpoof = new SelectableSpoof(false, this, "RankSpoof"); 
    return new RANK(selectableSpoof);
  }
  
  /**
   * @return returns a rank function 
   */
  public RANK RANK(String userDefinedAlias)
  {
    SelectableSpoof selectableSpoof = new SelectableSpoof(false, this, "RankSpoof"); 
    return new RANK(selectableSpoof, userDefinedAlias);
  }
  
  /**
   *
   * @param selectable
   */
  public RANK RANK(Selectable selectable)
  {
    return new RANK(selectable, null, null);
  }
 
  /**
   *
   * @param selectable
   * @param userDefinedAlias
   */
  public RANK RANK(Selectable selectable, String userDefinedAlias)
  {
    return new RANK(selectable, userDefinedAlias, null);
  }
 
  /**
   *
   * @param selectable
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   */
  public RANK RANK(Selectable selectable, String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return new RANK(selectable, userDefinedAlias, userDefinedDisplayLabel);
  }

}
