/**
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
 */
package com.runwaysdk.system.metadata;

@com.runwaysdk.business.ClassSignature(hash = -115528470)
public abstract class MdEntityDTOBase extends com.runwaysdk.system.metadata.MdClassDTO
{
  public final static String CLASS = "com.runwaysdk.system.metadata.MdEntity";
  private static final long serialVersionUID = -115528470;
  
  protected MdEntityDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected MdEntityDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String CACHESIZE = "cacheSize";
  public static java.lang.String ENFORCESITEMASTER = "enforceSiteMaster";
  public static java.lang.String HASDETERMINISTICIDS = "hasDeterministicIds";
  public static java.lang.String QUERYCLASS = "queryClass";
  public static java.lang.String QUERYDTOCLASS = "queryDTOclass";
  public static java.lang.String QUERYDTOSOURCE = "queryDTOsource";
  public static java.lang.String QUERYSOURCE = "querySource";
  public static java.lang.String TABLENAME = "tableName";
  public Integer getCacheSize()
  {
    return com.runwaysdk.constants.MdAttributeIntegerUtil.getTypeSafeValue(getValue(CACHESIZE));
  }
  
  public void setCacheSize(Integer value)
  {
    if(value == null)
    {
      setValue(CACHESIZE, "");
    }
    else
    {
      setValue(CACHESIZE, java.lang.Integer.toString(value));
    }
  }
  
  public boolean isCacheSizeWritable()
  {
    return isWritable(CACHESIZE);
  }
  
  public boolean isCacheSizeReadable()
  {
    return isReadable(CACHESIZE);
  }
  
  public boolean isCacheSizeModified()
  {
    return isModified(CACHESIZE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeNumberMdDTO getCacheSizeMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeNumberMdDTO) getAttributeDTO(CACHESIZE).getAttributeMdDTO();
  }
  
  public Boolean getEnforceSiteMaster()
  {
    return com.runwaysdk.constants.MdAttributeBooleanUtil.getTypeSafeValue(getValue(ENFORCESITEMASTER));
  }
  
  public void setEnforceSiteMaster(Boolean value)
  {
    if(value == null)
    {
      setValue(ENFORCESITEMASTER, "");
    }
    else
    {
      setValue(ENFORCESITEMASTER, java.lang.Boolean.toString(value));
    }
  }
  
  public boolean isEnforceSiteMasterWritable()
  {
    return isWritable(ENFORCESITEMASTER);
  }
  
  public boolean isEnforceSiteMasterReadable()
  {
    return isReadable(ENFORCESITEMASTER);
  }
  
