/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved.
 * 
 * This file is part of Runway SDK(tm).
 * 
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.runwaysdk.business;

import java.util.Map;

import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.transport.attributes.AttributeBlobDTO;
import com.runwaysdk.transport.attributes.AttributeDTO;
import com.runwaysdk.transport.attributes.AttributeEnumerationDTO;
import com.runwaysdk.transport.attributes.AttributeMultiReferenceDTO;

public abstract class MutableDTO extends ComponentDTO
{

  /**
   * 
   */
  private static final long   serialVersionUID = 1009627087661984045L;

  /**
   * Flag denoting if this is a new instance or not. If this is a new instance,
   * the value of this.newInstance will be true.
   */
  private boolean             newInstance;

  /**
   * The default writable value of an attribute.
   */
  public static final boolean WRITABLE_DEFAULT = false;

  /**
   * The default modified value of an attribute.
   */
  public static final boolean MODIFIED_DEFAULT = false;

  /**
   * Flag denoting if this entity as a whole has been modified. This will be set
   * to true if an attribute is modified.
   */
  private boolean             modified         = false;

  /**
   * Flag denoting if this DTO as a whole can be written.
   */
  private boolean             writable         = false;

  /**
   * Constructor used when the object is instantiated on the front-end.
   */
  protected MutableDTO(ClientRequestIF clientRequest, String type)
  {
    super(clientRequest, type);
  }

  /**
   * Constructor used when the object is instantiated on the front-end or
   * back-end. If the clientRequest is null, then it is instantiated on the
   * front-end, otherwise on the back-end.
   */
  protected MutableDTO(ClientRequestIF clientRequest, String type, Map<String, AttributeDTO> attributeMap)
  {
    super(clientRequest, type, attributeMap);
  }

  /**
   * Constructor used when the object is instantiated on the front-end.
   */
  protected MutableDTO(ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }

  /**
   * Constructor used when the object is instantiated on the front-end or
   * back-end. If the clientRequest is null, then it is instantiated on the
   * front-end, otherwise on the back-end.
   */
  protected MutableDTO(ClientRequestIF clientRequest, Map<String, AttributeDTO> attributeMap)
  {
    super(clientRequest, attributeMap);
  }

  /**
   * Checks if this entity has been modified.
   * 
   * @return true if the entity has been modified, false otherwise.
   */
  public boolean isModified()
  {
    return this.modified;
  }

  /**
   * Sets the flag denoting whether or not this entity (as a whole) has been
   * modified.
   * 
   * @param modified
   */
  public void setModified(boolean modified)
  {
    this.modified = modified;
  }

  /**
   * Checks if this entity is writable.
   * 
   * @return true if the entity is writable, false otherwise.
   */
  public boolean isWritable()
  {
    return this.writable;
  }

  /**
   * Sets the writable value on this entity.
   * 
   * @param writable
   */
  protected void setWritable(boolean writable)
  {
    this.writable = writable;
  }

  /**
   * Checks if an attribute can be written or not.
   * 
   * @param attributeName
   * @return true if the value can be edited, false otherwise.
   */
  public boolean isWritable(String attributeName)
  {
    if (attributeMap.containsKey(attributeName))
    {
      AttributeDTO attribute = attributeMap.get(attributeName);
      return attribute.isWritable();
    }
    else
    {
      return WRITABLE_DEFAULT;
    }
  }

  /**
   * Checks if this BusinessDTO instance is a new instance or not.
   * 
   * @return true if it is a new instance, false otherwise.
   */
  public boolean isNewInstance()
  {
    return this.newInstance;
  }

  /**
   * Sets the flag denoting if this a new instance or not. Boolean true means
   * it's a new instance, false does not.
   * 
   * @param isNew
   */
  protected void setNewInstance(boolean isNew)
  {
    this.newInstance = isNew;
  }

  /**
   * Sets the value denoting if an attribute has been modified or nto.
   * 
   * @param attributeName
   * @param set
   *          true if the attribute has been modified, false otherwise.
   */
  public void setModified(String attributeName, boolean modified)
  {
    if (attributeMap.containsKey(attributeName))
    {
      AttributeDTO attribute = attributeMap.get(attributeName);
      attribute.setModified(modified);
    }
  }

