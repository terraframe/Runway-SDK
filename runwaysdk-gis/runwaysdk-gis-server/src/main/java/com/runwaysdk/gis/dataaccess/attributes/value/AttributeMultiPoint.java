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

import java.util.Set;

import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.gis.dataaccess.AttributeMultiPointIF;
import com.runwaysdk.gis.dataaccess.MdAttributeMultiPointDAOIF;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.MultiPoint;

public class AttributeMultiPoint extends AttributeGeometry implements AttributeMultiPointIF
{

  /**
   *
   */
  private static final long serialVersionUID = 1854579262634218181L;

  private MultiPoint multiPoint = null;

  /**
   * Creates an attribute with the given name.
   *
   * <br>
   * <b>Precondition: </b> name != null <br>
   * <b>Precondition: </b> !name.trim().equals("") <br>
   * <b>Precondition: </b> value != null <br>
   * <b>Precondition: </b> definingEntityType != null <br>
   * <b>Precondition: </b> !definingEntityType().equals("") <br>
   * <b>Precondition: </b> definingEntityType is the name of a class that defines an attribute with
   * this name
   *
   * @param name name of the attribute
   * @param multiPoint the multiPoint of the attribute
   * @param definingEntityType name of the class that defines this attribute from which the value came
   * @param mdAttributeIF metadata that defines the attribute from which the value came.
   * @param entityMdAttributeIFset all MdAttributes that were involved in constructing this attribute.
   */
  protected AttributeMultiPoint(String name, MultiPoint multiPoint, String definingEntityType, MdAttributeConcreteDAOIF mdAttributeIF, Set<MdAttributeConcreteDAOIF> entityMdAttributeIFset)
  {
    super(name, "", definingEntityType, mdAttributeIF, entityMdAttributeIFset);

    this.multiPoint = multiPoint;
  }

  @Override
  public Geometry getGeometry()
  {
    return this.getMultiPoint();
  }

  /**
   * Returns the MultiPoint object.
   * @return MultiPoint object.
   */
  public MultiPoint getMultiPoint()
  {
    return this.multiPoint;
  }

  /**
   * Returns the value of the attribute as stored in the database.
   * Not all objects are represented as Strings in the <code>Attribute</code>
   * hierarchy.
   *
   * <br>
   * <b>Precondition: </b> true <br>
   * <b>Postcondition: </b> return value != null
   *
   * @return value of the attribute
   */
  public Object getRawValueObject()
  {
    return this.multiPoint;
  }

  /**
   * Returns a string representation of the line.
   * @return string representation of the line.
   */
  public String getValue()
  {
    if (this.multiPoint == null)
    {
      return "";
    }
    else
    {
      return this.multiPoint.toText();
    }
  }

  /**
   * Some attributes store objects instead of strings.
   * @param name
   * @return object stored on the attribute.
   */
  public Object getObjectValue()
  {
    return this.getMultiPoint();
  }

  /**
   * Returns the BusinessDAO that defines the this attribute.
   *
   * <br>
   * <b>Precondition: </b> true <br>
   * <b>Postcondition: </b> true
   *
   * @return MdAttributeMultiPointDAOIF that defines the this attribute
   */
  public MdAttributeMultiPointDAOIF getMdAttribute()
  {
    return (MdAttributeMultiPointDAOIF)super.getMdAttribute();
  }

}
