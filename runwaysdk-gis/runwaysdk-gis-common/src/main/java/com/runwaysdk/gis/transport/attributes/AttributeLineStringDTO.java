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

import com.runwaysdk.gis.constants.MdAttributeLineStringInfo;
import com.runwaysdk.gis.transport.metadata.AttributeLineStringMdDTO;
import com.runwaysdk.gis.transport.metadata.GISCommonAttributeFacade;
import com.runwaysdk.transport.attributes.AttributeDTOFactory;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;

public class AttributeLineStringDTO extends AttributeGeometryDTO
{

  /**
   * 
   */
  private static final long serialVersionUID = 3586476139860107579L;
  
  private LineString lineString;

  /**
   * Constructor to create a new Attribute.
   * 
   * @param name
   * @param value
   * @param writable
   * @param modified
   */
  public AttributeLineStringDTO(String name, LineString lineString, boolean readable, boolean writable, boolean modified)
  {
    super(name, "", readable, writable, modified);
    
    this.lineString = lineString;
  }  
  
  /**
   * Constructor to create a new Attribute.
   * 
   * @param name
   * @param value
   * @param writable
   * @param modified
   */
  public AttributeLineStringDTO(String name, String value, boolean readable, boolean writable, boolean modified)
  {
    super(name, value, readable, writable, modified);
    
    this.lineString = null;
  }
 
  /**
   * Returns the Geometry object.
   * @return Geometry object.
   */
  public Geometry getGeometry()
  {
    return this.getLineString();
  }
  
  /**
   * Returns the LineString object.
   * @return LineString object.
   */
  public LineString getLineString()
  {
    return this.lineString;
  }

  /**
   * Sets the lineString.
   * @param lineString
   */
  public void setLineString(LineString _lineString)
  {
    if (_lineString == null)
    {
      if (this.lineString != null)
      {
        this.setModified(true);
      }
    }
    else
    {
      if (this.lineString == null)
      {
        this.setModified(true);
      }
      else
      {
        if (!this.lineString.equalsExact(_lineString) )
        {
          this.setModified(true);
        }
      }
    }
    
    this.lineString = _lineString;
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
    this.lineString = null;

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
    this.setLineString((LineString)value);
  }

  /**
   * Returns a string representation of the points.
   * @return string representation of the points.
   */
  public String getValue()
  {
    if (this.lineString == null)
    {
      return super.getValue();
    }
    else
    {
      return this.lineString.toText();
    }
  }

  /**
   * Returns the value of the attribute.
   * 
   * @return Attribute value.
   */
  public Object getObjectValue()
  {
    return this.getLineString();
  }

  @Override
  public String getType()
  {
    return MdAttributeLineStringInfo.CLASS;
  }

  @Override
  public AttributeLineStringDTO clone()
  {   
    LineString clonedGeom = null;
    if (this.lineString != null)
    {
      clonedGeom = (LineString)this.lineString.clone();
    }
    
    AttributeLineStringDTO clone = 
      (AttributeLineStringDTO)AttributeDTOFactory.createAttributeDTO(getName(), getType(), getValue(), isReadable(), isWritable(), isModified());

    clone.lineString = clonedGeom;
        
    GISCommonAttributeFacade.setGeometryMetadata(this.getAttributeMdDTO(), clone.getAttributeMdDTO());
    
    return clone;
  }
  
  @Override
  public AttributeLineStringMdDTO getAttributeMdDTO()
  {
    return (AttributeLineStringMdDTO) super.getAttributeMdDTO();
  }

}