  public boolean isEnforceSiteMasterModified()
  {
    return isModified(ENFORCESITEMASTER);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeBooleanMdDTO getEnforceSiteMasterMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeBooleanMdDTO) getAttributeDTO(ENFORCESITEMASTER).getAttributeMdDTO();
  }
  
  public Boolean getHasDeterministicIds()
  {
    return com.runwaysdk.constants.MdAttributeBooleanUtil.getTypeSafeValue(getValue(HASDETERMINISTICIDS));
  }
  
  public void setHasDeterministicIds(Boolean value)
  {
    if(value == null)
    {
      setValue(HASDETERMINISTICIDS, "");
    }
    else
    {
      setValue(HASDETERMINISTICIDS, java.lang.Boolean.toString(value));
    }
  }
  
  public boolean isHasDeterministicIdsWritable()
  {
    return isWritable(HASDETERMINISTICIDS);
  }
  
  public boolean isHasDeterministicIdsReadable()
  {
    return isReadable(HASDETERMINISTICIDS);
  }
  
  public boolean isHasDeterministicIdsModified()
  {
    return isModified(HASDETERMINISTICIDS);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeBooleanMdDTO getHasDeterministicIdsMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeBooleanMdDTO) getAttributeDTO(HASDETERMINISTICIDS).getAttributeMdDTO();
  }
  
  public byte[] getQueryClass()
  {
    return super.getBlob(QUERYCLASS);
  }
  
  public boolean isQueryClassWritable()
  {
    return isWritable(QUERYCLASS);
  }
  
  public boolean isQueryClassReadable()
  {
    return isReadable(QUERYCLASS);
  }
  
  public boolean isQueryClassModified()
  {
    return isModified(QUERYCLASS);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeBlobMdDTO getQueryClassMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeBlobMdDTO) getAttributeDTO(QUERYCLASS).getAttributeMdDTO();
  }
  
  public byte[] getQueryDTOclass()
  {
    return super.getBlob(QUERYDTOCLASS);
  }
  
  public boolean isQueryDTOclassWritable()
  {
    return isWritable(QUERYDTOCLASS);
  }
  
  public boolean isQueryDTOclassReadable()
  {
    return isReadable(QUERYDTOCLASS);
  }
  
  public boolean isQueryDTOclassModified()
  {
    return isModified(QUERYDTOCLASS);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeBlobMdDTO getQueryDTOclassMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeBlobMdDTO) getAttributeDTO(QUERYDTOCLASS).getAttributeMdDTO();
  }
  
  public String getQueryDTOsource()
  {
    return getValue(QUERYDTOSOURCE);
  }
  
  public boolean isQueryDTOsourceWritable()
  {
    return isWritable(QUERYDTOSOURCE);
  }
  
  public boolean isQueryDTOsourceReadable()
  {
    return isReadable(QUERYDTOSOURCE);
  }
  
  public boolean isQueryDTOsourceModified()
  {
    return isModified(QUERYDTOSOURCE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeClobMdDTO getQueryDTOsourceMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeClobMdDTO) getAttributeDTO(QUERYDTOSOURCE).getAttributeMdDTO();
  }
  
  public String getQuerySource()
  {
    return getValue(QUERYSOURCE);
  }
  
  public boolean isQuerySourceWritable()
  {
    return isWritable(QUERYSOURCE);
  }
  
  public boolean isQuerySourceReadable()
  {
    return isReadable(QUERYSOURCE);
  }
  
  public boolean isQuerySourceModified()
  {
    return isModified(QUERYSOURCE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeClobMdDTO getQuerySourceMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeClobMdDTO) getAttributeDTO(QUERYSOURCE).getAttributeMdDTO();
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
  public java.util.List<? extends com.runwaysdk.system.metadata.MdIndexDTO> getAllIndex()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdIndexDTO>) getRequest().getChildren(this.getId(), com.runwaysdk.system.metadata.EntityIndexDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.MdIndexDTO> getAllIndex(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdIndexDTO>) clientRequestIF.getChildren(id, com.runwaysdk.system.metadata.EntityIndexDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.EntityIndexDTO> getAllIndexRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.EntityIndexDTO>) getRequest().getChildRelationships(this.getId(), com.runwaysdk.system.metadata.EntityIndexDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.EntityIndexDTO> getAllIndexRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.EntityIndexDTO>) clientRequestIF.getChildRelationships(id, com.runwaysdk.system.metadata.EntityIndexDTO.CLASS);
  }
  
  public com.runwaysdk.system.metadata.EntityIndexDTO addIndex(com.runwaysdk.system.metadata.MdIndexDTO child)
  {
    return (com.runwaysdk.system.metadata.EntityIndexDTO) getRequest().addChild(this.getId(), child.getId(), com.runwaysdk.system.metadata.EntityIndexDTO.CLASS);
  }
  
  public static com.runwaysdk.system.metadata.EntityIndexDTO addIndex(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id, com.runwaysdk.system.metadata.MdIndexDTO child)
  {
    return (com.runwaysdk.system.metadata.EntityIndexDTO) clientRequestIF.addChild(id, child.getId(), com.runwaysdk.system.metadata.EntityIndexDTO.CLASS);
  }
  
  public void removeIndex(com.runwaysdk.system.metadata.EntityIndexDTO relationship)
  {
    getRequest().deleteChild(relationship.getId());
  }
  
  public static void removeIndex(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.metadata.EntityIndexDTO relationship)
  {
    clientRequestIF.deleteChild(relationship.getId());
  }
  
  public void removeAllIndex()
  {
    getRequest().deleteChildren(this.getId(), com.runwaysdk.system.metadata.EntityIndexDTO.CLASS);
  }
  
  public static void removeAllIndex(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    clientRequestIF.deleteChildren(id, com.runwaysdk.system.metadata.EntityIndexDTO.CLASS);
  }
  
  public static com.runwaysdk.system.metadata.MdEntityDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String id)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(id);
    
    return (com.runwaysdk.system.metadata.MdEntityDTO) dto;
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
  
  public static com.runwaysdk.system.metadata.MdEntityQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.metadata.MdEntityQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.metadata.MdEntityDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdEntityDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdEntityDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdEntityDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdEntityDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdEntityDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdEntityDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
