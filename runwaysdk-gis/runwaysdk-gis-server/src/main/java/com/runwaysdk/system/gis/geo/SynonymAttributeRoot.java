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

public class SynonymAttributeRoot extends SynonymAttributeRootBase
{
  private static final long serialVersionUID = -834699164;
  
  public SynonymAttributeRoot(String parentOid, String childOid)
  {
    super(parentOid, childOid);
  }
  
  public SynonymAttributeRoot(com.runwaysdk.system.metadata.MdAttributeTerm parent, com.runwaysdk.system.gis.geo.Synonym child)
  {
    this(parent.getOid(), child.getOid());
  }
  
}
