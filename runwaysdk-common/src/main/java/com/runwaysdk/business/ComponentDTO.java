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
package com.runwaysdk.business;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.runwaysdk.CommonExceptionProcessor;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.ComponentInfo;
import com.runwaysdk.constants.ExceptionConstants;
import com.runwaysdk.generation.loader.LoaderDecorator;
import com.runwaysdk.transport.attributes.AttributeBlobDTO;
import com.runwaysdk.transport.attributes.AttributeBooleanDTO;
import com.runwaysdk.transport.attributes.AttributeCharacterDTO;
import com.runwaysdk.transport.attributes.AttributeDTO;
import com.runwaysdk.transport.attributes.AttributeDTOFactory;
import com.runwaysdk.transport.attributes.AttributeDecDTO;
import com.runwaysdk.transport.attributes.AttributeEnumerationDTO;
import com.runwaysdk.transport.attributes.AttributeHashDTO;
import com.runwaysdk.transport.attributes.AttributeMultiReferenceDTO;
import com.runwaysdk.transport.attributes.AttributeMultiTermDTO;
import com.runwaysdk.transport.attributes.AttributeNumberDTO;
import com.runwaysdk.transport.attributes.AttributeReferenceDTO;
import com.runwaysdk.transport.attributes.AttributeStructDTO;
import com.runwaysdk.transport.attributes.AttributeSymmetricDTO;
import com.runwaysdk.transport.attributes.AttributeTermDTO;
import com.runwaysdk.transport.metadata.AttributeCharacterMdDTO;
import com.runwaysdk.transport.metadata.AttributeMdDTO;
import com.runwaysdk.transport.metadata.TypeMd;

public abstract class ComponentDTO implements ComponentDTOIF, Cloneable, Serializable
{
  /**
   * 
   */
  private static final long           serialVersionUID = 7049208553855713646L;

  public static final String          CLASS            = "com.runwaysdk.business.ComponentDTO";

  /**
   * Flag denoting if this DTO as a whole can be read.
   */
  private boolean                     readable         = false;

  /**
   * The oid of this ComponentDTO instance.
   */
  private String                      oid               = "";

  /**
   * The type of this BusinessDTO instance.
   */
  private String                      type;

  /**
   * A two-dimensional HashMap to store attributes where the key is the
   * attribute name and the value is a HashMap to store data about that
   * attribute.
   */
  protected Map<String, AttributeDTO> attributeMap;

  /**
   * The type object to hold type metadata about this EntityDTO.
   */
  private TypeMd                      typeMd;

  /**
   *
   */
  private ClientRequestIF             clientRequest;

  /**
   * The toString() of the Entity (copied from the business layer)
   */
  private String                      toString         = "";

  /**
   * Constructor used when the object is instantiated on the front-end.
   */
  protected ComponentDTO(ClientRequestIF clientRequest, String type)
  {
    this.clientRequest = clientRequest;
    this.type = type;
    this.attributeMap = new HashMap<String, AttributeDTO>();
  }

  /**
   * Constructor used when the object is instantiated on the front-end.
   * Delegates the type information to the abstract method getDeclaredType.
   */
  protected ComponentDTO(ClientRequestIF clientRequest)
  {
    this.clientRequest = clientRequest;
    this.type = this.getDeclaredType();
  }

  /**
   * Constructor used when the object is instantiated on the front-end or
   * back-end. If the clientRequest is null, then it is instantiated on the
   * front-end, otherwise on the back-end.
   */
  protected ComponentDTO(ClientRequestIF clientRequest, String type, Map<String, AttributeDTO> attributeMap)
  {
    this.init(clientRequest, type, attributeMap);
  }

  /**
   * Constructor used when the object is instantiated on the front-end or
   * back-end. If the clientRequest is null, then it is instantiated on the
   * front-end, otherwise on the back-end. Delegates the type information to the
   * abstract method getDeclaredType.
   */
  protected ComponentDTO(ClientRequestIF clientRequest, Map<String, AttributeDTO> attributeMap)
  {
    this.init(clientRequest, this.getDeclaredType(), attributeMap);
  }

  /**
   * Establishes invariants
   * 
   * @param clientRequest
   * @param type
   * @param attributeMap
   */
  protected void init(ClientRequestIF clientRequest, String type, Map<String, AttributeDTO> attributeMap)
  {
    if (attributeMap.get(ComponentInfo.ID) == null)
    {
      this.oid = "";
    }
    else
    {
      this.oid = attributeMap.get(ComponentInfo.ID).getValue();
    }

    this.clientRequest = clientRequest;
    this.type = type;
    this.attributeMap = attributeMap;

    for (AttributeDTO attributeDTO : this.attributeMap.values())
    {
      attributeDTO.setContainingDTO(this);
    }
  }

