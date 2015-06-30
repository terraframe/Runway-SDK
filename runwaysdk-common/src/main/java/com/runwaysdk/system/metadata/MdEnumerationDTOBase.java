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

@com.runwaysdk.business.ClassSignature(hash = -203146929)
public abstract class MdEnumerationDTOBase extends com.runwaysdk.system.metadata.MdTypeDTO
{
  public final static String CLASS = "com.runwaysdk.system.metadata.MdEnumeration";
  private static final long serialVersionUID = -203146929;
  
  protected MdEnumerationDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected MdEnumerationDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String INCLUDEALL = "includeAll";
  public static java.lang.String MASTERMDBUSINESS = "masterMdBusiness";
  public static java.lang.String TABLENAME = "tableName";
  public Boolean getIncludeAll()
  {
    return com.runwaysdk.constants.MdAttributeBooleanUtil.getTypeSafeValue(getValue(INCLUDEALL));
  }
  
  public void setIncludeAll(Boolean value)
  {
    if(value == null)
    {
      setValue(INCLUDEALL, "");
    }
    else
    {
      setValue(INCLUDEALL, java.lang.Boolean.toString(value));
    }
  }
  
  public boolean isIncludeAllWritable()
  {
    return isWritable(INCLUDEALL);
  }
  
  public boolean isIncludeAllReadable()
  {
    return isReadable(INCLUDEALL);
  }
  
