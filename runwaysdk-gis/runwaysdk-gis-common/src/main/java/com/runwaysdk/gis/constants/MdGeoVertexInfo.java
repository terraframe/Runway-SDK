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
package com.runwaysdk.gis.constants;

import com.runwaysdk.constants.graph.MdVertexInfo;

public interface MdGeoVertexInfo extends MdVertexInfo
{
  /**
   * Class {@link MdGeoVertexInfo}.
   */
  public static final String CLASS              = GISConstants.GRAPH_GIS_METADATA_PACKAGE+".MdGeoVertex";
  
  /**
   * OID.
   */
  public static final String ID_VALUE           = "61ea830a-2ea7-3943-9f99-a6561c00003a";  
  
  /**
   * SRID.
   */
  public static final String DEFAULT_SRID       = "4326";
  
  /**
   * SRID.
   */
  public static final String DEFAULT_DIMENSION  = "2";
}
