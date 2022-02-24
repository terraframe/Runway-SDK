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
package com.runwaysdk.constants;

public interface BusinessInfo extends ElementInfo
{
  /**
   * The Class. Unlike other CLASS constants, this does <i>not</i>
   * correlate directly with the database. This class is extended by generated
   * classes (which do have database counterparts), but Business.java
   * exists only as part of the bridge between layers.
   */
  public static final String CLASS = Constants.SYSTEM_BUSINESS_PACKAGE+".Business";

  public static final String ID_VALUE          = "6508693a-cfd0-3c54-a03e-8a836900003a";
  
  public static final String TABLE             = "business";
  
}
