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

import java.util.List;
import java.util.Map;

import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.transport.attributes.AttributeDTO;
import com.runwaysdk.transport.metadata.TypeMd;

public interface ComponentDTOIF
{

  /**
   * The value to use if incorrect attribute data is requested.
   */
  public static final String        EMPTY_VALUE         = "";
  /**
   * The default readable value of an attribute.
   */
  public static final boolean       READABLE_DEFAULT    = false;

  /**
   * Returns the oid of this object.
   * 
   * @return A unique oid.
   */
  public String getOid();
  
  /**
   * Returns the type of this object.
   * 
   * @return A String representing the type.
   */
  public String getType();
  
  /**
   * Sets the clientRequest.
   * 
   * @param clientRequest.
   */
  void setClientRequest(ClientRequestIF clientRequest);
  
  /**
   * Gets the display metadata for this type.
   * @return display metadata for this type.
   */
  public TypeMd getMd();
  
  /**
   * Sets the display metadata.
   * @param typeMd
   */
  public void setMd(TypeMd typeMd);
  
  /**
   * Returns the ClientRequest use for this DTO's construction.
   * 
   * @return ClientRequest use for this DTO's construction.
   */
  public ClientRequestIF getRequest();
  
  /**
   * Checks if this entity is readable.
   * 
   * @return true if the entity is readable, false otherwise.
   */
  public boolean isReadable();
  
  /**
   * Checks if an attribute can be read or not.
   * 
   * @param attributeName
   * @return true if the value can be read, false otherwise.
   */
  public boolean isReadable(String attributeName);
  
  /**
   * Returns all the names of every attribute in this BusinessDTO instance.
   * 
   * @return String[] Array of attribute names.
   */
  public List<String> getAttributeNames();
  
  /**
   * Checks if this EntityDTO has a specific attribute.
   * 
   * @param name
   *          The name of the attribute.
   * @return true if the attribute exists, false otherwise.
   */
  public boolean hasAttribute(String attributeName);
  
  /**
   * Returns the type of the specified attribute.
   * 
   * @param attributeName
   * @return The type of the attribute.
   */
  public String getAttributeType(String attributeName);
  
  /**
   * Returns the value of an attribute.
   * 
   * @param attributeName
   * @return The value of the specified attribute.
   */
  public String getValue(String attributeName);
  
  /**
   * Returns the names of the enumeration items for the given attribute.
   * @param attributeName
   * @return names of the enumeration items for the given attribute.
   */
  public List<String> getEnumNames(String attributeName);
  
  /**
   * If this is called on the front end, it will return all BusinessDTOs for which this
   * object has an internal mapping.  If this object only has the oid of an enumItem, it
   * will fetch the generic BusinessDTO for it via the clientRequest.  If this is called on the 
   * back-end, it will only return the BusinessDTOs that it has references to.  If it only 
   * has an oid for an enumItem, it does not return the BusinessDTO for it.  This is OK,
   * as on the back-end, the object should contain all BusinessDTOs.  On the front-end,
   * the method addEnumItem(String) may have been called, but the object does not have a
   * businessDTO for that item.
   * 
   * @param attributeName
   *          The name of the attribute to retrieve all items.
   * @returns A List of strings where each string is an item oid. An emptry list
   *          is returned if no items exist for the attribute.
   */
  public List<? extends BusinessDTO> getEnumValues(String attributeName);
  
  /**
   * Returns a binary value for an attribute on this ocmponent with the given name.
   * @param attributeName
   * @return binary value
   */
  public byte[] getBlob(String attributeName);
  
  /**
   * Copies over properties from the given componentDTOIF.
   * @param componentDTOIF
   */
  public void copyProperties(ComponentDTOIF componentDTOIF);
  
  /**
   * Copies over properties from the given componentDTOIF.
   * @param componentDTO
   * @param clonedAttributeMap cloned attribute map from componentDTOIF.
   */
  public void copyProperties(ComponentDTOIF componentDTOIF, Map<String, AttributeDTO> clonedAttributeMap);
  
}
