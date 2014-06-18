/*******************************************************************************
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
 ******************************************************************************/
package com.runwaysdk.system;

@com.runwaysdk.business.ClassSignature(hash = 656711673)
public abstract class SingleActorDTOBase extends com.runwaysdk.system.ActorDTO
{
  public final static String CLASS = "com.runwaysdk.system.SingleActor";
  private static final long serialVersionUID = 656711673;
  
  protected SingleActorDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected SingleActorDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.RolesDTO> getAllAssignedRole()
  {
    return (java.util.List<? extends com.runwaysdk.system.RolesDTO>) getRequest().getChildren(this.getId(), com.runwaysdk.system.AssignmentsDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.RolesDTO> getAllAssignedRole(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.RolesDTO>) clientRequestIF.getChildren(id, com.runwaysdk.system.AssignmentsDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.AssignmentsDTO> getAllAssignedRoleRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.AssignmentsDTO>) getRequest().getChildRelationships(this.getId(), com.runwaysdk.system.AssignmentsDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.AssignmentsDTO> getAllAssignedRoleRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.AssignmentsDTO>) clientRequestIF.getChildRelationships(id, com.runwaysdk.system.AssignmentsDTO.CLASS);
  }
  
  public com.runwaysdk.system.AssignmentsDTO addAssignedRole(com.runwaysdk.system.RolesDTO child)
  {
    return (com.runwaysdk.system.AssignmentsDTO) getRequest().addChild(this.getId(), child.getId(), com.runwaysdk.system.AssignmentsDTO.CLASS);
  }
  
  public static com.runwaysdk.system.AssignmentsDTO addAssignedRole(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id, com.runwaysdk.system.RolesDTO child)
  {
    return (com.runwaysdk.system.AssignmentsDTO) clientRequestIF.addChild(id, child.getId(), com.runwaysdk.system.AssignmentsDTO.CLASS);
  }
  
  public void removeAssignedRole(com.runwaysdk.system.AssignmentsDTO relationship)
  {
    getRequest().deleteChild(relationship.getId());
  }
  
  public static void removeAssignedRole(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.AssignmentsDTO relationship)
  {
    clientRequestIF.deleteChild(relationship.getId());
  }
  
  public void removeAllAssignedRole()
  {
    getRequest().deleteChildren(this.getId(), com.runwaysdk.system.AssignmentsDTO.CLASS);
  }
  
  public static void removeAllAssignedRole(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    clientRequestIF.deleteChildren(id, com.runwaysdk.system.AssignmentsDTO.CLASS);
  }
  
  public static com.runwaysdk.system.SingleActorDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String id)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(id);
    
    return (com.runwaysdk.system.SingleActorDTO) dto;
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
  
  public static com.runwaysdk.system.SingleActorQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.SingleActorQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.SingleActorDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.SingleActorDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.SingleActorDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.SingleActorDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.SingleActorDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.SingleActorDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.SingleActorDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
