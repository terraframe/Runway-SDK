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
package com.runwaysdk.gis.dataaccess.attributes.value;

import com.runwaysdk.gis.constants.MdAttributePolygonInfo;
import com.runwaysdk.gis.dataaccess.MdAttributePolygonDAOIF;
import com.runwaysdk.gis.transport.metadata.AttributePolygonMdDTO;

public class MdAttributePolygon_Q extends MdAttributeGeometry_Q implements MdAttributePolygonDAOIF
{
  /**
   *
   */
  private static final long serialVersionUID = 4815860272255668580L;

  /**
   * Used in value objects with attributes that contain values that are the result of functions, where the function result
   * data type does not match the datatype of the column.
   * @param mdAttributeConcreteIF metadata that defines the column.
   */
  public MdAttributePolygon_Q(MdAttributePolygonDAOIF mdAttributeConcreteIF)
  {
    super(mdAttributeConcreteIF);
  }

  /**
   *
   * @see com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF#getType()
   */
  @Override
  public String getType()
  {
    return MdAttributePolygonInfo.CLASS;
  }

  @Override
  public String attributeMdDTOType()
  {
    return AttributePolygonMdDTO.class.getName();
  }

}
