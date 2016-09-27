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
package com.runwaysdk.gis.dataaccess.attributes.tranzient;

import com.runwaysdk.gis.dataaccess.AttributeGeometryIF;
import com.runwaysdk.gis.dataaccess.MdAttributeGeometryDAOIF;
import com.runwaysdk.dataaccess.attributes.tranzient.Attribute;
import com.vividsolutions.jts.geom.Geometry;

public abstract class AttributeGeometry extends Attribute implements AttributeGeometryIF
{
  /**
   * Creates an attribute with the given name and initializes the value to blank.
   * 
   * <br>
   * <b>Precondition: </b> name != null <br>
   * <b>Precondition: </b> !name.trim().equals("") <br>
   * <b>Precondition: </b> definingTransientType != null <br>
   * <b>Precondition: </b> !definingTransientType().equals("") <br>
   * <b>Precondition: </b> definingTransientType is the name of a class that defines an attribute with
   * this name
   * 
   * @param name name of the attribute
   * @param mdAttributeKey key of the defining metadata.
   * @param definingTransientType name of the class that defines this attribute
   */
  protected AttributeGeometry(String name, String mdAttributeKey, String definingTransientType)
  {
    super(name, mdAttributeKey, definingTransientType);
  }

  /**
   * Creates an attribute with the given name and initializes the value to the given
   * value.
   * 
   * <br>
   * <b>Precondition: </b> name != null <br>
   * <b>Precondition: </b> !name.trim().equals("") <br>
   * <b>Precondition: </b> definingTransientType != null <br>
   * <b>Precondition: </b> !definingTransientType.trim().equals("") <br>
   * <b>Precondition: </b> definingTransientType represents a class that defines an attribute with
   * this name <br>
   * <b>Precondition: </b> value != null
   * 
   * @param name name of the attribute
   * @param mdAttributeKey key of the defining metadata.
   * @param definingTransientType name of the class that defines this attribute
   * @param value initial value of the attribute
   */
  protected AttributeGeometry(String name, String mdAttributeKey, String definingTransientType, String value)
  {
    super(name, mdAttributeKey, definingTransientType, value);
  }
  
  /**
   * Returns the Geometry object.
   * @return Geometry object.
   */
  public abstract Geometry getGeometry();

  /**
   * Returns the BusinessDAO that defines the this attribute.
   * 
   * <br>
   * <b>Precondition: </b> true <br>
   * <b>Postcondition: </b> true
   * 
   * @return <code>MdAttributeGeometryDAOIF</code> that defines the this attribute
   */
  public MdAttributeGeometryDAOIF getMdAttributeConcrete()
  {
    return (MdAttributeGeometryDAOIF)super.getMdAttributeConcrete();
  }
}
