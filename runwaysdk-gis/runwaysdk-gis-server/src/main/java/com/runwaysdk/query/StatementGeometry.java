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
package com.runwaysdk.query;

import java.util.Map;
import java.util.Set;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.WKTWriter;

public class StatementGeometry extends StatementPrimitive
{
  private Geometry  geometry;

  private int       srid;

  private WKTWriter writer;

  public StatementGeometry(Geometry geometry)
  {
    this(geometry, geometry.getSRID());
  }

  public StatementGeometry(Geometry geometry, int srid)
  {
    super("");

    this.geometry = geometry;
    this.srid = srid;
    this.writer = new WKTWriter();
  }

  public String getSQL()
  {
    return "ST_GeomFromText('SRID=" + this.srid + ";" + this.writer.write(this.geometry) + "')";
  }

  /**
   * Returns a Set of TableJoin objects that represent joins statements that are
   * required for this expression.
   * 
   * @return Set of TableJoin objects that represent joins statements that are
   *         required for this expression, or null of there are none.
   */
  public Set<Join> getJoinStatements()
  {
    return null;
  }

  /**
   * Returns a Map representing tables that should be included in the from
   * clause, where the key is the table alias and the value is the name of the
   * table.
   * 
   * @return Map representing tables that should be included in the from clause,
   *         where the key is the table alias and the value is the name of the
   *         table.
   */
  public Map<String, String> getFromTableMap()
  {
    return null;
  }

  /**
   * Visitor to traverse the query object structure.
   * 
   * @param visitor
   */
  public void accept(Visitor visitor)
  {
    visitor.visit(this);
  }

}
