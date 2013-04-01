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

@com.runwaysdk.business.ClassSignature(hash = -1217894807)
public abstract class MethodActorDTOBase extends com.runwaysdk.system.SingleActorDTO
{
  public final static String CLASS = "com.runwaysdk.system.MethodActor";
  private static final long serialVersionUID = -1217894807;
  
  protected MethodActorDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected MethodActorDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String MDMETHOD = "mdMethod";
  public com.runwaysdk.system.metadata.MdMethodDTO getMdMethod()
  {
    if(getValue(MDMETHOD) == null || getValue(MDMETHOD).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.metadata.MdMethodDTO.get(getRequest(), getValue(MDMETHOD));
    }
  }
  
  public String getMdMethodId()
  {
    return getValue(MDMETHOD);
  }
  
  public void setMdMethod(com.runwaysdk.system.metadata.MdMethodDTO value)
  {
    if(value == null)
    {
      setValue(MDMETHOD, "");
    }
    else
    {
      setValue(MDMETHOD, value.getId());
    }
  }
  
  public boolean isMdMethodWritable()
  {
    return isWritable(MDMETHOD);
  }
  
  public boolean isMdMethodReadable()
  {
    return isReadable(MDMETHOD);
  }
  
  public boolean isMdMethodModified()
  {
    return isModified(MDMETHOD);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getMdMethodMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(MDMETHOD).getAttributeMdDTO();
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.MdMethodDTO> getAllMdMethod()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdMethodDTO>) getRequest().getParents(this.getId(), com.runwaysdk.system.metadata.MdMethodMethodActorDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.MdMethodDTO> getAllMdMethod(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdMethodDTO>) clientRequestIF.getParents(id, com.runwaysdk.system.metadata.MdMethodMethodActorDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.MdMethodMethodActorDTO> getAllMdMethodRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdMethodMethodActorDTO>) getRequest().getParentRelationships(this.getId(), com.runwaysdk.system.metadata.MdMethodMethodActorDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.MdMethodMethodActorDTO> getAllMdMethodRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdMethodMethodActorDTO>) clientRequestIF.getParentRelationships(id, com.runwaysdk.system.metadata.MdMethodMethodActorDTO.CLASS);
  }
  
  public com.runwaysdk.system.metadata.MdMethodMethodActorDTO addMdMethod(com.runwaysdk.system.metadata.MdMethodDTO parent)
  {
    return (com.runwaysdk.system.metadata.MdMethodMethodActorDTO) getRequest().addParent(parent.getId(), this.getId(), com.runwaysdk.system.metadata.MdMethodMethodActorDTO.CLASS);
  }
  
  public static com.runwaysdk.system.metadata.MdMethodMethodActorDTO addMdMethod(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id, com.runwaysdk.system.metadata.MdMethodDTO parent)
  {
    return (com.runwaysdk.system.metadata.MdMethodMethodActorDTO) clientRequestIF.addParent(parent.getId(), id, com.runwaysdk.system.metadata.MdMethodMethodActorDTO.CLASS);
  }
  
  public void removeMdMethod(com.runwaysdk.system.metadata.MdMethodMethodActorDTO relationship)
  {
    getRequest().deleteParent(relationship.getId());
  }
  
  public static void removeMdMethod(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.metadata.MdMethodMethodActorDTO relationship)
  {
    clientRequestIF.deleteParent(relationship.getId());
  }
  
  public void removeAllMdMethod()
  {
    getRequest().deleteParents(this.getId(), com.runwaysdk.system.metadata.MdMethodMethodActorDTO.CLASS);
  }
  
  public static void removeAllMdMethod(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    clientRequestIF.deleteParents(id, com.runwaysdk.system.metadata.MdMethodMethodActorDTO.CLASS);
  }
  
  public static com.runwaysdk.system.MethodActorDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String id)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(id);
    
    return (com.runwaysdk.system.MethodActorDTO) dto;
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
  
  public static com.runwaysdk.system.MethodActorQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.MethodActorQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.MethodActorDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.MethodActorDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.MethodActorDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.MethodActorDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.MethodActorDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.MethodActorDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.MethodActorDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
