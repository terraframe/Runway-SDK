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

@com.runwaysdk.business.ClassSignature(hash = -366658978)
public abstract class MdControllerDTOBase extends com.runwaysdk.system.metadata.MdTypeDTO
{
  public final static String CLASS = "com.runwaysdk.system.metadata.MdController";
  private static final long serialVersionUID = -366658978;
  
  protected MdControllerDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected MdControllerDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String STUBCLASS = "stubClass";
  public static java.lang.String STUBSOURCE = "stubSource";
  public byte[] getStubClass()
  {
    return super.getBlob(STUBCLASS);
  }
  
  public void setStubClass(byte[] bytes)
  {
    super.setBlob(STUBCLASS, bytes);
  }
  
  public boolean isStubClassWritable()
  {
    return isWritable(STUBCLASS);
  }
  
  public boolean isStubClassReadable()
  {
    return isReadable(STUBCLASS);
  }
  
  public boolean isStubClassModified()
  {
    return isModified(STUBCLASS);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeBlobMdDTO getStubClassMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeBlobMdDTO) getAttributeDTO(STUBCLASS).getAttributeMdDTO();
  }
  
  public String getStubSource()
  {
    return getValue(STUBSOURCE);
  }
  
  public void setStubSource(String value)
  {
    if(value == null)
    {
      setValue(STUBSOURCE, "");
    }
    else
    {
      setValue(STUBSOURCE, value);
    }
  }
  
  public boolean isStubSourceWritable()
  {
    return isWritable(STUBSOURCE);
  }
  
  public boolean isStubSourceReadable()
  {
    return isReadable(STUBSOURCE);
  }
  
  public boolean isStubSourceModified()
  {
    return isModified(STUBSOURCE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeClobMdDTO getStubSourceMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeClobMdDTO) getAttributeDTO(STUBSOURCE).getAttributeMdDTO();
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.MdActionDTO> getAllMdAction()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdActionDTO>) getRequest().getChildren(this.getId(), com.runwaysdk.system.ControllerActionDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.MdActionDTO> getAllMdAction(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdActionDTO>) clientRequestIF.getChildren(id, com.runwaysdk.system.ControllerActionDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.ControllerActionDTO> getAllMdActionRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.ControllerActionDTO>) getRequest().getChildRelationships(this.getId(), com.runwaysdk.system.ControllerActionDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.ControllerActionDTO> getAllMdActionRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.ControllerActionDTO>) clientRequestIF.getChildRelationships(id, com.runwaysdk.system.ControllerActionDTO.CLASS);
  }
  
  public com.runwaysdk.system.ControllerActionDTO addMdAction(com.runwaysdk.system.metadata.MdActionDTO child)
  {
    return (com.runwaysdk.system.ControllerActionDTO) getRequest().addChild(this.getId(), child.getId(), com.runwaysdk.system.ControllerActionDTO.CLASS);
  }
  
  public static com.runwaysdk.system.ControllerActionDTO addMdAction(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id, com.runwaysdk.system.metadata.MdActionDTO child)
  {
    return (com.runwaysdk.system.ControllerActionDTO) clientRequestIF.addChild(id, child.getId(), com.runwaysdk.system.ControllerActionDTO.CLASS);
  }
  
  public void removeMdAction(com.runwaysdk.system.ControllerActionDTO relationship)
  {
    getRequest().deleteChild(relationship.getId());
  }
  
  public static void removeMdAction(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.ControllerActionDTO relationship)
  {
    clientRequestIF.deleteChild(relationship.getId());
  }
  
  public void removeAllMdAction()
  {
    getRequest().deleteChildren(this.getId(), com.runwaysdk.system.ControllerActionDTO.CLASS);
  }
  
  public static void removeAllMdAction(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    clientRequestIF.deleteChildren(id, com.runwaysdk.system.ControllerActionDTO.CLASS);
  }
  
  public static com.runwaysdk.system.metadata.MdControllerDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String id)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(id);
    
    return (com.runwaysdk.system.metadata.MdControllerDTO) dto;
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
  
  public static com.runwaysdk.system.metadata.MdControllerQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.metadata.MdControllerQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.metadata.MdControllerDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdControllerDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdControllerDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdControllerDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdControllerDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdControllerDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdControllerDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
