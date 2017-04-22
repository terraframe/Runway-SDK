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
package com.runwaysdk.gis.dataaccess.attributes.entity;

import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.gis.dataaccess.AttributeGeometryIF;
import com.runwaysdk.gis.dataaccess.MdAttributeGeometryDAOIF;
import com.vividsolutions.jts.geom.Geometry;

public abstract class AttributeGeometry extends Attribute implements AttributeGeometryIF
{
  /**
   * 
   */
  private static final long serialVersionUID = -3028633335915503456L;

  /**
   * Inherited constrcutor, sets <code>name</code> and <code>definingEntityType</code>.
   * 
   * @param name The name of this character attribute.
   * @param mdAttributeKey key of the defining attribute metadata
   * @param definingEntityType The class that defines this attribute.
   */
  protected AttributeGeometry(String name, String mdAttributeKey, String definingEntityType)
  {
    super(name, mdAttributeKey, definingEntityType);
  }

  /**
   * Returns the Geometry object.
   * @return Geometry object.
   */
  public abstract Geometry getGeometry();
  
  /**
   * Returns the JDBC prepared statement variable used for this type.
   * Most just need to return the "?" character, but others require that
   * special methods or variables are used in addition.
   * 
   * <br>
   * <b>Precondition: </b> true <br>
   * <b>Postcondition: </b> return value != null
   * 
   * @return value of the attribute
   */
  public String getPreparedStatementVar()
  {
    if (this.getGeometry() == null)
    {
      return "?";
    }
    else
    {
      return "ST_GeomFromEWKT(?)";
    }
  }
  
  /**
   * Returns the BusinessDAO that defines the this attribute.
   * 
   * <br>
   * <b>Precondition: </b> true <br>
   * <b>Postcondition: </b> true
   * 
   * @return <code>MdAttributeGeometryDAOIF</code> that defines the this attribute
   */
  public MdAttributeGeometryDAOIF getMdAttribute()
  {
    return (MdAttributeGeometryDAOIF)super.getMdAttribute();
  }
}
