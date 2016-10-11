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
package com.runwaysdk.gis.dataaccess.attributes.value;

import com.runwaysdk.gis.dataaccess.MdAttributeGeometryDAOIF;
import com.runwaysdk.transport.metadata.caching.AttributeMdSession;
import com.runwaysdk.dataaccess.attributes.value.MdAttributeConcrete_Q;

public abstract class MdAttributeGeometry_Q extends MdAttributeConcrete_Q implements MdAttributeGeometryDAOIF
{
  /**
   * Used in value objects with attributes that contain values that are the result of functions, where the function result
   * data type does not match the datatype of the column.
   * @param mdAttributeConcreteIF metadata that defines the column.
   */
  public MdAttributeGeometry_Q(MdAttributeGeometryDAOIF mdAttributeConcreteIF)
  {
    super(mdAttributeConcreteIF);
  }

  /**
   * Returns the projection ID used for this geometry attribute;
   * @return projection ID used for this geometry attribute;
   */
  public int getSRID()
  {
    return ((MdAttributeGeometryDAOIF)this.mdAttributeConcreteIF).getSRID();
  }

  /**
   * Returns the dimension used for this attribute;
   * @return dimension used for this attribute;
   */
  public int getDimension()
  {
    return ((MdAttributeGeometryDAOIF)this.mdAttributeConcreteIF).getDimension();
  }
  
  @Override
  public AttributeMdSession getAttributeMdSession()
  {
    throw new UnsupportedOperationException("getAttributeMdSession() not supported for ["+this.getClass()+"].");
  }
}
