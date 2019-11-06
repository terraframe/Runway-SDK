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

import com.runwaysdk.constants.ComponentInfo;
import com.runwaysdk.constants.Constants;

public interface GraphClassInfo extends ComponentInfo
{
  /**
   * This class, unlike other CLASS constants, this does <i>not</i>
   * correlate directly with the database. This class is extended by generated
   * classes (which do have database counterparts), but {@link GraphClass}
   * exists only as part of the bridge between layers.
   */
  public static final String CLASS = Constants.SYSTEM_BUSINESS_PACKAGE+".GraphClass";
  
  // There is no class defined called {@link GraphClass} defined, so the existing architecture breaks
  // down here a bit. Therefore, no value is defined here and {@link GraphClass#ID_VALUE}
  // public static final String ID_VALUE          = "";
}
