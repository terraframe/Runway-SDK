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
package com.runwaysdk.gis.transport.attributes;

import com.runwaysdk.gis.constants.MdAttributePolygonInfo;
import com.runwaysdk.gis.transport.metadata.AttributePolygonMdDTO;
import com.runwaysdk.gis.transport.metadata.GISCommonAttributeFacade;
import com.runwaysdk.transport.attributes.AttributeDTOFactory;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Polygon;

public class AttributePolygonDTO extends AttributeGeometryDTO
{

  /**
   * 
   */
  private static final long serialVersionUID = -7391705593916045363L;

  private Polygon polygon;
  
  /**
   * Constructor to create a new Attribute.
   * 
   * @param name
   * @param value
   * @param writable
   * @param modified
   */
  public AttributePolygonDTO(String name, Polygon polygon, boolean readable, boolean writable, boolean modified)
  {
    super(name, "", readable, writable, modified);
    
    this.polygon = polygon;
  }  
  
  /**
   * Constructor to create a new Attribute.
   * 
   * @param name
   * @param value
   * @param writable
   * @param modified
   */
  public AttributePolygonDTO(String name, String value, boolean readable, boolean writable, boolean modified)
  {
    super(name, value, readable, writable, modified);
    
    this.polygon = null;
  } 

  /**
   * Returns the Geometry object.
   * @return Geometry object.
   */
  public Geometry getGeometry()
  {
    return this.getPolygon();
  }
  
  /**
   * Returns the Polygon object.
   * @return Polygon object.
   */
  public Polygon getPolygon()
  {
    return this.polygon;
  }
  
  /**
   * Sets the polygon.
   * @param polygon
   */
  public void setPolygon(Polygon _polygon)
  {
    if (_polygon == null)
    {
      if (this.polygon != null)
      {
        this.setModified(true);
      }
    }
    else
    {
      if (this.polygon == null)
      {
        this.setModified(true);
      }
      else
      {
        if (!this.polygon.equalsExact(_polygon) )
        {
          this.setModified(true);
        }
      }
    }
    
    this.polygon = _polygon;
  }

  /**
   * Assumes WKT format for a polygon. IMPORTANT!  This is NOT converted
   * into a <code>Polygon</code> object, because such conversions need
   * to occur on the server.  Only the server has enough context to
   * generate a localized error message.  Calling <code>getPolygon()</code>
   * will not reflect the WKT passed into this method unless the object is
   * applied to the server, unless the parameter is null or an empty String
   * will the polygon object be set to null immediately.
   * @param WKT
   */
  public void setValue(String value)
  {
    this.polygon = null;

    super.setValue(value);

  }

  /**
   * Sets the value of the attribute.  Some attributes store objects
   * instead of strings.  This method assumes you are passing
   * in a Polygon.
   * 
   * @param value
   */
  public void setValue(Object value)
  {
    this.setPolygon((Polygon)value);
  }

  /**
   * Returns a string representation of the polygon.
   * @return string representation of the polygon.
   */
  public String getValue()
  {
    if (this.polygon == null)
    {
      return super.getValue();
    }
    else
    {
      return this.polygon.toText();
    }
  }
  
  /**
   * Returns the value of the attribute.
   * 
   * @return Attribute value.
   */
  public Object getObjectValue()
  {
    return this.getPolygon();
  }
  
  @Override
  public String getType()
  {
    return MdAttributePolygonInfo.CLASS;
  }
  
  @Override
  public AttributePolygonDTO clone()
  {   
    Polygon clonedGeom = null;
    if (this.polygon != null)
    {
      clonedGeom = (Polygon)this.polygon.clone();
    }
    
    AttributePolygonDTO clone = 
      (AttributePolygonDTO)AttributeDTOFactory.createAttributeDTO(getName(), getType(), getValue(), isReadable(), isWritable(), isModified());

    clone.polygon = clonedGeom;
        
    GISCommonAttributeFacade.setGeometryMetadata(this.getAttributeMdDTO(), clone.getAttributeMdDTO());
    
    return clone;
  }
  
  @Override
  public AttributePolygonMdDTO getAttributeMdDTO()
  {
    return (AttributePolygonMdDTO) super.getAttributeMdDTO();
  }
}
