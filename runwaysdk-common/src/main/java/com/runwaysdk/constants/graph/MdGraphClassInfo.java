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
import com.runwaysdk.constants.MdClassInfo;

public interface MdGraphClassInfo extends MdClassInfo
{
  /**
   * Class {@link MdGraphClassInfo}.
   */
  public static final String CLASS                   = Constants.METADATA_PACKAGE + ".MdGraphClass";

  /**
   * OID.
   */
  public static final String ID_VALUE                = "da5013dd-9e05-3532-8869-300f8100003a";

  /**
   * The name of the class in the database.
   */
  public static final String DB_CLASS_NAME           = "dbClassName";

  /**
   * Flag indicating if the type has change over time enabled
   */
  public static final String ENABLE_CHANGE_OVER_TIME = "enableChangeOverTime";
}
