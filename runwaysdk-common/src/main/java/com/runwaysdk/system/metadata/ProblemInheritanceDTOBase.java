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

@com.runwaysdk.business.ClassSignature(hash = -1990814218)
public abstract class ProblemInheritanceDTOBase extends com.runwaysdk.system.metadata.ClassInheritanceDTO
{
  public final static String CLASS = "com.runwaysdk.system.metadata.ProblemInheritance";
  private static final long serialVersionUID = -1990814218;
  
  public ProblemInheritanceDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String parentId, java.lang.String childId)
  {
    super(clientRequest, parentId, childId);
    
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given RelationshipDTO into a new DTO.
  * 
  * @param relationshipDTO The RelationshipDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected ProblemInheritanceDTOBase(com.runwaysdk.business.RelationshipDTO relationshipDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(relationshipDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public com.runwaysdk.system.metadata.MdProblemDTO getParent()
  {
    return com.runwaysdk.system.metadata.MdProblemDTO.get(getRequest(), super.getParentId());
  }
  
    public com.runwaysdk.system.metadata.MdProblemDTO getChild()
  {
    return com.runwaysdk.system.metadata.MdProblemDTO.get(getRequest(), super.getChildId());
  }
  
  public static com.runwaysdk.system.metadata.ProblemInheritanceDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String oid)
  {
    com.runwaysdk.business.RelationshipDTO dto = (com.runwaysdk.business.RelationshipDTO) clientRequest.get(oid);
    
    return (com.runwaysdk.system.metadata.ProblemInheritanceDTO) dto;
  }
  
  public static com.runwaysdk.system.metadata.ProblemInheritanceQueryDTO parentQuery(com.runwaysdk.constants.ClientRequestIF clientRequest, String parentId)
  {
    com.runwaysdk.business.RelationshipQueryDTO queryDTO = (com.runwaysdk.business.RelationshipQueryDTO) clientRequest.getQuery(com.runwaysdk.system.metadata.ProblemInheritanceDTO.CLASS);
    queryDTO.addCondition("parent_id", "EQ", parentId);
    return (com.runwaysdk.system.metadata.ProblemInheritanceQueryDTO) clientRequest.queryRelationships(queryDTO);
  }
  public static com.runwaysdk.system.metadata.ProblemInheritanceQueryDTO childQuery(com.runwaysdk.constants.ClientRequestIF clientRequest, String childId)
  {
    com.runwaysdk.business.RelationshipQueryDTO queryDTO = (com.runwaysdk.business.RelationshipQueryDTO) clientRequest.getQuery(com.runwaysdk.system.metadata.ProblemInheritanceDTO.CLASS);
    queryDTO.addCondition("child_id", "EQ", childId);
    return (com.runwaysdk.system.metadata.ProblemInheritanceQueryDTO) clientRequest.queryRelationships(queryDTO);
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
  
  public static com.runwaysdk.system.metadata.ProblemInheritanceQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.metadata.ProblemInheritanceQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.metadata.ProblemInheritanceDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.metadata.ProblemInheritanceDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.ProblemInheritanceDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.metadata.ProblemInheritanceDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.metadata.ProblemInheritanceDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.ProblemInheritanceDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.metadata.ProblemInheritanceDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
