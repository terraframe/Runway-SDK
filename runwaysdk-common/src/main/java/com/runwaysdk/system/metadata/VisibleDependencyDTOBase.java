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

@com.runwaysdk.business.ClassSignature(hash = -1388598951)
public abstract class VisibleDependencyDTOBase extends com.runwaysdk.system.metadata.MetadataRelationshipDTO
{
  public final static String CLASS = "com.runwaysdk.system.metadata.VisibleDependency";
  private static final long serialVersionUID = -1388598951;
  
  public VisibleDependencyDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String parentOid, java.lang.String childOid)
  {
    super(clientRequest, parentOid, childOid);
    
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given RelationshipDTO into a new DTO.
  * 
  * @param relationshipDTO The RelationshipDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected VisibleDependencyDTOBase(com.runwaysdk.business.RelationshipDTO relationshipDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(relationshipDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public final static java.lang.String CONDITION = "condition";
  public com.runwaysdk.system.metadata.FieldConditionDTO getCondition()
  {
    if(getValue(CONDITION) == null || getValue(CONDITION).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.metadata.FieldConditionDTO.get(getRequest(), getValue(CONDITION));
    }
  }
  
  public String getConditionId()
  {
    return getValue(CONDITION);
  }
  
  public void setCondition(com.runwaysdk.system.metadata.FieldConditionDTO value)
  {
    if(value == null)
    {
      setValue(CONDITION, "");
    }
    else
    {
      setValue(CONDITION, value.getOid());
    }
  }
  
  public boolean isConditionWritable()
  {
    return isWritable(CONDITION);
  }
  
  public boolean isConditionReadable()
  {
    return isReadable(CONDITION);
  }
  
  public boolean isConditionModified()
  {
    return isModified(CONDITION);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getConditionMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(CONDITION).getAttributeMdDTO();
  }
  
  public com.runwaysdk.system.metadata.MdFieldDTO getParent()
  {
    return com.runwaysdk.system.metadata.MdFieldDTO.get(getRequest(), super.getParentOid());
  }
  
    public com.runwaysdk.system.metadata.MdFieldDTO getChild()
  {
    return com.runwaysdk.system.metadata.MdFieldDTO.get(getRequest(), super.getChildOid());
  }
  
  public static com.runwaysdk.system.metadata.VisibleDependencyDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String oid)
  {
    com.runwaysdk.business.RelationshipDTO dto = (com.runwaysdk.business.RelationshipDTO) clientRequest.get(oid);
    
    return (com.runwaysdk.system.metadata.VisibleDependencyDTO) dto;
  }
  
  public static com.runwaysdk.system.metadata.VisibleDependencyQueryDTO parentQuery(com.runwaysdk.constants.ClientRequestIF clientRequest, String parentOid)
  {
    com.runwaysdk.business.RelationshipQueryDTO queryDTO = (com.runwaysdk.business.RelationshipQueryDTO) clientRequest.getQuery(com.runwaysdk.system.metadata.VisibleDependencyDTO.CLASS);
    queryDTO.addCondition("parent_oid", "EQ", parentOid);
    return (com.runwaysdk.system.metadata.VisibleDependencyQueryDTO) clientRequest.queryRelationships(queryDTO);
  }
  public static com.runwaysdk.system.metadata.VisibleDependencyQueryDTO childQuery(com.runwaysdk.constants.ClientRequestIF clientRequest, String childOid)
  {
    com.runwaysdk.business.RelationshipQueryDTO queryDTO = (com.runwaysdk.business.RelationshipQueryDTO) clientRequest.getQuery(com.runwaysdk.system.metadata.VisibleDependencyDTO.CLASS);
    queryDTO.addCondition("child_oid", "EQ", childOid);
    return (com.runwaysdk.system.metadata.VisibleDependencyQueryDTO) clientRequest.queryRelationships(queryDTO);
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
  
  public static com.runwaysdk.system.metadata.VisibleDependencyQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.metadata.VisibleDependencyQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.metadata.VisibleDependencyDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.metadata.VisibleDependencyDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.VisibleDependencyDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.metadata.VisibleDependencyDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.metadata.VisibleDependencyDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.VisibleDependencyDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.metadata.VisibleDependencyDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
