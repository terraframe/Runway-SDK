/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
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

import java.util.LinkedList;
import java.util.List;

import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;

public class GeoEntityProblem extends GeoEntityProblemBase
{
  private static final long serialVersionUID = 1674989479;

  public GeoEntityProblem()
  {
    super();
  }

  public List<GeoEntityProblemView> getViews()
  {
    GeoEntity entity = this.getGeoEntity();

    String label = entity.getDisplayLabel().getValue() + " (" + entity.getUniversal().getDisplayLabel().getValue() + ") : " + entity.getGeoId();

    List<GeoEntityProblemType> problemTypes = this.getProblemType();

    List<GeoEntityProblemView> list = new LinkedList<GeoEntityProblemView>();

    for (GeoEntityProblemType type : problemTypes)
    {
      String problemText = type.getDescription().replace("{0}", label);

      GeoEntityProblemView view = new GeoEntityProblemView();
      view.setConcreteId(this.getOid());
      view.setGeoId(this.getGeoEntityId());
      view.setProblem(problemText);
      view.setProblemName(type.getEnumName());

      list.add(view);
    }

    return list;
  }

  public static void createProblems(GeoEntity entity, GeoEntityProblemType... types)
  {
    for (GeoEntityProblemType type : types)
    {
      GeoEntityProblem problem = new GeoEntityProblem();
      problem.setGeoEntity(entity);
      problem.addProblemType(type);
      problem.apply();
    }
  }

  public static void deleteProblems(GeoEntity entity, GeoEntityProblemType... types)
  {
    GeoEntityProblemQuery query = new GeoEntityProblemQuery(new QueryFactory());
    query.WHERE(query.getGeoEntity().EQ(entity));

    if (types != null && types.length > 0)
    {
      query.AND(query.getProblemType().containsAny(types));
    }

    OIterator<? extends GeoEntityProblem> iterator = query.getIterator();

    try
    {
      while (iterator.hasNext())
      {
        GeoEntityProblem problem = iterator.next();
        problem.delete();
      }
    }
    finally
    {
      iterator.close();
    }
  }

  public static boolean hasEntry(GeoEntity geoEntity, GeoEntityProblemType type)
  {
    GeoEntityProblemQuery query = new GeoEntityProblemQuery(new QueryFactory());
    query.WHERE(query.getGeoEntity().EQ(geoEntity));
    query.AND(query.getProblemType().containsExactly(type));

    return ( query.getCount() > 0 );
  }

}
