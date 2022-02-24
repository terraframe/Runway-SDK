/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.transport.attributes;

import java.io.Serializable;

import com.runwaysdk.business.ComponentDTO;
import com.runwaysdk.transport.metadata.AttributeMdDTO;
import com.runwaysdk.transport.metadata.AttributeMdDTOFactory;

/**
 * Describes an attribute
 */
public abstract class AttributeDTO implements Serializable, Cloneable
{
  /**
   * 
   */
  private static final long serialVersionUID = -6075663876028068314L;
  
  /**
   * The AttributeMdDTO that describes this AttributeDTOs metadata.
   */
  private AttributeMdDTO attributeMdDTO;

  /**
   * The name of the attribute.
   */
  private String          name;

  /**
   * The value of the attribute.
   */
  private String          value;

  /**
   * The flag denoting if the attribute can be edited or not.
   */
  private boolean         writable;

  /**
   * The flag denoting if the attribute can be read or not.
   */
  private boolean         readable;
  
  /**
   * The flag denoting if the attribute has been modified or not.
   */
  private boolean         modified;
  
  /**
   * Reference to the object that contains this attribute.
   */
  private ComponentDTO    containingDTO;
  
  /**
   * Constructor to create a new Attribute.
   * 
   * @param name
   * @param value
   * @param writable
   * @param modified
   */
  public AttributeDTO(String name, String value, boolean readable, boolean writable, boolean modified)
  {
    this.name = name;
    this.value = value;
    this.readable = readable;
    this.writable = writable;
    this.modified = modified;
    this.attributeMdDTO = AttributeMdDTOFactory.createAttributeMd(getType());
  }

  /**
   * Sets a reference to the given attribute metadata.
   * @param attributeMdDTO
   */
  protected void setAttributeMdDTO(AttributeMdDTO attributeMdDTO)
  {
    this.attributeMdDTO = attributeMdDTO;
  }
  
  /**
   * Sets the DTO that contains this attribute.
   * @param containingDTO
   */
  public void setContainingDTO(ComponentDTO containingDTO)
  {
    this.containingDTO = containingDTO;
  }
  
  /**
   * Returns the DTO that contains this attrbiute.
   * @return DTO that contains this attrbiute.
   */
  public ComponentDTO getContainingDTO()
  {
    return this.containingDTO;
  }
  
  /**
   * Returns the attribute metadata object;
   * @return
   */
  public AttributeMdDTO getAttributeMdDTO()
  {
    return attributeMdDTO;
  }
  
  /**
   * Returns the name of the attribute.
   * 
   * @return Attribute name.
   */
  public String getName()
  {
    return name;
  }

  /**
   * Sets the value of the attribute.
   * 
   * @param value
   */
  public void setValue(String value)
  {
    if (value == null)
    {
      this.value = "";
    }
    else
    {
      this.value = value;
    }
    this.setModified(true);
  }
  
  /**
   * Sets the value of the attribute.  Some attributes store objects
   * instead of strings.  This method assumes you are passing
   * in a string.
   * 
   * @param value
   */
  public void setValue(Object value)
  {
    this.setValue((String)value);
  }

  /**
   * Returns the value of the attribute.
   * 
   * @return Attribute value.
   */
  public String getValue()
  {
    return value;
  }
  
  /**
   * Returns the value of the attribute.
   * 
   * @return Attribute value.
   */
  public Object getObjectValue()
  {
    return this.getValue();
  }
  
  /**
   * Returns the type of this attribute.
   */
  public abstract String getType();
  
  /**
   * Returns the flag denoting if the attribute can be read.
   * 
   * @return true if it can be read, false otherwise.
   */
  public boolean isReadable()
  {
    return readable;
  }

  /**
   * Returns the flag denoting if the attribute cannot be edited.
   * 
   * @return true if it can be edited, false otherwise.
   */
  public boolean isWritable()
  {
    return writable;
  }

  /**
   * Sets the flag if an attribute is writable or not. A flag of true means
   * the attribute is writable, false otherwise.
   * 
   * @param modified
   */
  public void setModified(boolean modified)
  {
    this.modified = modified;
  }

  /**
   * Returns the flag denoting if an attribute has been modified.
   * 
   * @return true if the attribute has been modified, false otherwise.
   */
  public boolean isModified()
  {
    return modified;
  }

  /**
   * String representation of an Attribute
   * 
   * @return Object String
   */
  public String toString()
  {
    String data = "Name: " + name + "\n";
    data += "Type: " + getType() + "\n";
    return data;
  }
  
  /**
   * Clones this attribute and returns the clone.
   * Note that this is a shallow clone.
   * 
   * @return the cloned attribute
   */
  public AttributeDTO clone()
  {
	  AttributeDTO attributeDTO = 
		  AttributeDTOFactory.createAttributeDTO(getName(), getType(), getValue(), isReadable(), isWritable(), isModified());
	  
	  return attributeDTO;
  }
}
