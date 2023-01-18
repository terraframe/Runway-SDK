/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
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
import com.runwaysdk.constants.MdClassInfo;

public interface MdEmbeddedGraphClassInfo extends MdClassInfo
{
  /**
   * Class {@link MdEmbeddedGraphClassInfo}.
   */
  public static final String CLASS                   = Constants.SYSTEM_GRAPH_PACKAGE + ".MdEmbeddedGraphClass";

  /**
   * OID.
   */
  public static final String ID_VALUE                = "c1cb1bb9-a732-3791-bb58-e2fcdd00003a";

  /**
   * The name of the class in the database.
   */
  public static final String DB_CLASS_NAME           = "dbClassName";
  
  /**
   * Super class attribute name
   */
  public static final String SUPER_MD_RELATIONSHIP = "superMdEmbedded";
}