  /**
   * Copies over properties from the given componentDTO.
   * 
   * @param componentDTOIF
   */
  public void copyProperties(ComponentDTOIF componentDTOIF)
  {
    this.type = componentDTOIF.getType();
    this.setReadable(componentDTOIF.isReadable());
    this.setToString(componentDTOIF.toString());

    // copy the type metadata
    this.typeMd = componentDTOIF.getMd().clone();
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
    this.copyProperties(componentDTOIF);
    this.attributeMap = clonedAttributeMap;

    for (AttributeDTO attributeDTO : this.attributeMap.values())
    {
      attributeDTO.setContainingDTO(this);
    }

    if (attributeMap.get(ComponentInfo.ID) == null)
    {
      this.oid = "";
    }
    else
    {
      this.oid = attributeMap.get(ComponentInfo.ID).getValue();
    }

  }

  /**
   * Sets the attributeDTOs on the given ComponentDTO to reference the metadata
   * on the given attributes.
   * 
   * @param componentDTO
   * @param foreignAttributeDTOMap
   */
  protected void setDefinedAttributeMetadata(Map<String, AttributeDTO> foreginAttributeDTOMap)
  {
    for (String attributeName : foreginAttributeDTOMap.keySet())
    {
      AttributeDTO myAttributeDTO = this.getAttributeDTO(attributeName);
      AttributeDTO foreignAttributeDTO = foreginAttributeDTOMap.get(attributeName);

      AttributeDTOFactory.setAttributeMdDTO(myAttributeDTO, foreignAttributeDTO.getAttributeMdDTO());
    }
  }

  /**
   * Returns the oid of this object.
   * 
   * @return A unique oid.
   */
  public String getOid()
  {
    return oid;
  }

  public AttributeCharacterMdDTO getOidMd()
  {
    return this.getAttributeCharacterDTO(ComponentInfo.ID).getAttributeMdDTO();
  }

  /**
   * Returns the type of this object.
   * 
   * @return A String representing the type.
   */
  public String getType()
  {
    return type;
  }

  public AttributeCharacterMdDTO getTypeMd()
  {
    return this.getAttributeCharacterDTO(ComponentInfo.TYPE).getAttributeMdDTO();
  }

  /**
   * Returns true if the TypeMd is set.
   * 
   * @return
   */
  protected boolean isTypeMdSet()
  {
    return typeMd != null;
  }

  /**
   * Gets the metadata for this type
   * 
   * @return
   */
  public TypeMd getMd()
  {
    if (!isTypeMdSet())
    {
      return new TypeMd();
    }
    else
    {
      return typeMd;
    }
  }

  /**
   * Sets the display metadata.
   * 
   * @param typeMd
   */
  public void setMd(TypeMd typeMd)
  {
    this.typeMd = typeMd;
  }

  /**
   * Returns the ClientRequest use for this DTO's construction.
   * 
   * 
   * @return
   */
  public ClientRequestIF getRequest()
  {
    return clientRequest;
  }

  /**
   * Sets the clientRequest.
   * 
   * @param clientRequest
   *          .
   */
  public void setClientRequest(ClientRequestIF clientRequest)
  {
    this.clientRequest = clientRequest;
  }

  /**
   * Checks if this entity is readable.
   * 
   * @return true if the entity is readable, false otherwise.
   */
  public boolean isReadable()
  {
    return readable;
  }

  /**
   * Checks if an attribute can be read or not.
   * 
   * @param attributeName
   * @return true if the value can be read, false otherwise.
   */
  public boolean isReadable(String attributeName)
  {
    if (attributeMap.containsKey(attributeName))
    {
      AttributeDTO attribute = attributeMap.get(attributeName);
      return attribute.isReadable();
    }
    else
    {
      return ComponentDTOIF.READABLE_DEFAULT;
    }
  }

  /**
   * Sets the readable value on this entity.
   * 
   * @param readable
   */
  protected void setReadable(boolean readable)
  {
    this.readable = readable;
  }

