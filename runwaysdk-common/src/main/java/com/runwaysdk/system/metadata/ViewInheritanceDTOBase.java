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
package com.runwaysdk.system.metadata;

@com.runwaysdk.business.ClassSignature(hash = 1153460302)
public abstract class ViewInheritanceDTOBase extends com.runwaysdk.system.metadata.ClassInheritanceDTO
{
  public final static String CLASS = "com.runwaysdk.system.metadata.ViewInheritance";
  private static final long serialVersionUID = 1153460302;
  
  public ViewInheritanceDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String parentOid, java.lang.String childOid)
  {
    super(clientRequest, parentOid, childOid);
    
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given RelationshipDTO into a new DTO.
  * 
  * @param relationshipDTO The RelationshipDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected ViewInheritanceDTOBase(com.runwaysdk.business.RelationshipDTO relationshipDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(relationshipDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public com.runwaysdk.system.metadata.MdViewDTO getParent()
  {
    return com.runwaysdk.system.metadata.MdViewDTO.get(getRequest(), super.getParentOid());
  }
  
    public com.runwaysdk.system.metadata.MdViewDTO getChild()
  {
    return com.runwaysdk.system.metadata.MdViewDTO.get(getRequest(), super.getChildOid());
  }
  
  public static com.runwaysdk.system.metadata.ViewInheritanceDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String oid)
  {
    com.runwaysdk.business.RelationshipDTO dto = (com.runwaysdk.business.RelationshipDTO) clientRequest.get(oid);
    
    return (com.runwaysdk.system.metadata.ViewInheritanceDTO) dto;
  }
  
  public static com.runwaysdk.system.metadata.ViewInheritanceQueryDTO parentQuery(com.runwaysdk.constants.ClientRequestIF clientRequest, String parentOid)
  {
    com.runwaysdk.business.RelationshipQueryDTO queryDTO = (com.runwaysdk.business.RelationshipQueryDTO) clientRequest.getQuery(com.runwaysdk.system.metadata.ViewInheritanceDTO.CLASS);
    queryDTO.addCondition("parent_oid", "EQ", parentOid);
    return (com.runwaysdk.system.metadata.ViewInheritanceQueryDTO) clientRequest.queryRelationships(queryDTO);
  }
  public static com.runwaysdk.system.metadata.ViewInheritanceQueryDTO childQuery(com.runwaysdk.constants.ClientRequestIF clientRequest, String childOid)
  {
    com.runwaysdk.business.RelationshipQueryDTO queryDTO = (com.runwaysdk.business.RelationshipQueryDTO) clientRequest.getQuery(com.runwaysdk.system.metadata.ViewInheritanceDTO.CLASS);
    queryDTO.addCondition("child_oid", "EQ", childOid);
    return (com.runwaysdk.system.metadata.ViewInheritanceQueryDTO) clientRequest.queryRelationships(queryDTO);
  }
  public void apply()
  {
    if(isNewInstance())
    {
      getRequest().createRelationship(this);
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
  
  public static com.runwaysdk.system.metadata.ViewInheritanceQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.metadata.ViewInheritanceQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.metadata.ViewInheritanceDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.metadata.ViewInheritanceDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.ViewInheritanceDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.metadata.ViewInheritanceDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.metadata.ViewInheritanceDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.ViewInheritanceDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.metadata.ViewInheritanceDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
