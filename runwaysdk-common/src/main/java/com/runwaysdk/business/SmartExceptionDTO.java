/**
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved.
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
package com.runwaysdk.business;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.runwaysdk.RunwayExceptionIF;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.constants.SmartExceptionDTOInfo;
import com.runwaysdk.system.metadata.MdExceptionDTO;
import com.runwaysdk.system.metadata.MdLocalizableMessageDTO;
import com.runwaysdk.transport.attributes.AttributeCharacterDTO;
import com.runwaysdk.transport.attributes.AttributeDTO;
import com.runwaysdk.transport.attributes.AttributeDecDTO;
import com.runwaysdk.transport.attributes.AttributeEnumerationDTO;
import com.runwaysdk.transport.attributes.AttributeHashDTO;
import com.runwaysdk.transport.attributes.AttributeMultiReferenceDTO;
import com.runwaysdk.transport.attributes.AttributeNumberDTO;
import com.runwaysdk.transport.attributes.AttributeReferenceDTO;
import com.runwaysdk.transport.attributes.AttributeStructDTO;
import com.runwaysdk.transport.attributes.AttributeSymmetricDTO;
import com.runwaysdk.transport.attributes.AttributeTermDTO;
import com.runwaysdk.transport.metadata.TypeMd;

public class SmartExceptionDTO extends RuntimeException implements RunwayExceptionIF, SmartExceptionDTOIF, Serializable
{
  public static final String CLASS            = SmartExceptionDTOInfo.CLASS;

  /**
   *
   */
  private static final long  serialVersionUID = 7548587761939312052L;

  private ExceptionDTO       exceptionDTO     = null;

  private Locale             locale           = null;

  /**
   * This constructor is called client side.
   * 
   * @param clientRequest
   * @param locale
   */
  public SmartExceptionDTO(ClientRequestIF clientRequest, Locale locale)
  {
    this.exceptionDTO = new ExceptionDTO(clientRequest, this.getDeclaredType());
    this.locale = locale;

    SmartExceptionDTO dto = (SmartExceptionDTO) clientRequest.newGenericException(this.getDeclaredType());
    // Not bothering to clone the map, since the source dto is gargage collected
    // anyway.
    this.copyProperties(dto.exceptionDTO, dto.exceptionDTO.attributeMap);
  }

  /**
   * This constructor is called client side.
   * 
   * @param clientRequest
   * @param locale
   * @param developerMessage
   */
  public SmartExceptionDTO(ClientRequestIF clientRequest, Locale locale, String developerMessage)
  {
    super(developerMessage);
    this.exceptionDTO = new ExceptionDTO(clientRequest, this.getDeclaredType());
    this.locale = locale;

    // only get default values if a subclass of BusinessDTO is requested.
    SmartExceptionDTO dto = (SmartExceptionDTO) clientRequest.newGenericException(this.getDeclaredType());
    // Not bothering to clone the map, since the source dto is gargage collected
    // anyway.
    this.copyProperties(dto.exceptionDTO, dto.exceptionDTO.attributeMap);
    this.exceptionDTO.setDeveloperMessage(developerMessage);
  }

  public SmartExceptionDTO(ClientRequestIF clientRequest, Locale locale, Throwable cause)
  {
    super(cause);

    this.exceptionDTO = new ExceptionDTO(clientRequest, this.getDeclaredType());
    this.locale = locale;

    SmartExceptionDTO dto = (SmartExceptionDTO) clientRequest.newGenericException(this.getDeclaredType());
    // Not bothering to clone the map, since the source dto is gargage collected
    // anyway.
    this.copyProperties(dto.exceptionDTO, dto.exceptionDTO.attributeMap);
  }

  public SmartExceptionDTO(ClientRequestIF clientRequest, Locale locale, String developerMessage, Throwable cause)
  {
    super(developerMessage, cause);

    this.exceptionDTO = new ExceptionDTO(clientRequest, this.getDeclaredType());
    this.locale = locale;

    SmartExceptionDTO dto = (SmartExceptionDTO) clientRequest.newGenericException(this.getDeclaredType());
    // Not bothering to clone the map, since the source dto is gargage collected
    // anyway.
    this.copyProperties(dto.exceptionDTO, dto.exceptionDTO.attributeMap);
    this.exceptionDTO.setDeveloperMessage(developerMessage);
  }

  protected SmartExceptionDTO(ExceptionDTO exceptionDTO)
  {
    this.exceptionDTO = exceptionDTO;
  }

  protected SmartExceptionDTO(ClientRequestIF clientRequest)
  {
    this.exceptionDTO = new ExceptionDTO(clientRequest, this.getDeclaredType());
  }

  protected SmartExceptionDTO(ClientRequestIF clientRequest, String devMessage)
  {
    super(devMessage);

    this.exceptionDTO = new ExceptionDTO(clientRequest, this.getDeclaredType());
  }

  protected SmartExceptionDTO(ClientRequestIF clientRequest, Throwable cause)
  {
    super(cause);

    this.exceptionDTO = new ExceptionDTO(clientRequest, this.getDeclaredType());
  }

  protected SmartExceptionDTO(ClientRequestIF clientRequest, String msg, Throwable cause)
  {
    super(msg, cause);

    this.exceptionDTO = new ExceptionDTO(clientRequest, this.getDeclaredType());
  }

  /**
   * Returns the inner generic ExceptionDTO which is a container for all of the
   * attributes for this exception.
   * 
   * @param smartExceptionDTO
   * @return inner generic ExceptionDTO which is a container for all of the
   *         attributes for this exception
   */
  public static ExceptionDTO getExceptionDTO(SmartExceptionDTO smartExceptionDTO)
  {
    return smartExceptionDTO.exceptionDTO;
  }

  /**
   * Returns the id of this object.
   * 
   * @return A unique id.
   */
  public String getId()
  {
    return this.exceptionDTO.getId();
  }

  /**
   * Returns the type of this object.
   * 
   * @return A String representing the type.
   */
  public String getType()
  {
    return this.exceptionDTO.getType();
  }

  /**
   * Sets the clientRequest.
   * 
   * @param clientRequest
   *          .
   */
  public void setClientRequest(ClientRequestIF clientRequest)
  {
    this.exceptionDTO.setClientRequest(clientRequest);
  }

  /**
   * Returns the session Id that created this object.
   * 
   * @return session Id that created this object.
   */
  public String getSessionId()
  {
    return this.exceptionDTO.getRequest().getSessionId();
  }

  /**
   * Sets the display metadata.
   * 
   * @param typeMd
   */
  public void setMd(TypeMd typeMd)
  {
    this.exceptionDTO.setMd(typeMd);
  }

  /**
   * Gets the display metadata for this type.
   * 
   * @return display metadata for this type.
   */
  public TypeMd getMd()
  {
    return this.exceptionDTO.getMd();
  }

  /**
   * Returns the ClientRequest use for this DTO's construction.
   * 
   * @return ClientRequest use for this DTO's construction.
   */
  public ClientRequestIF getRequest()
  {
    return this.exceptionDTO.getRequest();
  }

  /**
   * Checks if this entity is readable.
   * 
   * @return true if the entity is readable, false otherwise.
   */
  public boolean isReadable()
  {
    return this.exceptionDTO.isReadable();
  }

  /**
   * Checks if an attribute can be read or not.
   * 
   * @param attributeName
   * @return true if the value can be read, false otherwise.
   */
  public boolean isReadable(String attributeName)
  {
    return this.exceptionDTO.isReadable(attributeName);
  }

  /**
   * Returns all the names of every attribute in this BusinessDTO instance.
   * 
   * @return String[] Array of attribute names.
   */
  public List<String> getAttributeNames()
  {
    return this.exceptionDTO.getAttributeNames();
  }

  /**
   * Checks if this EntityDTO has a specific attribute.
   * 
   * @param name
   *          The name of the attribute.
   * @return true if the attribute exists, false otherwise.
   */
  public boolean hasAttribute(String attributeName)
  {
    return this.exceptionDTO.hasAttribute(attributeName);
  }

  /**
   * Returns the type of the specified attribute.
   * 
   * @param attributeName
   * @return The type of the attribute.
   */
  public String getAttributeType(String attributeName)
  {
    return this.exceptionDTO.getAttributeType(attributeName);
  }

  /**
   * Returns the value of an attribute.
   * 
   * @param attributeName
   * @return The value of the specified attribute.
   */
  public String getValue(String attributeName)
  {
    return this.exceptionDTO.getValue(attributeName);
  }

  /**
   * Returns the value of an attribute.
   * 
   * @param attributeName
   * @return The value of the specified attribute.
   */
  public Object getObjectValue(String attributeName)
  {
    return this.exceptionDTO.getObjectValue(attributeName);
  }

  /**
   * Returns the names of the enumeration items for the given attribute.
   * 
   * @param attributeName
   * @return names of the enumeration items for the given attribute.
   */
  public List<String> getEnumNames(String attributeName)
  {
    return this.exceptionDTO.getEnumNames(attributeName);
  }

  /**
   * If this is called on the front end, it will return all BusinessDTOs for
   * which this object has an internal mapping. If this object only has the id
   * of an enumItem, it will fetch the generic BusinessDTO for it via the
   * clientRequest. If this is called on the back-end, it will only return the
   * BusinessDTOs that it has references to. If it only has an id for an
   * enumItem, it does not return the BusinessDTO for it. This is OK, as on the
   * back-end, the object should contain all BusinessDTOs. On the front-end, the
   * method addEnumItem(String) may have been called, but the object does not
   * have a businessDTO for that item.
   * 
   * @param attributeName
   *          The name of the attribute to retrieve all items.
   * @returns A List of strings where each string is an item id. An emptry list
   *          is returned if no items exist for the attribute.
   */
  public List<? extends BusinessDTO> getEnumValues(String attributeName)
  {
    return this.exceptionDTO.getEnumValues(attributeName);
  }

  /**
   * Returns a binary value for an attribute on this ocmponent with the given
   * name.
   * 
   * @param attributeName
   * @return binary value
   */
  public byte[] getBlob(String attributeName)
  {
    return this.exceptionDTO.getBlob(attributeName);
  }

  protected String getDeclaredType()
  {
    return ExceptionDTO.class.getName();
  }

  /**
   * Returns the locale of this exception, or null if this exception was thrown
   * from the server.
   * 
   * @return locale of this exception, or null if this exception was thrown from
   *         the server.
   */
  protected Locale getLocale()
  {
    return this.locale;
  }

  /**
   * Returns the ExceptionDTO that contains all of the attributes for this
   * exception.
   * 
   * @return ExceptionDTO that contains all of the attributes for this
   *         exception.
   */
  protected ExceptionDTO getExceptionDTO()
  {
    return this.exceptionDTO;
  }

  /**
   * Overrides java.lang.Throwable#getMessage() to retrieve the localized
   * message from the exceptionDTO, instead of from a class variable.
   */
  public String getMessage()
  {
    // Throw client side
    if (this.getLocale() != null)
    {
      String id = this.getMd().getId();
      MdExceptionDTO mdExceptionDTO = MdExceptionDTO.get(getRequest(), id);
      MdLocalizableMessageDTO messages = mdExceptionDTO.getMessage();
      return messages.getValue(this.getLocale());
    }
    else
    {
      return exceptionDTO.getLocalizedMessage();
    }
  }

  /**
   * Overrides java.lang.Throwable#getMessage() to retrieve the developer
   * message from the exceptionDTO, instead of from a class variable.
   */
  public String getDeveloperMessage()
  {
    return exceptionDTO.getDeveloperMessage();
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
    exceptionDTO.addEnumItem(attributeName, enumName);
  }

  /**
   * Clears the enumeration items on an attribute enumeration.
   * 
   * @param attributeName
   */
  public void clearEnum(String attributeName)
  {
    exceptionDTO.clearEnum(attributeName);
  }

  /**
   * Copies over properties from the given componentDTO.
   * 
   * @param componentDTOIF
   */
  public void copyProperties(ComponentDTOIF componentDTOIF)
  {
    exceptionDTO.copyProperties(componentDTOIF);
  }

  /**
   * Copies over properties from the given componentDTO.
   * 
   * @param componentDTOIF
   * @param clonedAttributeMap
   *          cloned attribute map from componentDTO.
   */
  public void copyProperties(ComponentDTOIF componentDTOIF, Map<String, AttributeDTO> clonedAttributeMap)
  {
    exceptionDTO.copyProperties(componentDTOIF, clonedAttributeMap);
  }

  /**
   * Checks if this entity has been modified.
   * 
   * @return true if the entity has been modified, false otherwise.
   */
  public boolean isModified()
  {
    return exceptionDTO.isModified();
  }

  /**
   * Checks if an attribute has been modified or not.
   * 
   * @param attributeName
   * @return true if the attribute has been modified, false otherwise.
   */
  public boolean isModified(String attributeName)
  {
    return exceptionDTO.isModified(attributeName);
  }

  /**
   * Checks if this BusinessDTO instance is a new instance or not.
   * 
   * @return true if it is a new instance, false otherwise.
   */
  public boolean isNewInstance()
  {
    return exceptionDTO.isNewInstance();
  }

  /**
   * Checks if this entity is writable.
   * 
   * @return true if the entity is writable, false otherwise.
   */
  public boolean isWritable()
  {
    return exceptionDTO.isWritable();
  }

  /**
   * Checks if an attribute can be written or not.
   * 
   * @param attributeName
   * @return true if the value can be edited, false otherwise.
   */
  public boolean isWritable(String attributeName)
  {
    return exceptionDTO.isWritable(attributeName);
  }

  /**
   * Sets the writable value on this entity.
   * 
   * @param writable
   */
  protected void setWritable(boolean writable)
  {
    exceptionDTO.setWritable(writable);
  }

  /**
   * 
   * @param enumerationName
   * @param enumName
   */
  public void removeEnumItem(String enumerationName, String enumName)
  {
    exceptionDTO.removeEnumItem(enumerationName, enumName);
  }

  public void setBlob(String attributeName, byte[] value)
  {
    exceptionDTO.setBlob(attributeName, value);
  }

  /**
   * Sets the flag denoting whether or not this entity (as a whole) has been
   * modified.
   * 
   * @param modified
   */
  public void setModified(boolean modified)
  {
    exceptionDTO.setModified(modified);
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
    exceptionDTO.setModified(attributeName, modified);
  }

  /**
   * Sets the flag denoting if this a new instance or not. Boolean true means
   * it's a new instance, false does not.
   * 
   * @param isNew
   */
  protected void setNewInstance(boolean isNew)
  {
    exceptionDTO.setNewInstance(isNew);
  }

  /**
   * Sets the value of a given attribute.
   * 
   * @param attributeName
   * @param value
   */
  public void setValue(String attributeName, String value)
  {
    exceptionDTO.setValue(attributeName, value);
  }

  /**
   * Sets the value of a given attribute.
   * 
   * @param attributeName
   * @param value
   */
  public void setValue(String attributeName, Object value)
  {
    exceptionDTO.setValue(attributeName, value);
  }

  protected AttributeDTO getAttributeDTO(String attributeName)
  {
    return exceptionDTO.getAttributeDTO(attributeName);
  }

  /**
   * Returns a HashDTO representing the attribute with the specified name.
   * 
   * @param hashName
   * @return
   */
  protected AttributeHashDTO getAttributeHashDTO(String hashName)
  {
    return exceptionDTO.getAttributeHashDTO(hashName);
  }

  /**
   * Returns an EnumerationDTO representing the attribute with the specified
   * name.
   * 
   * @param enumName
   * @return
   */
  protected AttributeEnumerationDTO getAttributeEnumerationDTO(String enumName)
  {
    return exceptionDTO.getAttributeEnumerationDTO(enumName);
  }

  protected AttributeMultiReferenceDTO getAttributeMultiReferenceDTO(String enumName)
  {
    return exceptionDTO.getAttributeMultiReferenceDTO(enumName);
  }

  /**
   *
   */
  protected AttributeNumberDTO getAttributeNumberDTO(String attributeName)
  {
    return exceptionDTO.getAttributeNumberDTO(attributeName);
  }

  /**
   *
   */
  protected AttributeDecDTO getAttributeDecDTO(String attributeName)
  {
    return exceptionDTO.getAttributeDecDTO(attributeName);
  }

  /**
   *
   */
  protected AttributeSymmetricDTO getAttributeSymmetricDTO(String attributeName)
  {
    return (AttributeSymmetricDTO) getAttributeDTO(attributeName);
  }

  /**
   *
   */
  protected AttributeCharacterDTO getAttributeCharacterDTO(String attributeName)
  {
    return (AttributeCharacterDTO) getAttributeDTO(attributeName);
  }

  /**
   *
   */
  protected AttributeStructDTO getAttributeStructDTO(String attributeName)
  {
    return (AttributeStructDTO) getAttributeDTO(attributeName);
  }

  /**
   *
   */
  protected AttributeReferenceDTO getAttributeReferenceDTO(String attributeName)
  {
    return (AttributeReferenceDTO) getAttributeDTO(attributeName);
  }

  /**
   *
   */
  protected AttributeTermDTO getAttributeTermDTO(String attributeName)
  {
    return (AttributeTermDTO) getAttributeDTO(attributeName);
  }
}
