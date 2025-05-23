/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK GIS(tm).
 *
 * Runway SDK GIS(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway SDK GIS(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK GIS(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.system.gis.geo;


/**
 *
 * @author Autogenerated by RunwaySDK
 */
public class UniversalViewQuery extends com.runwaysdk.system.gis.geo.UniversalViewQueryBase 
{

  /**
   * 
   */
  private UniversalQuery universalQuery;
  
  /**
   * The Universal ids to restrict the query.
   */
  private String[] ids;
  
  /**
   * The Id on which to restrict
   * 
   * @param queryFactory
   */
  
  public UniversalViewQuery(com.runwaysdk.query.QueryFactory queryFactory)
  {
    super(queryFactory);
  }

  public UniversalViewQuery(com.runwaysdk.query.QueryFactory queryFactory, String ... ids)
  {
    super(queryFactory);
    
    this.universalQuery = new UniversalQuery(queryFactory);
    this.ids = ids;
    
    this.buildQuery(new DefaultUniversalViewBuilder(queryFactory));
  }

  public UniversalViewQuery(com.runwaysdk.query.QueryFactory queryFactory, com.runwaysdk.query.ViewQueryBuilder viewQueryBuilder)
  {
    super(queryFactory, viewQueryBuilder);
  }

  class DefaultUniversalViewBuilder extends com.runwaysdk.query.ViewQueryBuilder
  {
    public DefaultUniversalViewBuilder(com.runwaysdk.query.QueryFactory queryFactory)
    {
      super(queryFactory);
    }

    protected UniversalViewQuery getViewQuery()
    {
      return (UniversalViewQuery)super.getViewQuery();
    }

    /**
     * build the select clause
     */
    protected void buildSelectClause()
    {
      UniversalViewQuery q = this.getViewQuery();
      
      q.map(UniversalView.DISPLAYLABEL, universalQuery.getDisplayLabel().localize());
      q.map(UniversalView.DESCRIPTION, universalQuery.getDescription().localize());
      q.map(UniversalView.UNIVERSAL, universalQuery.getOid());
      
      // Because a Universal can have more than one allowed in parent it does
      // not make sense to return just one match.
//      q.map(UniversalView.PARENTUNIVERSAL, universalQuery.getOid());
    }

    /**
     * Implement only if additional join criteria is required.
     */
    protected void buildWhereClause()
    {
      UniversalViewQuery q = this.getViewQuery();
      
      if(ids.length > 0)
      {
        q.WHERE(universalQuery.getOid().IN(ids));
      }
    }

  }
}