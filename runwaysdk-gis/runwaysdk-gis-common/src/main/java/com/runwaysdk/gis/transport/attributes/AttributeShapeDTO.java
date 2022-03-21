/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK GIS(tm).
 *
 * Runway SDK GIS(tm) is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Runway SDK GIS(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Runway SDK GIS(tm). If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.gis.transport.attributes;

import com.runwaysdk.gis.constants.MdAttributeShapeInfo;
import com.runwaysdk.gis.transport.metadata.AttributeShapeMdDTO;
import com.runwaysdk.gis.transport.metadata.GISCommonAttributeFacade;
import com.runwaysdk.transport.attributes.AttributeDTOFactory;
import com.vividsolutions.jts.geom.Geometry;

public class AttributeShapeDTO extends AttributeGeometryDTO
{

  /**
   * 
   */
  private static final long serialVersionUID = -7391705593916045363L;

  private Geometry          geometry;

  /**
   * Constructor to create a new Attribute.
   * 
   * @param name
   * @param value
   * @param writable
   * @param modified
   */
  public AttributeShapeDTO(String name, Geometry geometry, boolean readable, boolean writable, boolean modified)
  {
    super(name, "", readable, writable, modified);

    this.geometry = geometry;
  }

  /**
   * Constructor to create a new Attribute.
   * 
   * @param name
   * @param value
   * @param writable
   * @param modified
   */
  public AttributeShapeDTO(String name, String value, boolean readable, boolean writable, boolean modified)
  {
    super(name, value, readable, writable, modified);

    this.geometry = null;
  }

  /**
   * Returns the Geometry object.
   * 
   * @return Geometry object.
   */
  public Geometry getGeometry()
  {
    return this.geometry;
  }

  /**
   * Sets the polygon.
   * 
   * @param geometry
   */
  public void setGeometry(Geometry _geometry)
  {
    if (_geometry == null)
    {
      if (this.geometry != null)
      {
        this.setModified(true);
      }
    }
    else
    {
      if (this.geometry == null)
      {
        this.setModified(true);
      }
      else
      {
        if (!this.geometry.equalsExact(_geometry))
        {
          this.setModified(true);
        }
      }
    }

    this.geometry = _geometry;
  }

  /**
   * Assumes WKT format for a polygon. IMPORTANT! This is NOT converted into a
   * <code>Shape</code> object, because such conversions need to occur on the
   * server. Only the server has enough context to generate a localized error
   * message. Calling <code>getShape()</code> will not reflect the WKT passed
   * into this method unless the object is applied to the server, unless the
   * parameter is null or an empty String will the polygon object be set to null
   * immediately.
   * 
   * @param WKT
   */
  public void setValue(String value)
  {
    this.geometry = null;

    super.setValue(value);

  }

  /**
   * Sets the value of the attribute. Some attributes store objects instead of
   * strings. This method assumes you are passing in a Shape.
   * 
   * @param value
   */
  public void setValue(Object value)
  {
    this.setGeometry((Geometry) value);
  }

  /**
   * Returns a string representation of the polygon.
   * 
   * @return string representation of the polygon.
   */
  public String getValue()
  {
    if (this.geometry == null)
    {
      return super.getValue();
    }
    else
    {
      return this.geometry.toText();
    }
  }

  /**
   * Returns the value of the attribute.
   * 
   * @return Attribute value.
   */
  public Object getObjectValue()
  {
    return this.getGeometry();
  }

  @Override
  public String getType()
  {
    return MdAttributeShapeInfo.CLASS;
  }

  @Override
  public AttributeShapeDTO clone()
  {
    Geometry clonedGeom = null;
    if (this.geometry != null)
    {
      clonedGeom = (Geometry) this.geometry.clone();
    }

    AttributeShapeDTO clone = (AttributeShapeDTO) AttributeDTOFactory.createAttributeDTO(getName(), getType(), getValue(), isReadable(), isWritable(), isModified());

    clone.geometry = clonedGeom;

    GISCommonAttributeFacade.setGeometryMetadata(this.getAttributeMdDTO(), clone.getAttributeMdDTO());

    return clone;
  }

  @Override
  public AttributeShapeMdDTO getAttributeMdDTO()
  {
    return (AttributeShapeMdDTO) super.getAttributeMdDTO();
  }
}
