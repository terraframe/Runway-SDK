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

import com.runwaysdk.gis.constants.MdAttributeMultiPointInfo;
import com.runwaysdk.gis.transport.metadata.AttributeMultiPointMdDTO;
import com.runwaysdk.gis.transport.metadata.GISCommonAttributeFacade;
import com.runwaysdk.transport.attributes.AttributeDTOFactory;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.MultiPoint;

public class AttributeMultiPointDTO extends AttributeGeometryDTO
{
  /**
   * 
   */
  private static final long serialVersionUID = -854323237429408760L;

  private MultiPoint multiPoint;

  /**
   * Constructor to create a new Attribute.
   * 
   * @param name
   * @param value
   * @param writable
   * @param modified
   */
  public AttributeMultiPointDTO(String name, MultiPoint multiPoint, boolean readable, boolean writable, boolean modified)
  {
    super(name, "", readable, writable, modified);
    
    this.multiPoint = multiPoint;
  }  
  
  /**
   * Constructor to create a new Attribute.
   * 
   * @param name
   * @param value
   * @param writable
   * @param modified
   */
  public AttributeMultiPointDTO(String name, String value, boolean readable, boolean writable, boolean modified)
  {
    super(name, value, readable, writable, modified);
    
    this.multiPoint = null;
  }
 
  /**
   * Returns the Geometry object.
   * @return Geometry object.
   */
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
   * Sets the multiPoint.
   * @param multiPoint
   */
  public void setMultiPoint(MultiPoint _multiPoint)
  {
    if (_multiPoint == null)
    {
      if (this.multiPoint != null)
      {
        this.setModified(true);
      }
    }
    else
    {
      if (this.multiPoint == null)
      {
        this.setModified(true);
      }
      else
      {
        if (!this.multiPoint.equalsExact(_multiPoint) )
        {
          this.setModified(true);
        }
      }
    }
    
    this.multiPoint = _multiPoint;
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
    this.multiPoint = null;

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
    this.setMultiPoint((MultiPoint)value);
  }

  /**
   * Returns a string representation of the points.
   * @return string representation of the points.
   */
  public String getValue()
  {
    if (this.multiPoint == null)
    {
      return super.getValue();
    }
    else
    {
      return this.multiPoint.toText();
    }
  }

  /**
   * Returns the value of the attribute.
   * 
   * @return Attribute value.
   */
  public Object getObjectValue()
  {
    return this.getMultiPoint();
  }

  @Override
  public String getType()
  {
    return MdAttributeMultiPointInfo.CLASS;
  }

  @Override
  public AttributeMultiPointDTO clone()
  {   
    MultiPoint clonedGeom = null;
    if (this.multiPoint != null)
    {
      clonedGeom = (MultiPoint)this.multiPoint.clone();
    }
    
    AttributeMultiPointDTO clone = 
      (AttributeMultiPointDTO)AttributeDTOFactory.createAttributeDTO(getName(), getType(), getValue(), isReadable(), isWritable(), isModified());

    clone.multiPoint = clonedGeom;
        
    GISCommonAttributeFacade.setGeometryMetadata(this.getAttributeMdDTO(), clone.getAttributeMdDTO());
    
    return clone;
  }
  
  @Override
  public AttributeMultiPointMdDTO getAttributeMdDTO()
  {
    return (AttributeMultiPointMdDTO ) super.getAttributeMdDTO();
  }


}
