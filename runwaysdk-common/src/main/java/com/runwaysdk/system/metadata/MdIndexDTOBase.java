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

@com.runwaysdk.business.ClassSignature(hash = -297568756)
public abstract class MdIndexDTOBase extends com.runwaysdk.system.metadata.MetadataDTO
{
  public final static String CLASS = "com.runwaysdk.system.metadata.MdIndex";
  private static final long serialVersionUID = -297568756;
  
  protected MdIndexDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected MdIndexDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public final static java.lang.String ACTIVE = "active";
  public final static java.lang.String DISPLAYLABEL = "displayLabel";
  public final static java.lang.String INDEXNAME = "indexName";
  public final static java.lang.String MDENTITY = "mdEntity";
  public final static java.lang.String UNIQUEVALUE = "uniqueValue";
  public Boolean getActive()
  {
    return com.runwaysdk.constants.MdAttributeBooleanUtil.getTypeSafeValue(getValue(ACTIVE));
  }
  
  public void setActive(Boolean value)
  {
    if(value == null)
    {
      setValue(ACTIVE, "");
    }
    else
    {
      setValue(ACTIVE, java.lang.Boolean.toString(value));
    }
  }
  
  public boolean isActiveWritable()
  {
    return isWritable(ACTIVE);
  }
  
  public boolean isActiveReadable()
  {
    return isReadable(ACTIVE);
  }
  
