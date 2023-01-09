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

import com.runwaysdk.gis.constants.MdAttributeMultiLineStringInfo;
import com.runwaysdk.gis.transport.metadata.AttributeMultiLineStringMdDTO;
import com.runwaysdk.gis.transport.metadata.GISCommonAttributeFacade;
import com.runwaysdk.transport.attributes.AttributeDTOFactory;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.MultiLineString;

public class AttributeMultiLineStringDTO extends AttributeGeometryDTO
{

  /**
   * 
   */
  private static final long serialVersionUID = -6279019824973718964L;

  private MultiLineString multiLineString;
  
  /**
   * Constructor to create a new Attribute.
   * 
   * @param name
   * @param value
   * @param writable
   * @param modified
   */
  public AttributeMultiLineStringDTO(String name, MultiLineString multiLineString, boolean readable, boolean writable, boolean modified)
  {
    super(name, "", readable, writable, modified);
    
    this.multiLineString = multiLineString;
  }  
  
  /**
   * Constructor to create a new Attribute.
   * 
   * @param name
   * @param value
   * @param writable
   * @param modified
   */
  public AttributeMultiLineStringDTO(String name, String value, boolean readable, boolean writable, boolean modified)
  {
    super(name, value, readable, writable, modified);
    
    this.multiLineString = null;
  } 

  /**
   * Returns the Geometry object.
   * @return Geometry object.
   */
  public Geometry getGeometry()
  {
    return this.getMultiLineString();
  }
  
  /**
   * Returns the MultiLineString object.
   * @return MultiLineString object.
   */
  public MultiLineString getMultiLineString()
  {
    return this.multiLineString;
  }
  
  /**
   * Sets the MultiLineString.
   * @param multiLineString
   */
  public void setMultiLineString(MultiLineString _multiLineString)
  {
    if (_multiLineString == null)
    {
      if (this.multiLineString != null)
      {
        this.setModified(true);
      }
    }
    else
    {
      if (this.multiLineString == null)
      {
        this.setModified(true);
      }
      else
      {
        if (!this.multiLineString.equalsExact(_multiLineString) )
        {
          this.setModified(true);
        }
      }
    }
    
    this.multiLineString = _multiLineString;
  }

  /**
   * Assumes WKT format for a MultiLineString. IMPORTANT!  This is NOT converted
   * into a <code>MultiLineString</code> object, because such conversions need
   * to occur on the server.  Only the server has enough context to
   * generate a localized error message.  Calling <code>getMultiLineString()</code>
   * will not reflect the WKT passed into this method unless the object is
   * applied to the server, unless the parameter is null or an empty String
   * will the MultiLineString object be set to null immediately.
   * @param WKT
   */
  public void setValue(String value)
  {
    this.multiLineString = null;

    super.setValue(value);

  }

  /**
   * Sets the value of the attribute.  Some attributes store objects
   * instead of strings.  This method assumes you are passing
   * in a MultiLineString.
   * 
   * @param value
   */
  public void setValue(Object value)
  {
    this.setMultiLineString((MultiLineString)value);
  }

  /**
   * Returns a string representation of the MultiLineString.
   * @return string representation of the MultiLineString.
   */
  public String getValue()
  {
    if (this.multiLineString == null)
    {
      return super.getValue();
    }
    else
    {
      return this.multiLineString.toText();
    }
  }
  
  /**
   * Returns the value of the attribute.
   * 
   * @return Attribute value.
   */
  public Object getObjectValue()
  {
    return this.getMultiLineString();
  }
  
  @Override
  public String getType()
  {
    return MdAttributeMultiLineStringInfo.CLASS;
  }
  
  @Override
  public AttributeMultiLineStringDTO clone()
  {   
    MultiLineString clonedGeom = null;
    if (this.multiLineString != null)
    {
      clonedGeom = (MultiLineString)this.multiLineString.clone();
    }
    
    AttributeMultiLineStringDTO clone = 
      (AttributeMultiLineStringDTO)AttributeDTOFactory.createAttributeDTO(getName(), getType(), getValue(), isReadable(), isWritable(), isModified());

    clone.multiLineString = clonedGeom;
        
    GISCommonAttributeFacade.setGeometryMetadata(this.getAttributeMdDTO(), clone.getAttributeMdDTO());
    
    return clone;
  }
  
  @Override
  public AttributeMultiLineStringMdDTO getAttributeMdDTO()
  {
    return (AttributeMultiLineStringMdDTO) super.getAttributeMdDTO();
  }

}
