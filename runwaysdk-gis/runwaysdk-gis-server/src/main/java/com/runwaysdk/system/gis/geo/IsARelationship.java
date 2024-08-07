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

public class IsARelationship extends IsARelationshipBase
{
  private static final long serialVersionUID = -1789452472;
  
  public IsARelationship(String parentOid, String childOid)
  {
    super(parentOid, childOid);
  }
  
  public IsARelationship(com.runwaysdk.system.gis.geo.Universal parent, com.runwaysdk.system.gis.geo.Universal child)
  {
    this(parent.getOid(), child.getOid());
  }
  
  @Override
  public String buildKey()
  {
    return this.getParentOid() + this.getChildOid();
  }
  
}
