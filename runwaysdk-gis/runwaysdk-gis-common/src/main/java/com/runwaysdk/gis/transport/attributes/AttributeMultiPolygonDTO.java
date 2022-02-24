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

import com.runwaysdk.gis.constants.MdAttributeMultiPolygonInfo;
import com.runwaysdk.gis.transport.metadata.AttributeMultiPolygonMdDTO;
import com.runwaysdk.gis.transport.metadata.GISCommonAttributeFacade;
import com.runwaysdk.transport.attributes.AttributeDTOFactory;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.MultiPolygon;

public class AttributeMultiPolygonDTO extends AttributeGeometryDTO
{

  /**
   * 
   */
  private static final long serialVersionUID = 3207036793843632370L;

  private MultiPolygon multiPolygon;
  
  /**
   * Constructor to create a new Attribute.
   * 
   * @param name
   * @param value
   * @param writable
   * @param modified
   */
  public AttributeMultiPolygonDTO(String name, MultiPolygon multiPolygon, boolean readable, boolean writable, boolean modified)
  {
    super(name, "", readable, writable, modified);
    
    this.multiPolygon = multiPolygon;
  }  
  
  /**
   * Constructor to create a new Attribute.
   * 
   * @param name
   * @param value
   * @param writable
   * @param modified
   */
  public AttributeMultiPolygonDTO(String name, String value, boolean readable, boolean writable, boolean modified)
  {
    super(name, value, readable, writable, modified);
    
    this.multiPolygon = null;
  } 

  /**
   * Returns the Geometry object.
   * @return Geometry object.
   */
  public Geometry getGeometry()
  {
    return this.getMultiPolygon();
  }
  
  /**
   * Returns the MultiPolygon object.
   * @return MultiPolygon object.
   */
  public MultiPolygon getMultiPolygon()
  {
    return this.multiPolygon;
  }
  
  /**
   * Sets the multiPolygon.
   * @param _multiPolygon
   */
  public void setMultiPolygon(MultiPolygon _multiPolygon)
  {
    if (_multiPolygon == null)
    {
      if (this.multiPolygon != null)
      {
        this.setModified(true);
      }
    }
    else
    {
      if (this.multiPolygon == null)
      {
        this.setModified(true);
      }
      else
      {
        if (!this.multiPolygon.equalsExact(_multiPolygon) )
        {
          this.setModified(true);
        }
      }
    }
    
    this.multiPolygon = _multiPolygon;
  }

  /**
   * Assumes WKT format for a MultiPolygon. IMPORTANT!  This is NOT converted
   * into a <code>MultiPolygon</code> object, because such conversions need
   * to occur on the server.  Only the server has enough context to
   * generate a localized error message.  Calling <code>getMultiPolygon()</code>
   * will not reflect the WKT passed into this method unless the object is
   * applied to the server, unless the parameter is null or an empty String
   * will the MultiPolygon object be set to null immediately.
   * @param WKT
   */
  public void setValue(String value)
  {
    this.multiPolygon = null;

    super.setValue(value);

  }

  /**
   * Sets the value of the attribute.  Some attributes store objects
   * instead of strings.  This method assumes you are passing
   * in a MultiPolygon.
   * 
   * @param value
   */
  public void setValue(Object value)
  {
    this.setMultiPolygon((MultiPolygon)value);
  }

  /**
   * Returns a string representation of the MultiPolygon.
   * @return string representation of the MultiPolygon.
   */
  public String getValue()
  {
    if (this.multiPolygon == null)
    {
      return super.getValue();
    }
    else
    {
      return this.multiPolygon.toText();
    }
  }
  
  /**
   * Returns the value of the attribute.
   * 
   * @return Attribute value.
   */
  public Object getObjectValue()
  {
    return this.getMultiPolygon();
  }
  
  @Override
  public String getType()
  {
    return MdAttributeMultiPolygonInfo.CLASS;
  }
  
  @Override
  public AttributeMultiPolygonDTO clone()
  {   
    MultiPolygon clonedGeom = null;
    if (this.multiPolygon != null)
    {
      clonedGeom = (MultiPolygon)this.multiPolygon.clone();
    }
    
    AttributeMultiPolygonDTO clone = 
      (AttributeMultiPolygonDTO)AttributeDTOFactory.createAttributeDTO(getName(), getType(), getValue(), isReadable(), isWritable(), isModified());

    clone.multiPolygon = clonedGeom;
        
    GISCommonAttributeFacade.setGeometryMetadata(this.getAttributeMdDTO(), clone.getAttributeMdDTO());
    
    return clone;
  }
  
  @Override
  public AttributeMultiPolygonMdDTO getAttributeMdDTO()
  {
    return (AttributeMultiPolygonMdDTO) super.getAttributeMdDTO();
  }

}