  public boolean isIncludeAllModified()
  {
    return isModified(INCLUDEALL);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeBooleanMdDTO getIncludeAllMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeBooleanMdDTO) getAttributeDTO(INCLUDEALL).getAttributeMdDTO();
  }
  
  public com.runwaysdk.system.metadata.MdBusinessDTO getMasterMdBusiness()
  {
    if(getValue(MASTERMDBUSINESS) == null || getValue(MASTERMDBUSINESS).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.metadata.MdBusinessDTO.get(getRequest(), getValue(MASTERMDBUSINESS));
    }
  }
  
  public String getMasterMdBusinessId()
  {
    return getValue(MASTERMDBUSINESS);
  }
  
  public void setMasterMdBusiness(com.runwaysdk.system.metadata.MdBusinessDTO value)
  {
    if(value == null)
    {
      setValue(MASTERMDBUSINESS, "");
    }
    else
    {
      setValue(MASTERMDBUSINESS, value.getId());
    }
  }
  
  public boolean isMasterMdBusinessWritable()
  {
    return isWritable(MASTERMDBUSINESS);
  }
  
  public boolean isMasterMdBusinessReadable()
  {
    return isReadable(MASTERMDBUSINESS);
  }
  
  public boolean isMasterMdBusinessModified()
  {
    return isModified(MASTERMDBUSINESS);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getMasterMdBusinessMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(MASTERMDBUSINESS).getAttributeMdDTO();
  }
  
  public String getTableName()
  {
    return getValue(TABLENAME);
  }
  
  public void setTableName(String value)
  {
    if(value == null)
    {
      setValue(TABLENAME, "");
    }
    else
    {
      setValue(TABLENAME, value);
    }
  }
  
  public boolean isTableNameWritable()
  {
    return isWritable(TABLENAME);
  }
  
  public boolean isTableNameReadable()
  {
    return isReadable(TABLENAME);
  }
  
  public boolean isTableNameModified()
  {
    return isModified(TABLENAME);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getTableNameMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(TABLENAME).getAttributeMdDTO();
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.EnumerationMasterDTO> getAllItem()
  {
    return (java.util.List<? extends com.runwaysdk.system.EnumerationMasterDTO>) getRequest().getChildren(this.getId(), com.runwaysdk.system.metadata.EnumerationAttributeItemDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.EnumerationMasterDTO> getAllItem(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.EnumerationMasterDTO>) clientRequestIF.getChildren(id, com.runwaysdk.system.metadata.EnumerationAttributeItemDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.EnumerationAttributeItemDTO> getAllItemRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.EnumerationAttributeItemDTO>) getRequest().getChildRelationships(this.getId(), com.runwaysdk.system.metadata.EnumerationAttributeItemDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.EnumerationAttributeItemDTO> getAllItemRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.EnumerationAttributeItemDTO>) clientRequestIF.getChildRelationships(id, com.runwaysdk.system.metadata.EnumerationAttributeItemDTO.CLASS);
  }
  
  public com.runwaysdk.system.metadata.EnumerationAttributeItemDTO addItem(com.runwaysdk.system.EnumerationMasterDTO child)
  {
    return (com.runwaysdk.system.metadata.EnumerationAttributeItemDTO) getRequest().addChild(this.getId(), child.getId(), com.runwaysdk.system.metadata.EnumerationAttributeItemDTO.CLASS);
  }
  
  public static com.runwaysdk.system.metadata.EnumerationAttributeItemDTO addItem(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id, com.runwaysdk.system.EnumerationMasterDTO child)
  {
    return (com.runwaysdk.system.metadata.EnumerationAttributeItemDTO) clientRequestIF.addChild(id, child.getId(), com.runwaysdk.system.metadata.EnumerationAttributeItemDTO.CLASS);
  }
  
  public void removeItem(com.runwaysdk.system.metadata.EnumerationAttributeItemDTO relationship)
  {
    getRequest().deleteChild(relationship.getId());
  }
  
  public static void removeItem(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.metadata.EnumerationAttributeItemDTO relationship)
  {
    clientRequestIF.deleteChild(relationship.getId());
  }
  
  public void removeAllItem()
  {
    getRequest().deleteChildren(this.getId(), com.runwaysdk.system.metadata.EnumerationAttributeItemDTO.CLASS);
  }
  
  public static void removeAllItem(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    clientRequestIF.deleteChildren(id, com.runwaysdk.system.metadata.EnumerationAttributeItemDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.MdBusinessDTO> getAllMasterClass()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdBusinessDTO>) getRequest().getParents(this.getId(), com.runwaysdk.system.metadata.EnumerationAttributeDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.MdBusinessDTO> getAllMasterClass(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdBusinessDTO>) clientRequestIF.getParents(id, com.runwaysdk.system.metadata.EnumerationAttributeDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.EnumerationAttributeDTO> getAllMasterClassRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.EnumerationAttributeDTO>) getRequest().getParentRelationships(this.getId(), com.runwaysdk.system.metadata.EnumerationAttributeDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.EnumerationAttributeDTO> getAllMasterClassRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.EnumerationAttributeDTO>) clientRequestIF.getParentRelationships(id, com.runwaysdk.system.metadata.EnumerationAttributeDTO.CLASS);
  }
  
  public com.runwaysdk.system.metadata.EnumerationAttributeDTO addMasterClass(com.runwaysdk.system.metadata.MdBusinessDTO parent)
  {
    return (com.runwaysdk.system.metadata.EnumerationAttributeDTO) getRequest().addParent(parent.getId(), this.getId(), com.runwaysdk.system.metadata.EnumerationAttributeDTO.CLASS);
  }
  
  public static com.runwaysdk.system.metadata.EnumerationAttributeDTO addMasterClass(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id, com.runwaysdk.system.metadata.MdBusinessDTO parent)
  {
    return (com.runwaysdk.system.metadata.EnumerationAttributeDTO) clientRequestIF.addParent(parent.getId(), id, com.runwaysdk.system.metadata.EnumerationAttributeDTO.CLASS);
  }
  
  public void removeMasterClass(com.runwaysdk.system.metadata.EnumerationAttributeDTO relationship)
  {
    getRequest().deleteParent(relationship.getId());
  }
  
  public static void removeMasterClass(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.metadata.EnumerationAttributeDTO relationship)
  {
    clientRequestIF.deleteParent(relationship.getId());
  }
  
  public void removeAllMasterClass()
  {
    getRequest().deleteParents(this.getId(), com.runwaysdk.system.metadata.EnumerationAttributeDTO.CLASS);
  }
  
  public static void removeAllMasterClass(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    clientRequestIF.deleteParents(id, com.runwaysdk.system.metadata.EnumerationAttributeDTO.CLASS);
  }
  
  public static com.runwaysdk.system.metadata.MdEnumerationDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String id)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(id);
    
    return (com.runwaysdk.system.metadata.MdEnumerationDTO) dto;
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
  
  public static com.runwaysdk.system.metadata.MdEnumerationQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.metadata.MdEnumerationQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.metadata.MdEnumerationDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdEnumerationDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdEnumerationDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdEnumerationDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdEnumerationDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdEnumerationDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdEnumerationDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
