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
package com.runwaysdk.system.metadata;

@com.runwaysdk.business.ClassSignature(hash = 1498168678)
public abstract class MdWebGroupDTOBase extends com.runwaysdk.system.metadata.MdWebFieldDTO
{
  public final static String CLASS = "com.runwaysdk.system.metadata.MdWebGroup";
  private static final long serialVersionUID = 1498168678;
  
  protected MdWebGroupDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected MdWebGroupDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.MdWebFieldDTO> getAllWebGroups()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdWebFieldDTO>) getRequest().getChildren(this.getId(), com.runwaysdk.system.metadata.WebGroupFieldDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.MdWebFieldDTO> getAllWebGroups(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdWebFieldDTO>) clientRequestIF.getChildren(id, com.runwaysdk.system.metadata.WebGroupFieldDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.WebGroupFieldDTO> getAllWebGroupsRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.WebGroupFieldDTO>) getRequest().getChildRelationships(this.getId(), com.runwaysdk.system.metadata.WebGroupFieldDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.WebGroupFieldDTO> getAllWebGroupsRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.WebGroupFieldDTO>) clientRequestIF.getChildRelationships(id, com.runwaysdk.system.metadata.WebGroupFieldDTO.CLASS);
  }
  
  public com.runwaysdk.system.metadata.WebGroupFieldDTO addWebGroups(com.runwaysdk.system.metadata.MdWebFieldDTO child)
  {
    return (com.runwaysdk.system.metadata.WebGroupFieldDTO) getRequest().addChild(this.getId(), child.getId(), com.runwaysdk.system.metadata.WebGroupFieldDTO.CLASS);
  }
  
  public static com.runwaysdk.system.metadata.WebGroupFieldDTO addWebGroups(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id, com.runwaysdk.system.metadata.MdWebFieldDTO child)
  {
    return (com.runwaysdk.system.metadata.WebGroupFieldDTO) clientRequestIF.addChild(id, child.getId(), com.runwaysdk.system.metadata.WebGroupFieldDTO.CLASS);
  }
  
  public void removeWebGroups(com.runwaysdk.system.metadata.WebGroupFieldDTO relationship)
  {
    getRequest().deleteChild(relationship.getId());
  }
  
  public static void removeWebGroups(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.metadata.WebGroupFieldDTO relationship)
  {
    clientRequestIF.deleteChild(relationship.getId());
  }
  
  public void removeAllWebGroups()
  {
    getRequest().deleteChildren(this.getId(), com.runwaysdk.system.metadata.WebGroupFieldDTO.CLASS);
  }
  
  public static void removeAllWebGroups(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    clientRequestIF.deleteChildren(id, com.runwaysdk.system.metadata.WebGroupFieldDTO.CLASS);
  }
  
  public static com.runwaysdk.system.metadata.MdWebGroupDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String id)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(id);
    
    return (com.runwaysdk.system.metadata.MdWebGroupDTO) dto;
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
  
  public static com.runwaysdk.system.metadata.MdWebGroupQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.metadata.MdWebGroupQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.metadata.MdWebGroupDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdWebGroupDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdWebGroupDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdWebGroupDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdWebGroupDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdWebGroupDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdWebGroupDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
