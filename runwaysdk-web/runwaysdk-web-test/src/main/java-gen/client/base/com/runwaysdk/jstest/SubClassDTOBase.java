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
package com.runwaysdk.jstest;

@com.runwaysdk.business.ClassSignature(hash = -1271096785)
public abstract class SubClassDTOBase extends com.runwaysdk.jstest.TestClassDTO implements com.runwaysdk.generation.loader.
{
  public final static String CLASS = "com.runwaysdk.jstest.SubClass";
  private static final long serialVersionUID = -1271096785;
  
  protected SubClassDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected SubClassDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String SUBCHARACTER = "subCharacter";
  public String getSubCharacter()
  {
    return getValue(SUBCHARACTER);
  }
  
  public void setSubCharacter(String value)
  {
    if(value == null)
    {
      setValue(SUBCHARACTER, "");
    }
    else
    {
      setValue(SUBCHARACTER, value);
    }
  }
  
  public boolean isSubCharacterWritable()
  {
    return isWritable(SUBCHARACTER);
  }
  
  public boolean isSubCharacterReadable()
  {
    return isReadable(SUBCHARACTER);
  }
  
  public boolean isSubCharacterModified()
  {
    return isModified(SUBCHARACTER);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getSubCharacterMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(SUBCHARACTER).getAttributeMdDTO();
  }
  
  public static com.runwaysdk.jstest.SubClassDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String id)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(id);
    
    return (com.runwaysdk.jstest.SubClassDTO) dto;
  }
  
  public void apply()
  {
    if(isNewInstance())
    {
      getRequest().createBusiness(this);
    }
    else
    {
      getRequest().update(this);
    }
  }
  public void delete()
  {
    getRequest().delete(this.getId());
  }
  
  public static com.runwaysdk.jstest.SubClassQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.jstest.SubClassQueryDTO) clientRequest.getAllInstances(com.runwaysdk.jstest.SubClassDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.jstest.SubClassDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.jstest.SubClassDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.jstest.SubClassDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.jstest.SubClassDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.jstest.SubClassDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.jstest.SubClassDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
