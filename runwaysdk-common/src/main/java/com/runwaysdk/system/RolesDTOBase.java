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
package com.runwaysdk.system;

@com.runwaysdk.business.ClassSignature(hash = -990499063)
public abstract class RolesDTOBase extends com.runwaysdk.system.ActorDTO
{
  public final static String CLASS = "com.runwaysdk.system.Roles";
  private static final long serialVersionUID = -990499063;
  
  protected RolesDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected RolesDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String DISPLAYLABEL = "displayLabel";
  public static java.lang.String ROLENAME = "roleName";
  public com.runwaysdk.system.metadata.MetadataDisplayLabelDTO getDisplayLabel()
  {
    return (com.runwaysdk.system.metadata.MetadataDisplayLabelDTO) this.getAttributeStructDTO(DISPLAYLABEL).getStructDTO();
  }
  
  public boolean isDisplayLabelWritable()
  {
    return isWritable(DISPLAYLABEL);
  }
  
  public boolean isDisplayLabelReadable()
  {
    return isReadable(DISPLAYLABEL);
  }
  
  public boolean isDisplayLabelModified()
  {
    return isModified(DISPLAYLABEL);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeLocalCharacterMdDTO getDisplayLabelMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeLocalCharacterMdDTO) getAttributeDTO(DISPLAYLABEL).getAttributeMdDTO();
  }
  
  public String getRoleName()
  {
    return getValue(ROLENAME);
  }
  
  public void setRoleName(String value)
  {
    if(value == null)
    {
      setValue(ROLENAME, "");
    }
    else
    {
      setValue(ROLENAME, value);
    }
  }
  
  public boolean isRoleNameWritable()
  {
    return isWritable(ROLENAME);
  }
  
  public boolean isRoleNameReadable()
  {
    return isReadable(ROLENAME);
  }
  
  public boolean isRoleNameModified()
  {
    return isModified(ROLENAME);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getRoleNameMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(ROLENAME).getAttributeMdDTO();
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.SDutyDTO> getAllRole()
  {
    return (java.util.List<? extends com.runwaysdk.system.SDutyDTO>) getRequest().getChildren(this.getId(), com.runwaysdk.system.ConflictingRolesDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.SDutyDTO> getAllRole(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.SDutyDTO>) clientRequestIF.getChildren(id, com.runwaysdk.system.ConflictingRolesDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.ConflictingRolesDTO> getAllRoleRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.ConflictingRolesDTO>) getRequest().getChildRelationships(this.getId(), com.runwaysdk.system.ConflictingRolesDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.ConflictingRolesDTO> getAllRoleRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.ConflictingRolesDTO>) clientRequestIF.getChildRelationships(id, com.runwaysdk.system.ConflictingRolesDTO.CLASS);
  }
  
  public com.runwaysdk.system.ConflictingRolesDTO addRole(com.runwaysdk.system.SDutyDTO child)
  {
    return (com.runwaysdk.system.ConflictingRolesDTO) getRequest().addChild(this.getId(), child.getId(), com.runwaysdk.system.ConflictingRolesDTO.CLASS);
  }
  
  public static com.runwaysdk.system.ConflictingRolesDTO addRole(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id, com.runwaysdk.system.SDutyDTO child)
  {
    return (com.runwaysdk.system.ConflictingRolesDTO) clientRequestIF.addChild(id, child.getId(), com.runwaysdk.system.ConflictingRolesDTO.CLASS);
  }
  
  public void removeRole(com.runwaysdk.system.ConflictingRolesDTO relationship)
  {
    getRequest().deleteChild(relationship.getId());
  }
  
  public static void removeRole(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.ConflictingRolesDTO relationship)
  {
    clientRequestIF.deleteChild(relationship.getId());
  }
  
  public void removeAllRole()
  {
    getRequest().deleteChildren(this.getId(), com.runwaysdk.system.ConflictingRolesDTO.CLASS);
  }
  
  public static void removeAllRole(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    clientRequestIF.deleteChildren(id, com.runwaysdk.system.ConflictingRolesDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.RolesDTO> getAllChildRole()
  {
    return (java.util.List<? extends com.runwaysdk.system.RolesDTO>) getRequest().getChildren(this.getId(), com.runwaysdk.system.RoleInheritanceDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.RolesDTO> getAllChildRole(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.RolesDTO>) clientRequestIF.getChildren(id, com.runwaysdk.system.RoleInheritanceDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.RoleInheritanceDTO> getAllChildRoleRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.RoleInheritanceDTO>) getRequest().getChildRelationships(this.getId(), com.runwaysdk.system.RoleInheritanceDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.RoleInheritanceDTO> getAllChildRoleRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.RoleInheritanceDTO>) clientRequestIF.getChildRelationships(id, com.runwaysdk.system.RoleInheritanceDTO.CLASS);
  }
  
  public com.runwaysdk.system.RoleInheritanceDTO addChildRole(com.runwaysdk.system.RolesDTO child)
  {
    return (com.runwaysdk.system.RoleInheritanceDTO) getRequest().addChild(this.getId(), child.getId(), com.runwaysdk.system.RoleInheritanceDTO.CLASS);
  }
  
  public static com.runwaysdk.system.RoleInheritanceDTO addChildRole(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id, com.runwaysdk.system.RolesDTO child)
  {
    return (com.runwaysdk.system.RoleInheritanceDTO) clientRequestIF.addChild(id, child.getId(), com.runwaysdk.system.RoleInheritanceDTO.CLASS);
  }
  
  public void removeChildRole(com.runwaysdk.system.RoleInheritanceDTO relationship)
  {
    getRequest().deleteChild(relationship.getId());
  }
  
  public static void removeChildRole(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.RoleInheritanceDTO relationship)
  {
    clientRequestIF.deleteChild(relationship.getId());
  }
  
  public void removeAllChildRole()
  {
    getRequest().deleteChildren(this.getId(), com.runwaysdk.system.RoleInheritanceDTO.CLASS);
  }
  
  public static void removeAllChildRole(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    clientRequestIF.deleteChildren(id, com.runwaysdk.system.RoleInheritanceDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.RolesDTO> getAllParentRole()
  {
    return (java.util.List<? extends com.runwaysdk.system.RolesDTO>) getRequest().getParents(this.getId(), com.runwaysdk.system.RoleInheritanceDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.RolesDTO> getAllParentRole(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.RolesDTO>) clientRequestIF.getParents(id, com.runwaysdk.system.RoleInheritanceDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.RoleInheritanceDTO> getAllParentRoleRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.RoleInheritanceDTO>) getRequest().getParentRelationships(this.getId(), com.runwaysdk.system.RoleInheritanceDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.RoleInheritanceDTO> getAllParentRoleRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.RoleInheritanceDTO>) clientRequestIF.getParentRelationships(id, com.runwaysdk.system.RoleInheritanceDTO.CLASS);
  }
  
  public com.runwaysdk.system.RoleInheritanceDTO addParentRole(com.runwaysdk.system.RolesDTO parent)
  {
    return (com.runwaysdk.system.RoleInheritanceDTO) getRequest().addParent(parent.getId(), this.getId(), com.runwaysdk.system.RoleInheritanceDTO.CLASS);
  }
  
  public static com.runwaysdk.system.RoleInheritanceDTO addParentRole(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id, com.runwaysdk.system.RolesDTO parent)
  {
    return (com.runwaysdk.system.RoleInheritanceDTO) clientRequestIF.addParent(parent.getId(), id, com.runwaysdk.system.RoleInheritanceDTO.CLASS);
  }
  
  public void removeParentRole(com.runwaysdk.system.RoleInheritanceDTO relationship)
  {
    getRequest().deleteParent(relationship.getId());
  }
  
  public static void removeParentRole(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.RoleInheritanceDTO relationship)
  {
    clientRequestIF.deleteParent(relationship.getId());
  }
  
  public void removeAllParentRole()
  {
    getRequest().deleteParents(this.getId(), com.runwaysdk.system.RoleInheritanceDTO.CLASS);
  }
  
  public static void removeAllParentRole(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    clientRequestIF.deleteParents(id, com.runwaysdk.system.RoleInheritanceDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.SingleActorDTO> getAllSingleActor()
  {
    return (java.util.List<? extends com.runwaysdk.system.SingleActorDTO>) getRequest().getParents(this.getId(), com.runwaysdk.system.AssignmentsDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.SingleActorDTO> getAllSingleActor(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.SingleActorDTO>) clientRequestIF.getParents(id, com.runwaysdk.system.AssignmentsDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.AssignmentsDTO> getAllSingleActorRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.AssignmentsDTO>) getRequest().getParentRelationships(this.getId(), com.runwaysdk.system.AssignmentsDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.AssignmentsDTO> getAllSingleActorRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.AssignmentsDTO>) clientRequestIF.getParentRelationships(id, com.runwaysdk.system.AssignmentsDTO.CLASS);
  }
  
  public com.runwaysdk.system.AssignmentsDTO addSingleActor(com.runwaysdk.system.SingleActorDTO parent)
  {
    return (com.runwaysdk.system.AssignmentsDTO) getRequest().addParent(parent.getId(), this.getId(), com.runwaysdk.system.AssignmentsDTO.CLASS);
  }
  
  public static com.runwaysdk.system.AssignmentsDTO addSingleActor(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id, com.runwaysdk.system.SingleActorDTO parent)
  {
    return (com.runwaysdk.system.AssignmentsDTO) clientRequestIF.addParent(parent.getId(), id, com.runwaysdk.system.AssignmentsDTO.CLASS);
  }
  
  public void removeSingleActor(com.runwaysdk.system.AssignmentsDTO relationship)
  {
    getRequest().deleteParent(relationship.getId());
  }
  
  public static void removeSingleActor(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.AssignmentsDTO relationship)
  {
    clientRequestIF.deleteParent(relationship.getId());
  }
  
  public void removeAllSingleActor()
  {
    getRequest().deleteParents(this.getId(), com.runwaysdk.system.AssignmentsDTO.CLASS);
  }
  
  public static void removeAllSingleActor(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    clientRequestIF.deleteParents(id, com.runwaysdk.system.AssignmentsDTO.CLASS);
  }
  
  public static com.runwaysdk.system.RolesDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String id)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(id);
    
    return (com.runwaysdk.system.RolesDTO) dto;
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
  
  public static com.runwaysdk.system.RolesQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.RolesQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.RolesDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.RolesDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.RolesDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.RolesDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.RolesDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.RolesDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.RolesDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