  /**
   * Checks if an attribute has been modified or not.
   * 
   * @param attributeName
   * @return true if the attribute has been modified, false otherwise.
   */
  public boolean isModified(String attributeName)
  {
    if (attributeMap.containsKey(attributeName))
    {
      AttributeDTO attribute = attributeMap.get(attributeName);
      return attribute.isModified();
    }
    else
    {
      return MODIFIED_DEFAULT;
    }
  }

  /**
   * Sets the value of a given attribute.
   * 
   * @param attributeName
   * @param value
   */
  public void setValue(String attributeName, String value)
  {
    if (attributeMap.containsKey(attributeName))
    {
      AttributeDTO attribute = this.getAttributeDTO(attributeName);
      attribute.setValue(value);
      this.setModified(true);
    }
  }

  /**
   * Sets the value of a given attribute.
   * 
   * @param attributeName
   * @param value
   */
  public void setValue(String attributeName, Object value)
  {
    if (attributeMap.containsKey(attributeName))
    {
      AttributeDTO attribute = this.getAttributeDTO(attributeName);
      attribute.setValue(value);
      this.setModified(true);
    }
  }

  /**
   * Adds an enumeration item to an enumeration attribute. This method requires
   * that the attribute has already been added via the addAttribute() method.
   * 
   * @param attributeName
   *          The name of the attribute.
   * @param enumName
   *          The name of the item to add to the attribute.
   */
  public void addEnumItem(String attributeName, String enumName)
  {
    if (attributeMap.containsKey(attributeName))
    {
      AttributeEnumerationDTO enumeration = (AttributeEnumerationDTO) attributeMap.get(attributeName);
      enumeration.addEnumItem(enumName);
      setModified(true);
    }
  }

  /**
   * Clears the enumeration items on an attribute enumeration.
   * 
   * @param attributeName
   */
  public void clearEnum(String attributeName)
  {
    if (attributeMap.containsKey(attributeName))
    {
      AttributeEnumerationDTO enumeration = (AttributeEnumerationDTO) attributeMap.get(attributeName);
      enumeration.clearEnum();
      setModified(true);
    }
  }

  /**
   * 
   * @param enumerationName
   * @param enumName
   */
  public void removeEnumItem(String enumerationName, String enumName)
  {
    if (attributeMap.containsKey(enumerationName))
    {
      AttributeEnumerationDTO enumeration = (AttributeEnumerationDTO) attributeMap.get(enumerationName);
      enumeration.removeEnumItem(enumName);
      setModified(true);
    }
  }

  public void setBlob(String attributeName, byte[] value)
  {
    if (attributeMap.containsKey(attributeName))
    {
      AttributeBlobDTO attributeBlob = (AttributeBlobDTO) attributeMap.get(attributeName);
      attributeBlob.setValue(value);
      setModified(true);
    }
  }

  /**
   * Copies over properties from the given componentDTO.
   * 
   * @param componentDTOIF
   */
  public void copyProperties(ComponentDTOIF componentDTOIF)
  {
    super.copyProperties(componentDTOIF);

    if (componentDTOIF instanceof MutableDTO)
    {
      MutableDTO mutableDTO = (MutableDTO) componentDTOIF;
      this.setNewInstance(mutableDTO.isNewInstance());
      this.setWritable(mutableDTO.isWritable());
      this.setModified(mutableDTO.isModified());
    }
  }

  public void addMultiItem(String attributeName, String id)
  {
    if (attributeMap.containsKey(attributeName))
    {
      AttributeMultiReferenceDTO reference = (AttributeMultiReferenceDTO) attributeMap.get(attributeName);
      reference.addItem(id);
      setModified(true);
    }
  }

  public void clearMultiItems(String attributeName)
  {
    if (attributeMap.containsKey(attributeName))
    {
      AttributeMultiReferenceDTO reference = (AttributeMultiReferenceDTO) attributeMap.get(attributeName);
      reference.clear();
      setModified(true);
    }
  }

  public void removeMultiItem(String enumerationName, String id)
  {
    if (attributeMap.containsKey(enumerationName))
    {
      AttributeMultiReferenceDTO reference = (AttributeMultiReferenceDTO) attributeMap.get(enumerationName);
      reference.removeItem(id);
      setModified(true);
    }
  }

}
