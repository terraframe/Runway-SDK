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

@com.runwaysdk.business.ClassSignature(hash = -1347412162)
public abstract class MdActionDTOBase extends com.runwaysdk.system.metadata.MetadataDTO
{
  public final static String CLASS = "com.runwaysdk.system.metadata.MdAction";
  private static final long serialVersionUID = -1347412162;
  
  protected MdActionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected MdActionDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String ACTIONNAME = "actionName";
  public static java.lang.String DISPLAYLABEL = "displayLabel";
  public static java.lang.String ENCLOSINGMDCONTROLLER = "enclosingMdController";
  public static java.lang.String ISPOST = "isPost";
  public static java.lang.String ISQUERY = "isQuery";
  public String getActionName()
  {
    return getValue(ACTIONNAME);
  }
  
  public void setActionName(String value)
  {
    if(value == null)
    {
      setValue(ACTIONNAME, "");
    }
    else
    {
      setValue(ACTIONNAME, value);
    }
  }
  
  public boolean isActionNameWritable()
  {
    return isWritable(ACTIONNAME);
  }
  
  public boolean isActionNameReadable()
  {
    return isReadable(ACTIONNAME);
  }
  
  public boolean isActionNameModified()
  {
    return isModified(ACTIONNAME);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getActionNameMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(ACTIONNAME).getAttributeMdDTO();
  }
  
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
  
  public com.runwaysdk.system.metadata.MdControllerDTO getEnclosingMdController()
  {
    if(getValue(ENCLOSINGMDCONTROLLER) == null || getValue(ENCLOSINGMDCONTROLLER).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.metadata.MdControllerDTO.get(getRequest(), getValue(ENCLOSINGMDCONTROLLER));
    }
  }
  
  public String getEnclosingMdControllerId()
  {
    return getValue(ENCLOSINGMDCONTROLLER);
  }
  
  public void setEnclosingMdController(com.runwaysdk.system.metadata.MdControllerDTO value)
  {
    if(value == null)
    {
      setValue(ENCLOSINGMDCONTROLLER, "");
    }
    else
    {
      setValue(ENCLOSINGMDCONTROLLER, value.getId());
    }
  }
  
  public boolean isEnclosingMdControllerWritable()
  {
    return isWritable(ENCLOSINGMDCONTROLLER);
  }
  
  public boolean isEnclosingMdControllerReadable()
  {
    return isReadable(ENCLOSINGMDCONTROLLER);
  }
  
  public boolean isEnclosingMdControllerModified()
  {
    return isModified(ENCLOSINGMDCONTROLLER);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getEnclosingMdControllerMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(ENCLOSINGMDCONTROLLER).getAttributeMdDTO();
  }
  
  public Boolean getIsPost()
  {
    return com.runwaysdk.constants.MdAttributeBooleanUtil.getTypeSafeValue(getValue(ISPOST));
  }
  
  public void setIsPost(Boolean value)
  {
    if(value == null)
    {
      setValue(ISPOST, "");
    }
    else
    {
      setValue(ISPOST, java.lang.Boolean.toString(value));
    }
  }
  
  public boolean isIsPostWritable()
  {
    return isWritable(ISPOST);
  }
  
  public boolean isIsPostReadable()
  {
    return isReadable(ISPOST);
  }
  
  public boolean isIsPostModified()
  {
    return isModified(ISPOST);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeBooleanMdDTO getIsPostMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeBooleanMdDTO) getAttributeDTO(ISPOST).getAttributeMdDTO();
  }
  
  public Boolean getIsQuery()
  {
    return com.runwaysdk.constants.MdAttributeBooleanUtil.getTypeSafeValue(getValue(ISQUERY));
  }
  
  public void setIsQuery(Boolean value)
  {
    if(value == null)
    {
      setValue(ISQUERY, "");
    }
    else
    {
      setValue(ISQUERY, java.lang.Boolean.toString(value));
    }
  }
  
  public boolean isIsQueryWritable()
  {
    return isWritable(ISQUERY);
  }
  
  public boolean isIsQueryReadable()
  {
    return isReadable(ISQUERY);
  }
  
  public boolean isIsQueryModified()
  {
    return isModified(ISQUERY);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeBooleanMdDTO getIsQueryMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeBooleanMdDTO) getAttributeDTO(ISQUERY).getAttributeMdDTO();
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.MdControllerDTO> getAllMdContoller()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdControllerDTO>) getRequest().getParents(this.getId(), com.runwaysdk.system.ControllerActionDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.MdControllerDTO> getAllMdContoller(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdControllerDTO>) clientRequestIF.getParents(id, com.runwaysdk.system.ControllerActionDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.ControllerActionDTO> getAllMdContollerRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.ControllerActionDTO>) getRequest().getParentRelationships(this.getId(), com.runwaysdk.system.ControllerActionDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.ControllerActionDTO> getAllMdContollerRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.ControllerActionDTO>) clientRequestIF.getParentRelationships(id, com.runwaysdk.system.ControllerActionDTO.CLASS);
  }
  
  public com.runwaysdk.system.ControllerActionDTO addMdContoller(com.runwaysdk.system.metadata.MdControllerDTO parent)
  {
    return (com.runwaysdk.system.ControllerActionDTO) getRequest().addParent(parent.getId(), this.getId(), com.runwaysdk.system.ControllerActionDTO.CLASS);
  }
  
  public static com.runwaysdk.system.ControllerActionDTO addMdContoller(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id, com.runwaysdk.system.metadata.MdControllerDTO parent)
  {
    return (com.runwaysdk.system.ControllerActionDTO) clientRequestIF.addParent(parent.getId(), id, com.runwaysdk.system.ControllerActionDTO.CLASS);
  }
  
  public void removeMdContoller(com.runwaysdk.system.ControllerActionDTO relationship)
  {
    getRequest().deleteParent(relationship.getId());
  }
  
  public static void removeMdContoller(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.ControllerActionDTO relationship)
  {
    clientRequestIF.deleteParent(relationship.getId());
  }
  
  public void removeAllMdContoller()
  {
    getRequest().deleteParents(this.getId(), com.runwaysdk.system.ControllerActionDTO.CLASS);
  }
  
  public static void removeAllMdContoller(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    clientRequestIF.deleteParents(id, com.runwaysdk.system.ControllerActionDTO.CLASS);
  }
  
  public static com.runwaysdk.system.metadata.MdActionDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String id)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(id);
    
    return (com.runwaysdk.system.metadata.MdActionDTO) dto;
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
  
  public static com.runwaysdk.system.metadata.MdActionQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.metadata.MdActionQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.metadata.MdActionDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdActionDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdActionDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdActionDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdActionDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdActionDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdActionDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