  public boolean isActiveModified()
  {
    return isModified(ACTIVE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeBooleanMdDTO getActiveMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeBooleanMdDTO) getAttributeDTO(ACTIVE).getAttributeMdDTO();
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
  
  public String getIndexName()
  {
    return getValue(INDEXNAME);
  }
  
  public boolean isIndexNameWritable()
  {
    return isWritable(INDEXNAME);
  }
  
  public boolean isIndexNameReadable()
  {
    return isReadable(INDEXNAME);
  }
  
  public boolean isIndexNameModified()
  {
    return isModified(INDEXNAME);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getIndexNameMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(INDEXNAME).getAttributeMdDTO();
  }
  
  public com.runwaysdk.system.metadata.MdEntityDTO getMdEntity()
  {
    if(getValue(MDENTITY) == null || getValue(MDENTITY).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.metadata.MdEntityDTO.get(getRequest(), getValue(MDENTITY));
    }
  }
  
  public String getMdEntityId()
  {
    return getValue(MDENTITY);
  }
  
  public void setMdEntity(com.runwaysdk.system.metadata.MdEntityDTO value)
  {
    if(value == null)
    {
      setValue(MDENTITY, "");
    }
    else
    {
      setValue(MDENTITY, value.getOid());
    }
  }
  
  public boolean isMdEntityWritable()
  {
    return isWritable(MDENTITY);
  }
  
  public boolean isMdEntityReadable()
  {
    return isReadable(MDENTITY);
  }
  
  public boolean isMdEntityModified()
  {
    return isModified(MDENTITY);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getMdEntityMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(MDENTITY).getAttributeMdDTO();
  }
  
  public Boolean getUniqueValue()
  {
    return com.runwaysdk.constants.MdAttributeBooleanUtil.getTypeSafeValue(getValue(UNIQUEVALUE));
  }
  
  public void setUniqueValue(Boolean value)
  {
    if(value == null)
    {
      setValue(UNIQUEVALUE, "");
    }
    else
    {
      setValue(UNIQUEVALUE, java.lang.Boolean.toString(value));
    }
  }
  
  public boolean isUniqueValueWritable()
  {
    return isWritable(UNIQUEVALUE);
  }
  
  public boolean isUniqueValueReadable()
  {
    return isReadable(UNIQUEVALUE);
  }
  
  public boolean isUniqueValueModified()
  {
    return isModified(UNIQUEVALUE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeBooleanMdDTO getUniqueValueMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeBooleanMdDTO) getAttributeDTO(UNIQUEVALUE).getAttributeMdDTO();
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.MdAttributeConcreteDTO> getAllIndexedAttribute()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdAttributeConcreteDTO>) getRequest().getChildren(this.getOid(), com.runwaysdk.system.metadata.IndexAttributeDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.MdAttributeConcreteDTO> getAllIndexedAttribute(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdAttributeConcreteDTO>) clientRequestIF.getChildren(oid, com.runwaysdk.system.metadata.IndexAttributeDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.IndexAttributeDTO> getAllIndexedAttributeRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.IndexAttributeDTO>) getRequest().getChildRelationships(this.getOid(), com.runwaysdk.system.metadata.IndexAttributeDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.IndexAttributeDTO> getAllIndexedAttributeRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.IndexAttributeDTO>) clientRequestIF.getChildRelationships(oid, com.runwaysdk.system.metadata.IndexAttributeDTO.CLASS);
  }
  
  public com.runwaysdk.system.metadata.IndexAttributeDTO addIndexedAttribute(com.runwaysdk.system.metadata.MdAttributeConcreteDTO child)
  {
    return (com.runwaysdk.system.metadata.IndexAttributeDTO) getRequest().addChild(this.getOid(), child.getOid(), com.runwaysdk.system.metadata.IndexAttributeDTO.CLASS);
  }
  
  public static com.runwaysdk.system.metadata.IndexAttributeDTO addIndexedAttribute(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid, com.runwaysdk.system.metadata.MdAttributeConcreteDTO child)
  {
    return (com.runwaysdk.system.metadata.IndexAttributeDTO) clientRequestIF.addChild(oid, child.getOid(), com.runwaysdk.system.metadata.IndexAttributeDTO.CLASS);
  }
  
  public void removeIndexedAttribute(com.runwaysdk.system.metadata.IndexAttributeDTO relationship)
  {
    getRequest().deleteChild(relationship.getOid());
  }
  
  public static void removeIndexedAttribute(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.metadata.IndexAttributeDTO relationship)
  {
    clientRequestIF.deleteChild(relationship.getOid());
  }
  
  public void removeAllIndexedAttribute()
  {
    getRequest().deleteChildren(this.getOid(), com.runwaysdk.system.metadata.IndexAttributeDTO.CLASS);
  }
  
  public static void removeAllIndexedAttribute(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    clientRequestIF.deleteChildren(oid, com.runwaysdk.system.metadata.IndexAttributeDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.MdEntityDTO> getAllEntity()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdEntityDTO>) getRequest().getParents(this.getOid(), com.runwaysdk.system.metadata.EntityIndexDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.MdEntityDTO> getAllEntity(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdEntityDTO>) clientRequestIF.getParents(oid, com.runwaysdk.system.metadata.EntityIndexDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.EntityIndexDTO> getAllEntityRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.EntityIndexDTO>) getRequest().getParentRelationships(this.getOid(), com.runwaysdk.system.metadata.EntityIndexDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.EntityIndexDTO> getAllEntityRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.EntityIndexDTO>) clientRequestIF.getParentRelationships(oid, com.runwaysdk.system.metadata.EntityIndexDTO.CLASS);
  }
  
  public com.runwaysdk.system.metadata.EntityIndexDTO addEntity(com.runwaysdk.system.metadata.MdEntityDTO parent)
  {
    return (com.runwaysdk.system.metadata.EntityIndexDTO) getRequest().addParent(parent.getOid(), this.getOid(), com.runwaysdk.system.metadata.EntityIndexDTO.CLASS);
  }
  
  public static com.runwaysdk.system.metadata.EntityIndexDTO addEntity(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid, com.runwaysdk.system.metadata.MdEntityDTO parent)
  {
    return (com.runwaysdk.system.metadata.EntityIndexDTO) clientRequestIF.addParent(parent.getOid(), oid, com.runwaysdk.system.metadata.EntityIndexDTO.CLASS);
  }
  
  public void removeEntity(com.runwaysdk.system.metadata.EntityIndexDTO relationship)
  {
    getRequest().deleteParent(relationship.getOid());
  }
  
  public static void removeEntity(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.metadata.EntityIndexDTO relationship)
  {
    clientRequestIF.deleteParent(relationship.getOid());
  }
  
  public void removeAllEntity()
  {
    getRequest().deleteParents(this.getOid(), com.runwaysdk.system.metadata.EntityIndexDTO.CLASS);
  }
  
  public static void removeAllEntity(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    clientRequestIF.deleteParents(oid, com.runwaysdk.system.metadata.EntityIndexDTO.CLASS);
  }
  
  public static com.runwaysdk.system.metadata.MdIndexDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String oid)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(oid);
    
    return (com.runwaysdk.system.metadata.MdIndexDTO) dto;
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
    getRequest().delete(this.getOid());
  }
  
  public static com.runwaysdk.system.metadata.MdIndexQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.metadata.MdIndexQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.metadata.MdIndexDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdIndexDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdIndexDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdIndexDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdIndexDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdIndexDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdIndexDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
