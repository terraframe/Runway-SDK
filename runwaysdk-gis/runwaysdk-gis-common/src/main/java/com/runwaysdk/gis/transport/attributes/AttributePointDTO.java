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
package com.runwaysdk.gis.transport.attributes;

import com.runwaysdk.gis.constants.MdAttributePointInfo;
import com.runwaysdk.gis.transport.metadata.AttributePointMdDTO;
import com.runwaysdk.gis.transport.metadata.GISCommonAttributeFacade;
import com.runwaysdk.transport.attributes.AttributeDTOFactory;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;

public class AttributePointDTO extends AttributeGeometryDTO
{
  /**
   * 
   */
  private static final long serialVersionUID = 865646941160280162L;

  private Point point;
  
  /**
   * Constructor to create a new Attribute.
   * 
   * @param name
   * @param value
   * @param writable
   * @param modified
   */
  public AttributePointDTO(String name, Point point, boolean readable, boolean writable, boolean modified)
  {
    super(name, "", readable, writable, modified);
    
    this.point = point;
  }  
  
  /**
   * Constructor to create a new Attribute.
   * 
   * @param name
   * @param value
   * @param writable
   * @param modified
   */
  public AttributePointDTO(String name, String value, boolean readable, boolean writable, boolean modified)
  {
    super(name, value, readable, writable, modified);
    
    this.point = null;
  } 

  /**
   * Returns the Geometry object.
   * @return Geometry object.
   */
  public Geometry getGeometry()
  {
    return this.getPoint();
  }
  
  /**
   * Returns the Point object.
   * @return Point object.
   */
  public Point getPoint()
  {
    return this.point;
  }
  
  /**
   * Sets the point.
   * @param point
   */
  public void setPoint(Point _point)
  {
    if (_point == null)
    {
      if (this.point != null)
      {
        this.setModified(true);
      }
    }
    else
    {
      if (this.point == null)
      {
        this.setModified(true);
      }
      else
      {
        if (!this.point.equalsExact(_point) )
        {
          this.setModified(true);
        }
      }
    }
    
    this.point = _point;
  }

  /**
   * Assumes WKT format for a point. IMPORTANT!  This is NOT converted
   * into a <code>Point</code> object, because such conversions need
   * to occur on the server.  Only the server has enough context to
   * generate a localized error message.  Calling <code>getPoint()</code>
   * will not reflect the WKT passed into this method unless the object is
   * applied to the server, unless the parameter is null or an empty String
   * will the point object be set to null immediately.
   * @param WKT
   */
  public void setValue(String value)
  {
    this.point = null;

    super.setValue(value);

  }

  /**
   * Sets the value of the attribute.  Some attributes store objects
   * instead of strings.  This method assumes you are passing
   * in a Point.
   * 
   * @param value
   */
  public void setValue(Object value)
  {
    this.setPoint((Point)value);
  }

  /**
   * Returns a string representation of the points.
   * @return string representation of the points.
   */
  public String getValue()
  {
    if (this.point == null)
    {
      return super.getValue();
    }
    else
    {
      return this.point.toText();
    }
  }
  
  /**
   * Returns the value of the attribute.
   * 
   * @return Attribute value.
   */
  public Object getObjectValue()
  {
    return this.getPoint();
  }
  
  @Override
  public String getType()
  {
    return MdAttributePointInfo.CLASS;
  }
  
  @Override
  public AttributePointDTO clone()
  {   
    Point clonedGeom = null;
    if (this.point != null)
    {
      clonedGeom = (Point)this.point.clone();
    }
    
    AttributePointDTO clone = 
      (AttributePointDTO)AttributeDTOFactory.createAttributeDTO(getName(), getType(), getValue(), isReadable(), isWritable(), isModified());

    clone.point = clonedGeom;
        
    GISCommonAttributeFacade.setGeometryMetadata(this.getAttributeMdDTO(), clone.getAttributeMdDTO());
    
    return clone;
  }
  
  @Override
  public AttributePointMdDTO getAttributeMdDTO()
  {
    return (AttributePointMdDTO) super.getAttributeMdDTO();
  }

}
