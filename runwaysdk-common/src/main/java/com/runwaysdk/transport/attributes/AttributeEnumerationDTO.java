/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.runwaysdk.business.BusinessDTO;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.constants.MdAttributeEnumerationInfo;
import com.runwaysdk.transport.metadata.AttributeEnumerationMdDTO;
import com.runwaysdk.transport.metadata.CommonAttributeFacade;

/**
 * Describes an attribute enumeration.
 */
public class AttributeEnumerationDTO extends AttributeDTO implements Serializable
{
  /**
   * Auto-generated ID
   */
  private static final long serialVersionUID = 7205728038373659039L;
  
  /**
   * Map of enum names
   * Key: name of the enum item
   */
  private Set<String>   enumNameSet;

  /**
   * Constructor to create a new AttributeEnumeration object.
   * 
   * @param name
   * @param value
   * @param writable
   * @param modified
   */
  protected AttributeEnumerationDTO(String name, String value, boolean readable, boolean writable, boolean modified)
  {
    super(name, value, readable, writable, modified);
    enumNameSet = new HashSet<String>();
  }
  
  /**
   * Adds an enumeration item to this attribute.
   * An empty string is stored for the display label.
   * 
   * @param enumName enum name of the item to add.
   */
  public void addEnumItem(String enumName)
  {
	if(isWritable())
    {
      addEnumItemInternal(enumName);
      setModified(true);
	}
  }
  
  /**
   * Internal method to add an enum item without doing
   * a permissions check. This is required in the case 
   * when a DTO is leaving the server and the user has
   * read permission but not write permission on the 
   * attribute.
   * 
   * @param enumName
   */
  void addEnumItemInternal(String enumName)
  {
    if(isReadable() || isWritable())
    {
      // clear map if select multiple is not supported
      if(!getAttributeMdDTO().selectMultiple())
      {
        enumNameSet.clear();
      }

      enumNameSet.add(enumName);
    }
  }
  
  /**
   * Sets the value of this attribute enumeration.
   * The value of an attribute enumeration is the enumName, but
   * users aren't allowed to set that directly. Instead, setting
   * a value on an enumeration is the same as calling addEnumItem(item).
   */
  public void setValue(String value)
  {
    addEnumItem(value);
  }
  
  /**
   * Removes an enumeration item from this attribute.
   * 
   * @param enumName
   */
  public void removeEnumItem(String enumName)
  {
	if(isWritable())
	{
		enumNameSet.remove(enumName);
        setModified(true);
	}
  }

  /**
   * Clears all enumeration items on this attribute.
   */
  public void clearEnum()
  {
	if(isWritable())
	{
		enumNameSet.clear();
        setModified(true);
	}
  }

  /**
   * Returns a list of the enum names.  
   * @return list of the enum names. 
   */
  public List<String> getEnumNames()
  {
    List<String> enumNameList = new LinkedList<String>();
    
    Iterator<String> iterator = enumNameSet.iterator();
    
    while (iterator.hasNext())
    {
      enumNameList.add(iterator.next());
    }

    return enumNameList;
  }
  
  /**
   * Returns a list of the enum display labels.  
   * @return list of the enum display labels. 
   */
  public List<String> getEnumLabels()
  {
    List<String> enumDisplayLabels = new LinkedList<String>();
    
    Iterator<String> iterator = enumNameSet.iterator();
    while (iterator.hasNext())
    {
      String displayLabel = this.getAttributeMdDTO().getEnumDisplayLabel(iterator.next());
      enumDisplayLabels.add(displayLabel);
    }

    return enumDisplayLabels;
  }
  
  /**
   * Returns a map where the key is the attribute name 
   * and the value is the display label.
   * @return map where the key is the attribute name 
   * and the value is the display label.
   */
  public Map<String, String> getEnumItems()
  {
    Map<String, String> enumNameMap = new HashMap<String, String>();

    Iterator<String> iterator = enumNameSet.iterator();
    while (iterator.hasNext())
    {
      String enumName = iterator.next();
      String displayLabel = this.getAttributeMdDTO().getEnumDisplayLabel(enumName);
      enumNameMap.put(enumName, displayLabel);
    }

    return enumNameMap;
  }
  
  /**
   * If this is called on the front end, it will return all BusinessDTOs for which this
   * object has an internal mapping.  If this is called on the  back-end, it will only 
   * return an empty list.  This method should not be called from the server anyway.  There 
   * is no need for a server method to get the BusinessDTOs representing the enum items.
   * 
   * <br/>
   * <b>Precondition</b>: this.getContainingDTO().getRequest() != null<br/>
   * 
   * @return String list of items.
   */
  public List<? extends BusinessDTO> getEnumValues()
  {   
    ClientRequestIF clientRequest = this.getContainingDTO().getRequest();
    
    // This is being called from the server.  This method should not be
    // called from the server anyway.  There is no need for a server method
    // to get the BusinessDTOs representing the enum items.
    if (clientRequest == null)
    {
      return new LinkedList<BusinessDTO>();
    }
    // This is being called from the client
    else
    {
      String[] enumNames = new String[this.getEnumNames().size()];
      this.getEnumNames().toArray(enumNames);

      String enumType = this.getAttributeMdDTO().getReferencedMdEnumeration();
      
      return clientRequest.getEnumerations(enumType, enumNames);
    }

  }
  
  @Override
  public String getType()
  {
    return MdAttributeEnumerationInfo.CLASS;
  }
  
  @Override
  public AttributeDTO clone()
  {
    // set the attribute value and metadata
    AttributeEnumerationDTO clone = (AttributeEnumerationDTO) super.clone();
    CommonAttributeFacade.setEnumerationMetadata(this.getAttributeMdDTO(), clone.getAttributeMdDTO());

    clone.enumNameSet = new HashSet<String>(this.enumNameSet);
    
    return clone;
  }
  
  @Override
  public AttributeEnumerationMdDTO getAttributeMdDTO()
  {
    return (AttributeEnumerationMdDTO) super.getAttributeMdDTO();
  }
}
