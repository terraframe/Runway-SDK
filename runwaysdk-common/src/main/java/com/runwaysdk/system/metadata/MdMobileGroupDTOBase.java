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
package com.runwaysdk.system.metadata;

@com.runwaysdk.business.ClassSignature(hash = 1382059072)
public abstract class MdMobileGroupDTOBase extends com.runwaysdk.system.metadata.MdMobileFieldDTO
{
  public final static String CLASS = "com.runwaysdk.system.metadata.MdMobileGroup";
  private static final long serialVersionUID = 1382059072;
  
  protected MdMobileGroupDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected MdMobileGroupDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.MdMobileFieldDTO> getAllMobileGroups()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdMobileFieldDTO>) getRequest().getChildren(this.getOid(), com.runwaysdk.system.metadata.MobileGroupFieldDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.MdMobileFieldDTO> getAllMobileGroups(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdMobileFieldDTO>) clientRequestIF.getChildren(oid, com.runwaysdk.system.metadata.MobileGroupFieldDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.MobileGroupFieldDTO> getAllMobileGroupsRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MobileGroupFieldDTO>) getRequest().getChildRelationships(this.getOid(), com.runwaysdk.system.metadata.MobileGroupFieldDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.MobileGroupFieldDTO> getAllMobileGroupsRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MobileGroupFieldDTO>) clientRequestIF.getChildRelationships(oid, com.runwaysdk.system.metadata.MobileGroupFieldDTO.CLASS);
  }
  
  public com.runwaysdk.system.metadata.MobileGroupFieldDTO addMobileGroups(com.runwaysdk.system.metadata.MdMobileFieldDTO child)
  {
    return (com.runwaysdk.system.metadata.MobileGroupFieldDTO) getRequest().addChild(this.getOid(), child.getOid(), com.runwaysdk.system.metadata.MobileGroupFieldDTO.CLASS);
  }
  
  public static com.runwaysdk.system.metadata.MobileGroupFieldDTO addMobileGroups(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid, com.runwaysdk.system.metadata.MdMobileFieldDTO child)
  {
    return (com.runwaysdk.system.metadata.MobileGroupFieldDTO) clientRequestIF.addChild(oid, child.getOid(), com.runwaysdk.system.metadata.MobileGroupFieldDTO.CLASS);
  }
  
  public void removeMobileGroups(com.runwaysdk.system.metadata.MobileGroupFieldDTO relationship)
  {
    getRequest().deleteChild(relationship.getOid());
  }
  
  public static void removeMobileGroups(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.metadata.MobileGroupFieldDTO relationship)
  {
    clientRequestIF.deleteChild(relationship.getOid());
  }
  
  public void removeAllMobileGroups()
  {
    getRequest().deleteChildren(this.getOid(), com.runwaysdk.system.metadata.MobileGroupFieldDTO.CLASS);
  }
  
  public static void removeAllMobileGroups(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    clientRequestIF.deleteChildren(oid, com.runwaysdk.system.metadata.MobileGroupFieldDTO.CLASS);
  }
  
  public static com.runwaysdk.system.metadata.MdMobileGroupDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String oid)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(oid);
    
    return (com.runwaysdk.system.metadata.MdMobileGroupDTO) dto;
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
    getRequest().delete(this.getOid());
  }
  
  public static com.runwaysdk.system.metadata.MdMobileGroupQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.metadata.MdMobileGroupQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.metadata.MdMobileGroupDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdMobileGroupDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdMobileGroupDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdMobileGroupDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdMobileGroupDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdMobileGroupDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdMobileGroupDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
