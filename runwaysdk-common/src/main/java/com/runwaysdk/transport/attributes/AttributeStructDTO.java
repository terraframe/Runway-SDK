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
package com.runwaysdk.transport.attributes;

import java.util.List;

import com.runwaysdk.business.BusinessDTO;
import com.runwaysdk.business.ComponentDTOFacade;
import com.runwaysdk.business.StructDTO;
import com.runwaysdk.constants.MdAttributeStructInfo;
import com.runwaysdk.transport.metadata.AttributeStructMdDTO;
import com.runwaysdk.transport.metadata.CommonAttributeFacade;

/**
 * Describes an attribute struct.
 */
public class AttributeStructDTO extends AttributeDTO
{
  /**
   * The StructDTO that this attribute struct wraps.
   */
  private StructDTO         structDTO;

  /**
   * Auto-generated ID
   */
  private static final long serialVersionUID = 3218179807583523937L;

  /**
   * Constructor
   * 
   * @param name
   * @param value
   * @param readable
   * @param writable
   * @param modified
   */
  protected AttributeStructDTO(String name, String value, boolean readable, boolean writable, boolean modified)
  {
    super(name, value, readable, writable, modified);

    structDTO = null;
  }

  /**
   * Sets the StructDTO that this attribute wraps.
   * 
   * @param structDTO
   */
  public void setStructDTO(StructDTO structDTO)
  {
    this.structDTO = structDTO;
  }

  /**
   * Returns the StructDTO this attribute wraps.
   * 
   * @return
   */
  public StructDTO getStructDTO()
  {
    return structDTO;
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
    structDTO.addEnumItem(attributeName, enumName);
  }

  /**
   * Removes an item from the specified enumeration.
   */
  public void removeEnumItem(String enumerationName, String enumName)
  {
    structDTO.removeEnumItem(enumerationName, enumName);
  }

  /**
   * Clears the enumeration items on an attribute enumeration.
   * 
   * @param attributeName
   */
  public void clearEnum(String attributeName)
  {
    structDTO.clearEnum(attributeName);
  }

  public void setValue(String attributeName, String value)
  {
    structDTO.setValue(attributeName, value);
  }

  public String getValue(String attributeName)
  {
    return structDTO.getValue(attributeName);
  }

  public boolean isReadable(String attributeName)
  {
    return structDTO.isReadable(attributeName);
  }

  public boolean isWritable(String attributeName)
  {
    return structDTO.isWritable(attributeName);
  }

  public boolean isModified(String attributeName)
  {
    // delegate the modified check to the StructDTO this attribute struct wraps
    return structDTO.isModified(attributeName);
  }

  /**
   * Returns all the names of every attribute this struct defines.
   * 
   * @return String[] Array of attribute names.
   */
  public List<String> getAttributeNames()
  {
    return structDTO.getAttributeNames();
  }

  /**
   * Returns a list containing the string item ids in an enumeration attribute.
   * 
   * @param attributeName
   *          The name of the attribute to retrieve all items.
   * @returns A List of strings where each string is an item id. An emptry list
   *          is returned if no items exist for the attribute.
   */
  public List<? extends BusinessDTO> getEnumValues(String attributeName)
  {
    return structDTO.getEnumValues(attributeName);
  }

  /**
   * Returns the names of the enumeration items for the given attribute.
   * 
   * @param attributeName
   * @return names of the enumeration items for the given attribute.
   */
  public List<String> getEnumNames(String attributeName)
  {
    return structDTO.getEnumNames(attributeName);
  }

  /**
   * Checks if this stuct has a specific attribute.
   * 
   * @param name
   *          The name of the attribute.
   * @return true if the attribute exists, false otherwise.
   */
  public boolean hasAttribute(String attributeName)
  {
    return structDTO.hasAttribute(attributeName);
  }

  public String getAttributeType(String attributeName)
  {
    return structDTO.getAttributeType(attributeName);
  }

  public byte[] getBlob(String attributeName)
  {
    return structDTO.getBlob(attributeName);
  }

  public void setBlob(String attributeName, byte[] value)
  {
    structDTO.setBlob(attributeName, value);
  }

  /**
   * Sets if this AttributeStructDTO has been modified. Calling this method has
   * no effect since AttributeStructDTOs are always marked as modified (just
   * asin the Business Layer).
   * 
   * @param modified
   */
  public void setModified(boolean modified)
  {
    // balk this method because attribute structs are always marked as modified
  }

  /**
   * Returns the flag denoting if an attribute has been modified.
   * AttributeStructDTOs are always marked as modified (just as in the Business
   * Layer).
   * 
   * @return Always returns true
   */
  public boolean isModified()
  {
    return true;
  }

  /**
   * Returns a HashDTO representing the attribute with the specified name.
   * 
   * @param hashName
   * @return
   */
  public AttributeHashDTO getAttributeHashDTO(String hashName)
  {
    return (AttributeHashDTO) getAttributeDTO(hashName);
  }

  /**
   * Returns an EnumerationDTO representing the attribute with the specified
   * name.
   * 
   * @param enumName
   * @return
   */
  public AttributeEnumerationDTO getAttributeEnumerationDTO(String enumName)
  {
    return (AttributeEnumerationDTO) getAttributeDTO(enumName);
  }

  /**
   * Returns an MultiReferenceDTO representing the attribute with the specified
   * name.
   * 
   * @param attributeName
   * @return
   */
  public AttributeMultiReferenceDTO getAttributeMultiReferenceDTO(String attributeName)
  {
    return (AttributeMultiReferenceDTO) getAttributeDTO(attributeName);
  }

  @Override
  public String getType()
  {
    return MdAttributeStructInfo.CLASS;
  }

  public AttributeDTO getAttributeDTO(String attributeName)
  {
    return ComponentDTOFacade.getAttributeDTO(structDTO, attributeName);
  }

  /**
   * 
   */
  public AttributeNumberDTO getAttributeNumberDTO(String attributeName)
  {
    return (AttributeNumberDTO) getAttributeDTO(attributeName);
  }

  /**
   * 
   */
  public AttributeDecDTO getAttributeDecDTO(String attributeName)
  {
    return (AttributeDecDTO) getAttributeDTO(attributeName);
  }

  /**
   * 
   */
  public AttributeSymmetricDTO getAttributeSymmetricDTO(String attributeName)
  {
    return (AttributeSymmetricDTO) getAttributeDTO(attributeName);
  }

  /**
   * 
   */
  public AttributeCharacterDTO getAttributeCharacterDTO(String attributeName)
  {
    return (AttributeCharacterDTO) getAttributeDTO(attributeName);
  }

  /**
   * Returns a shallow clone of this attribute struct.
   */
  @Override
  public AttributeStructDTO clone()
  {
    // clone attribute values and metadata
    AttributeStructDTO clone = (AttributeStructDTO) super.clone();

    CommonAttributeFacade.setStructMetadata(this.getAttributeMdDTO(), clone.getAttributeMdDTO());

    clone.setStructDTO(this.getStructDTO());

    return (AttributeStructDTO) clone;
  }

  @Override
  public AttributeStructMdDTO getAttributeMdDTO()
  {
    return (AttributeStructMdDTO) super.getAttributeMdDTO();
  }
}
