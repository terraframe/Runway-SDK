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

public class LocatedIn extends LocatedInBase
{
  private static final long serialVersionUID = 645462528;

  public LocatedIn(String parentId, String childId)
  {
    super(parentId, childId);
  }

  public LocatedIn(com.runwaysdk.system.gis.geo.GeoEntity parent, com.runwaysdk.system.gis.geo.GeoEntity child)
  {
    this(parent.getOid(), child.getOid());
  }

  @Override
  public String buildKey()
  {
    return this.getParentId() + this.getChildId();
  }

  @Override
  public void apply()
  {
    // TODO Auto-generated method stub
    super.apply();
  }

}