  /**
   * Returns all the names of every attribute in this BusinessDTO instance.
   * 
   * @return String[] Array of attribute names.
   */
  public List<String> getAttributeNames()
  {
    List<String> names = new LinkedList<String>();
    String[] namesArray = new String[attributeMap.size()];
    attributeMap.keySet().toArray(namesArray);

    for (String nameItem : namesArray)
    {
      names.add(nameItem);
    }

    Collections.sort(names);

    return names;
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
    return attributeMap.containsKey(attributeName);
  }

  /**
   * Returns the type of the specified attribute.
   * 
   * @param attributeName
   * @return The type of the attribute.
   */
  public String getAttributeType(String attributeName)
  {
    if (attributeMap.containsKey(attributeName))
    {
      AttributeDTO attribute = attributeMap.get(attributeName);
      return attribute.getType();
    }
    else
    {
      return ComponentDTOIF.EMPTY_VALUE;
    }
  }

  protected AttributeDTO getAttributeDTO(String attributeName)
  {
    return attributeMap.get(attributeName);
  }

  /**
   * Returns a HashDTO representing the attribute with the specified name.
   * 
   * @param hashName
   * @return
   */
  protected AttributeHashDTO getAttributeHashDTO(String hashName)
  {
    return (AttributeHashDTO) attributeMap.get(hashName);
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
    return (AttributeEnumerationDTO) getAttributeDTO(enumName);
  }

  /**
   * Returns an MultiReferenceDTO representing the attribute with the specified
   * name.
   * 
   * @param attributeName
   * @return
   */
  protected AttributeMultiReferenceDTO getAttributeMultiReferenceDTO(String attributeName)
  {
    return (AttributeMultiReferenceDTO) getAttributeDTO(attributeName);
  }

  /**
   * Returns an MultiTermDTO representing the attribute with the specified name.
   * 
   * @param attributeName
   * @return
   */
  protected AttributeMultiTermDTO getAttributeMultiTermDTO(String attributeName)
  {
    return (AttributeMultiTermDTO) getAttributeDTO(attributeName);
  }

  /**
   *
   */
  protected AttributeNumberDTO getAttributeNumberDTO(String attributeName)
  {
    return (AttributeNumberDTO) getAttributeDTO(attributeName);
  }

  /**
   *
   */
  protected AttributeBooleanDTO getAttributeBooleanDTO(String attributeName)
  {
    return (AttributeBooleanDTO) getAttributeDTO(attributeName);
  }

