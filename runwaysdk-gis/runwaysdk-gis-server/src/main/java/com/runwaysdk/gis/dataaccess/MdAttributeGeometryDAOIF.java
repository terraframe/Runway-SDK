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
package com.runwaysdk.gis.dataaccess;

import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;

public interface MdAttributeGeometryDAOIF extends MdAttributeConcreteDAOIF
{
  /**
   * Name of the table used to store instances of this class.
   */
  public static final String TABLE   = "md_attribute_geometry";
  
  /**
   * Returns the projection OID used for this geometry attribute;
   * @return projection OID used for this geometry attribute;
   */
  public int getSRID();
  
  /**
   * Returns the dimension used for this attribute;
   * @return dimension used for this attribute;
   */
  public int getDimension();
}
