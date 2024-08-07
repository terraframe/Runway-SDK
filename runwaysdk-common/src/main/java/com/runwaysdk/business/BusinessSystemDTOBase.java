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
package com.runwaysdk.business;

@com.runwaysdk.business.ClassSignature(hash = -1488791685)
public abstract class BusinessSystemDTOBase extends com.runwaysdk.business.ElementSystemDTO
{
  public final static String CLASS = "com.runwaysdk.business.Business";
  private static final long serialVersionUID = -1488791685;
  
  protected BusinessSystemDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected BusinessSystemDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.MetadataDTO> getAllParentMetadata()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MetadataDTO>) getRequest().getParents(this.getOid(), com.runwaysdk.system.metadata.MetadataRelationshipDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.MetadataDTO> getAllParentMetadata(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MetadataDTO>) clientRequestIF.getParents(oid, com.runwaysdk.system.metadata.MetadataRelationshipDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.MetadataRelationshipDTO> getAllParentMetadataRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MetadataRelationshipDTO>) getRequest().getParentRelationships(this.getOid(), com.runwaysdk.system.metadata.MetadataRelationshipDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.MetadataRelationshipDTO> getAllParentMetadataRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MetadataRelationshipDTO>) clientRequestIF.getParentRelationships(oid, com.runwaysdk.system.metadata.MetadataRelationshipDTO.CLASS);
  }
  
  public com.runwaysdk.system.metadata.MetadataRelationshipDTO addParentMetadata(com.runwaysdk.system.metadata.MetadataDTO parent)
  {
    return (com.runwaysdk.system.metadata.MetadataRelationshipDTO) getRequest().addParent(parent.getOid(), this.getOid(), com.runwaysdk.system.metadata.MetadataRelationshipDTO.CLASS);
  }
  
  public static com.runwaysdk.system.metadata.MetadataRelationshipDTO addParentMetadata(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid, com.runwaysdk.system.metadata.MetadataDTO parent)
  {
    return (com.runwaysdk.system.metadata.MetadataRelationshipDTO) clientRequestIF.addParent(parent.getOid(), oid, com.runwaysdk.system.metadata.MetadataRelationshipDTO.CLASS);
  }
  
  public void removeParentMetadata(com.runwaysdk.system.metadata.MetadataRelationshipDTO relationship)
  {
    getRequest().deleteParent(relationship.getOid());
  }
  
  public static void removeParentMetadata(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.metadata.MetadataRelationshipDTO relationship)
  {
    clientRequestIF.deleteParent(relationship.getOid());
  }
  
  public void removeAllParentMetadata()
  {
    getRequest().deleteParents(this.getOid(), com.runwaysdk.system.metadata.MetadataRelationshipDTO.CLASS);
  }
  
  public static void removeAllParentMetadata(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    clientRequestIF.deleteParents(oid, com.runwaysdk.system.metadata.MetadataRelationshipDTO.CLASS);
  }
  
  public static com.runwaysdk.business.BusinessSystemDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String oid)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(oid);
    
    return (com.runwaysdk.business.BusinessSystemDTO) dto;
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
  
  public static com.runwaysdk.business.BusinessQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.business.BusinessQueryDTO) clientRequest.getAllInstances(com.runwaysdk.business.BusinessDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.business.BusinessSystemDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.business.BusinessSystemDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.business.BusinessSystemDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.business.BusinessSystemDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.business.BusinessSystemDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.business.BusinessSystemDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