  /**
   *
   */
  protected AttributeDecDTO getAttributeDecDTO(String attributeName)
  {
    return (AttributeDecDTO) getAttributeDTO(attributeName);
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

  /**
   * Returns the value of an attribute.
   * 
   * @param attributeName
   * @return The value of the specified attribute.
   */
  public String getValue(String attributeName)
  {
    if (attributeMap.containsKey(attributeName))
    {
      AttributeDTO attribute = attributeMap.get(attributeName);
      this.checkAttributeReadPermission(attribute);
      return attribute.getValue();
    }
    else
    {
      return ComponentDTOIF.EMPTY_VALUE;
    }
  }

  public AttributeMdDTO getAttributeMd(String attributeName)
  {
    return this.getAttributeDTO(attributeName).getAttributeMdDTO();
  }

  /**
   * Returns the value of an attribute.
   * 
   * @param attributeName
   * @return The value of the specified attribute.
   */
  public Object getObjectValue(String attributeName)
  {
    if (attributeMap.containsKey(attributeName))
    {
      AttributeDTO attribute = attributeMap.get(attributeName);
      this.checkAttributeReadPermission(attribute);
      return attribute.getObjectValue();
    }
    else
    {
      return ComponentDTOIF.EMPTY_VALUE;
    }
  }

  /**
   * Returns the names of the enumeration items for the given attribute.
   * 
   * @param attributeName
   * @return names of the enumeration items for the given attribute.
   */
  public List<String> getEnumNames(String attributeName)
  {
    if (attributeMap.containsKey(attributeName))
    {
      AttributeEnumerationDTO enumeration = (AttributeEnumerationDTO) attributeMap.get(attributeName);
      this.checkAttributeReadPermission(enumeration);
      return enumeration.getEnumNames();
    }
    else
    {
      return new LinkedList<String>();
    }
  }

  public List<String> getMultiItems(String attributeName)
  {
    if (attributeMap.containsKey(attributeName))
    {
      AttributeMultiReferenceDTO enumeration = (AttributeMultiReferenceDTO) attributeMap.get(attributeName);
      this.checkAttributeReadPermission(enumeration);
      return enumeration.getItemIds();
    }
    else
    {
      return new LinkedList<String>();
    }
  }

  /**
   * If this is called on the front end, it will return all BusinessDTOs for
   * which this object has an internal mapping. If this object only has the oid
   * of an enumItem, it will fetch the generic BusinessDTO for it via the
   * clientRequest. If this is called on the back-end, it will only return the
   * BusinessDTOs that it has references to. If it only has an oid for an
   * enumItem, it does not return the BusinessDTO for it. This is OK, as on the
   * back-end, the object should contain all BusinessDTOs. On the front-end, the
   * method addEnumItem(String) may have been called, but the object does not
   * have a businessDTO for that item.
   * 
   * @param attributeName
   *          The name of the attribute to retrieve all items.
   * @returns A List of strings where each string is an item oid. An emptry list
   *          is returned if no items exist for the attribute.
   */
  public List<? extends BusinessDTO> getEnumValues(String attributeName)
  {
    if (attributeMap.containsKey(attributeName))
    {
      AttributeEnumerationDTO enumeration = (AttributeEnumerationDTO) attributeMap.get(attributeName);
      this.checkAttributeReadPermission(enumeration);
      return enumeration.getEnumValues();
    }
    else
    {
      return new LinkedList<BusinessDTO>();
    }
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
    if (attributeMap.containsKey(attributeName))
    {
      AttributeBlobDTO attributeBlob = (AttributeBlobDTO) attributeMap.get(attributeName);
      this.checkAttributeReadPermission(attributeBlob);
      return attributeBlob.getBlob();
    }
    else
    {
      return ComponentDTOIF.EMPTY_VALUE.getBytes();
    }
  }

  /**
   * Throws {@link ClientReadAttributePermissionException} if the user does not
   * have read exception on the given attribute.
   * 
   * @param attribute
   */
  protected void checkAttributeReadPermission(AttributeDTO attribute)
  {
    if (this.clientRequest != null && !attribute.isReadable())
    {
      String attributeDisplayLabel = attribute.getAttributeMdDTO().getDisplayLabel();
      String classDisplayLabel = this.typeMd.getDisplayLabel();
      String errMsg = "Invalid read permission for attribute [" + attribute.getName() + "] on class [" + this.getType() + "]";

      Locale[] locales = this.clientRequest.getLocales();
      Locale locale = ( locales.length > 0 ? locales[0] : CommonProperties.getDefaultLocale() );

      Class<?> clientReadExceptionClass = LoaderDecorator.load(ExceptionConstants.AttributeReadPermissionExceptionDTO.getExceptionClass());

      RuntimeException clientReadAttributePermissionException;

      try
      {

        clientReadAttributePermissionException = (RuntimeException) clientReadExceptionClass.getConstructor(String.class, Locale.class, String.class, String.class).newInstance(errMsg, locale, attributeDisplayLabel, classDisplayLabel);
      }
      catch (Exception e)
      {
        // This is one of the few times in which it is acceptable to throw a
        // runtime
        // exception in Runway. This indicates a problem with the common
        // exception mechanism.
        e.printStackTrace();
        throw new RuntimeException(e);
      }

      throw clientReadAttributePermissionException;
    }
  }

  public ComponentDTO clone()
  {
    ComponentDTO componentDTO = null;
    try
    {
      componentDTO = (ComponentDTO) super.clone();
    }
    catch (CloneNotSupportedException ex)
    {
      CommonExceptionProcessor.processException(ExceptionConstants.ProgrammingErrorException.getExceptionClass(), ex.getMessage(), ex);
    }

    TypeMd newTypeMd = this.getMd().clone();
    componentDTO.setMd(newTypeMd);

    Map<String, AttributeDTO> _attributeMap = new HashMap<String, AttributeDTO>();

    // Clone all of the attributes
    for (String attributeName : this.getAttributeNames())
    {
      _attributeMap.put(attributeName, this.getAttributeDTO(attributeName).clone());
    }
    componentDTO.attributeMap = _attributeMap;

    return componentDTO;
  }

  /**
   * Sets the toString value of this EntityDTO. Note that this value is the
   * value of the Entity.toString() method in the Business Layer.
   * 
   * @param toString
   */
  protected void setToString(String toString)
  {
    this.toString = toString;
  }

  @Override
  public String toString()
  {
    return toString;
  }

  protected abstract String getDeclaredType();

  @Override
  public boolean equals(Object obj)
  {
    if (! ( obj instanceof ComponentDTOIF ))
    {
      return false;
    }

    ComponentDTOIF comp = (ComponentDTOIF) obj;
    return this.getOid().equals(comp.getOid());
  }
}
