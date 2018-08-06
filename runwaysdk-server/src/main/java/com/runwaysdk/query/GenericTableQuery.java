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

import com.runwaysdk.dataaccess.MdTableDAOIF;

public class GenericTableQuery extends GeneratedTableClassQuery
{
  private MdTableDAOIF mdTableDAOIF;
  
  public GenericTableQuery(MdTableDAOIF _mdTableDAOIF, QueryFactory _componentQueryFactory)
  {
    super();
    
    this.mdTableDAOIF = _mdTableDAOIF;
    
    if (this.getComponentQuery() == null)
    {
      this.setComponentQuery(_componentQueryFactory.tableQuery(_mdTableDAOIF.definesType()));
    }
  }
  
  public GenericTableQuery(MdTableDAOIF _mdTableDAOIF, ValueQuery _valueQuery)
  {
    super();
    
    this.mdTableDAOIF = _mdTableDAOIF;
    
    if (this.getComponentQuery() == null)
    {
      this.setComponentQuery(new TableQuery(_valueQuery, _mdTableDAOIF.definesType()));
    }
  }
  
  @Override
  public MdTableDAOIF getMdClassIF()
  {
    return (MdTableDAOIF)super.getMdClassIF();
  }
  
  public MdTableDAOIF getMdTableF()
  {
    return (MdTableDAOIF)super.getMdClassIF();
  }

  @Override
  public String getClassType()
  {
    return mdTableDAOIF.definesType();
  }

  /**
   * Returns {@link TableQuery} that all generated query methods delegate to.
   * 
   * @return {@link TableQuery} that all generated query methods delegate to.
   */
  @Override
  protected TableQuery getComponentQuery()
  {
    return (TableQuery)super.getComponentQuery();
  }
  
  /**
   * Sets the {@link TableQuery} that all generated query methods delegate to.
   */
  protected void setComponentQuery(TableQuery _tableQuery)
  {
    super.setComponentQuery(_tableQuery);
  }

//  @Override
//  protected SelectableChar getOid()
//  {
//    // TODO Auto-generated method stub
//    return null;
//  }

}
