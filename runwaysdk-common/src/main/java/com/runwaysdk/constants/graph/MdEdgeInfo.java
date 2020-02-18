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
package com.runwaysdk.constants.graph;

import com.runwaysdk.constants.Constants;

public interface MdEdgeInfo extends MdGraphClassInfo
{
  /**
   * Class {@link MdEdgeInfo}.
   */
  public static final String CLASS             = Constants.METADATA_PACKAGE+".MdEdge";
  
  /**
   * OID.
   */
  public static final String ID_VALUE          = "bb38a595-0d71-38a9-8168-f0395300003a";  

  /**
   * Parent {@link MdVertexInfo} in the edge definition.
   */
  public static final String PARENT_MD_VERTEX  = "parentMdVertex";
  
  /**
   * Child {@link MdVertexInfo} in the edge definition.
   */
  public static final String CHILD_MD_VERTEX   = "childMdVertex";  
}
