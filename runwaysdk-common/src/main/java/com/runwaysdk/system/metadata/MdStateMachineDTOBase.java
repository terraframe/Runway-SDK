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
package com.runwaysdk.system.metadata;

@com.runwaysdk.business.ClassSignature(hash = 1927665840)
public abstract class MdStateMachineDTOBase extends com.runwaysdk.system.metadata.MdBusinessDTO
{
  public final static String CLASS = "com.runwaysdk.system.metadata.MdStateMachine";
  private static final long serialVersionUID = 1927665840;
  
  protected MdStateMachineDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected MdStateMachineDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String STATEMACHINEOWNER = "stateMachineOwner";
  public com.runwaysdk.system.metadata.MdBusinessDTO getStateMachineOwner()
  {
    if(getValue(STATEMACHINEOWNER) == null || getValue(STATEMACHINEOWNER).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.metadata.MdBusinessDTO.get(getRequest(), getValue(STATEMACHINEOWNER));
    }
  }
  
  public String getStateMachineOwnerId()
  {
    return getValue(STATEMACHINEOWNER);
  }
  
  public void setStateMachineOwner(com.runwaysdk.system.metadata.MdBusinessDTO value)
  {
    if(value == null)
    {
      setValue(STATEMACHINEOWNER, "");
    }
    else
    {
      setValue(STATEMACHINEOWNER, value.getId());
    }
  }
  
  public boolean isStateMachineOwnerWritable()
  {
    return isWritable(STATEMACHINEOWNER);
  }
  
  public boolean isStateMachineOwnerReadable()
  {
    return isReadable(STATEMACHINEOWNER);
  }
  
  public boolean isStateMachineOwnerModified()
  {
    return isModified(STATEMACHINEOWNER);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getStateMachineOwnerMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(STATEMACHINEOWNER).getAttributeMdDTO();
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.MdBusinessDTO> getAllStateMachine()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdBusinessDTO>) getRequest().getParents(this.getId(), com.runwaysdk.system.DefinesStateMachineDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.MdBusinessDTO> getAllStateMachine(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdBusinessDTO>) clientRequestIF.getParents(id, com.runwaysdk.system.DefinesStateMachineDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.DefinesStateMachineDTO> getAllStateMachineRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.DefinesStateMachineDTO>) getRequest().getParentRelationships(this.getId(), com.runwaysdk.system.DefinesStateMachineDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.DefinesStateMachineDTO> getAllStateMachineRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.DefinesStateMachineDTO>) clientRequestIF.getParentRelationships(id, com.runwaysdk.system.DefinesStateMachineDTO.CLASS);
  }
  
  public com.runwaysdk.system.DefinesStateMachineDTO addStateMachine(com.runwaysdk.system.metadata.MdBusinessDTO parent)
  {
    return (com.runwaysdk.system.DefinesStateMachineDTO) getRequest().addParent(parent.getId(), this.getId(), com.runwaysdk.system.DefinesStateMachineDTO.CLASS);
  }
  
  public static com.runwaysdk.system.DefinesStateMachineDTO addStateMachine(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id, com.runwaysdk.system.metadata.MdBusinessDTO parent)
  {
    return (com.runwaysdk.system.DefinesStateMachineDTO) clientRequestIF.addParent(parent.getId(), id, com.runwaysdk.system.DefinesStateMachineDTO.CLASS);
  }
  
  public void removeStateMachine(com.runwaysdk.system.DefinesStateMachineDTO relationship)
  {
    getRequest().deleteParent(relationship.getId());
  }
  
  public static void removeStateMachine(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.DefinesStateMachineDTO relationship)
  {
    clientRequestIF.deleteParent(relationship.getId());
  }
  
  public void removeAllStateMachine()
  {
    getRequest().deleteParents(this.getId(), com.runwaysdk.system.DefinesStateMachineDTO.CLASS);
  }
  
  public static void removeAllStateMachine(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    clientRequestIF.deleteParents(id, com.runwaysdk.system.DefinesStateMachineDTO.CLASS);
  }
  
  public static com.runwaysdk.system.metadata.MdStateMachineDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String id)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(id);
    
    return (com.runwaysdk.system.metadata.MdStateMachineDTO) dto;
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
  
  public static com.runwaysdk.system.metadata.MdStateMachineQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.metadata.MdStateMachineQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.metadata.MdStateMachineDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdStateMachineDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdStateMachineDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdStateMachineDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdStateMachineDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdStateMachineDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdStateMachineDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
