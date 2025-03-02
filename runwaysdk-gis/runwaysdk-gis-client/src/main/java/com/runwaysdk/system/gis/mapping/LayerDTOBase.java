/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK GIS(tm).
 *
 * Runway SDK GIS(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway SDK GIS(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK GIS(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.system.gis.mapping;

@com.runwaysdk.business.ClassSignature(hash = -1812646154)
public abstract class LayerDTOBase extends com.runwaysdk.business.BusinessDTO
{
  public final static String CLASS = "com.runwaysdk.system.gis.mapping.Layer";
  private static final long serialVersionUID = -1812646154;
  
  protected LayerDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected LayerDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public final static java.lang.String CREATEDATE = "createDate";
  public final static java.lang.String CREATEDBY = "createdBy";
  public final static java.lang.String DEFAULTSTYLE = "defaultStyle";
  public final static java.lang.String ENTITYDOMAIN = "entityDomain";
  public final static java.lang.String FORCEREFRESH = "forceRefresh";
  public final static java.lang.String OID = "oid";
  public final static java.lang.String KEYNAME = "keyName";
  public final static java.lang.String LASTUPDATEDATE = "lastUpdateDate";
  public final static java.lang.String LASTUPDATEDBY = "lastUpdatedBy";
  public final static java.lang.String LAYERNAME = "layerName";
  public final static java.lang.String LOCKEDBY = "lockedBy";
  public final static java.lang.String OWNER = "owner";
  public final static java.lang.String PUBLISHED = "published";
  public final static java.lang.String SEQ = "seq";
  public final static java.lang.String SITEMASTER = "siteMaster";
  public final static java.lang.String SQLBUILDER = "sqlBuilder";
  public final static java.lang.String TYPE = "type";
  public java.util.Date getCreateDate()
  {
    return com.runwaysdk.constants.MdAttributeDateTimeUtil.getTypeSafeValue(getValue(CREATEDATE));
  }
  
  public boolean isCreateDateWritable()
  {
    return isWritable(CREATEDATE);
  }
  
  public boolean isCreateDateReadable()
  {
    return isReadable(CREATEDATE);
  }
  
  public boolean isCreateDateModified()
  {
    return isModified(CREATEDATE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeDateTimeMdDTO getCreateDateMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeDateTimeMdDTO) getAttributeDTO(CREATEDATE).getAttributeMdDTO();
  }
  
  public com.runwaysdk.system.SingleActorDTO getCreatedBy()
  {
    if(getValue(CREATEDBY) == null || getValue(CREATEDBY).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.SingleActorDTO.get(getRequest(), getValue(CREATEDBY));
    }
  }
  
  public String getCreatedById()
  {
    return getValue(CREATEDBY);
  }
  
  public boolean isCreatedByWritable()
  {
    return isWritable(CREATEDBY);
  }
  
  public boolean isCreatedByReadable()
  {
    return isReadable(CREATEDBY);
  }
  
  public boolean isCreatedByModified()
  {
    return isModified(CREATEDBY);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getCreatedByMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(CREATEDBY).getAttributeMdDTO();
  }
  
  public com.runwaysdk.system.gis.mapping.LayerStyleDTO getDefaultStyle()
  {
    if(getValue(DEFAULTSTYLE) == null || getValue(DEFAULTSTYLE).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.gis.mapping.LayerStyleDTO.get(getRequest(), getValue(DEFAULTSTYLE));
    }
  }
  
  public String getDefaultStyleId()
  {
    return getValue(DEFAULTSTYLE);
  }
  
  public void setDefaultStyle(com.runwaysdk.system.gis.mapping.LayerStyleDTO value)
  {
    if(value == null)
    {
      setValue(DEFAULTSTYLE, "");
    }
    else
    {
      setValue(DEFAULTSTYLE, value.getOid());
    }
  }
  
  public boolean isDefaultStyleWritable()
  {
    return isWritable(DEFAULTSTYLE);
  }
  
  public boolean isDefaultStyleReadable()
  {
    return isReadable(DEFAULTSTYLE);
  }
  
  public boolean isDefaultStyleModified()
  {
    return isModified(DEFAULTSTYLE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getDefaultStyleMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(DEFAULTSTYLE).getAttributeMdDTO();
  }
  
  public com.runwaysdk.system.metadata.MdDomainDTO getEntityDomain()
  {
    if(getValue(ENTITYDOMAIN) == null || getValue(ENTITYDOMAIN).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.metadata.MdDomainDTO.get(getRequest(), getValue(ENTITYDOMAIN));
    }
  }
  
  public String getEntityDomainId()
  {
    return getValue(ENTITYDOMAIN);
  }
  
  public void setEntityDomain(com.runwaysdk.system.metadata.MdDomainDTO value)
  {
    if(value == null)
    {
      setValue(ENTITYDOMAIN, "");
    }
    else
    {
      setValue(ENTITYDOMAIN, value.getOid());
    }
  }
  
  public boolean isEntityDomainWritable()
  {
    return isWritable(ENTITYDOMAIN);
  }
  
  public boolean isEntityDomainReadable()
  {
    return isReadable(ENTITYDOMAIN);
  }
  
  public boolean isEntityDomainModified()
  {
    return isModified(ENTITYDOMAIN);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getEntityDomainMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(ENTITYDOMAIN).getAttributeMdDTO();
  }
  
  public Boolean getForceRefresh()
  {
    return com.runwaysdk.constants.MdAttributeBooleanUtil.getTypeSafeValue(getValue(FORCEREFRESH));
  }
  
  public void setForceRefresh(Boolean value)
  {
    if(value == null)
    {
      setValue(FORCEREFRESH, "");
    }
    else
    {
      setValue(FORCEREFRESH, java.lang.Boolean.toString(value));
    }
  }
  
  public boolean isForceRefreshWritable()
  {
    return isWritable(FORCEREFRESH);
  }
  
  public boolean isForceRefreshReadable()
  {
    return isReadable(FORCEREFRESH);
  }
  
  public boolean isForceRefreshModified()
  {
    return isModified(FORCEREFRESH);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeBooleanMdDTO getForceRefreshMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeBooleanMdDTO) getAttributeDTO(FORCEREFRESH).getAttributeMdDTO();
  }
  
  public String getKeyName()
  {
    return getValue(KEYNAME);
  }
  
  public void setKeyName(String value)
  {
    if(value == null)
    {
      setValue(KEYNAME, "");
    }
    else
    {
      setValue(KEYNAME, value);
    }
  }
  
  public boolean isKeyNameWritable()
  {
    return isWritable(KEYNAME);
  }
  
  public boolean isKeyNameReadable()
  {
    return isReadable(KEYNAME);
  }
  
  public boolean isKeyNameModified()
  {
    return isModified(KEYNAME);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getKeyNameMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(KEYNAME).getAttributeMdDTO();
  }
  
  public java.util.Date getLastUpdateDate()
  {
    return com.runwaysdk.constants.MdAttributeDateTimeUtil.getTypeSafeValue(getValue(LASTUPDATEDATE));
  }
  
  public boolean isLastUpdateDateWritable()
  {
    return isWritable(LASTUPDATEDATE);
  }
  
  public boolean isLastUpdateDateReadable()
  {
    return isReadable(LASTUPDATEDATE);
  }
  
  public boolean isLastUpdateDateModified()
  {
    return isModified(LASTUPDATEDATE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeDateTimeMdDTO getLastUpdateDateMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeDateTimeMdDTO) getAttributeDTO(LASTUPDATEDATE).getAttributeMdDTO();
  }
  
  public com.runwaysdk.system.SingleActorDTO getLastUpdatedBy()
  {
    if(getValue(LASTUPDATEDBY) == null || getValue(LASTUPDATEDBY).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.SingleActorDTO.get(getRequest(), getValue(LASTUPDATEDBY));
    }
  }
  
  public String getLastUpdatedById()
  {
    return getValue(LASTUPDATEDBY);
  }
  
  public boolean isLastUpdatedByWritable()
  {
    return isWritable(LASTUPDATEDBY);
  }
  
  public boolean isLastUpdatedByReadable()
  {
    return isReadable(LASTUPDATEDBY);
  }
  
  public boolean isLastUpdatedByModified()
  {
    return isModified(LASTUPDATEDBY);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getLastUpdatedByMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(LASTUPDATEDBY).getAttributeMdDTO();
  }
  
  public com.runwaysdk.system.gis.mapping.LayerLayerNameDTO getLayerName()
  {
    return (com.runwaysdk.system.gis.mapping.LayerLayerNameDTO) this.getAttributeStructDTO(LAYERNAME).getStructDTO();
  }
  
  public boolean isLayerNameWritable()
  {
    return isWritable(LAYERNAME);
  }
  
  public boolean isLayerNameReadable()
  {
    return isReadable(LAYERNAME);
  }
  
  public boolean isLayerNameModified()
  {
    return isModified(LAYERNAME);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeLocalCharacterMdDTO getLayerNameMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeLocalCharacterMdDTO) getAttributeDTO(LAYERNAME).getAttributeMdDTO();
  }
  
  public com.runwaysdk.system.UsersDTO getLockedBy()
  {
    if(getValue(LOCKEDBY) == null || getValue(LOCKEDBY).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.UsersDTO.get(getRequest(), getValue(LOCKEDBY));
    }
  }
  
  public String getLockedById()
  {
    return getValue(LOCKEDBY);
  }
  
  public boolean isLockedByWritable()
  {
    return isWritable(LOCKEDBY);
  }
  
  public boolean isLockedByReadable()
  {
    return isReadable(LOCKEDBY);
  }
  
  public boolean isLockedByModified()
  {
    return isModified(LOCKEDBY);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getLockedByMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(LOCKEDBY).getAttributeMdDTO();
  }
  
  public com.runwaysdk.system.ActorDTO getOwner()
  {
    if(getValue(OWNER) == null || getValue(OWNER).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.ActorDTO.get(getRequest(), getValue(OWNER));
    }
  }
  
  public String getOwnerId()
  {
    return getValue(OWNER);
  }
  
  public void setOwner(com.runwaysdk.system.ActorDTO value)
  {
    if(value == null)
    {
      setValue(OWNER, "");
    }
    else
    {
      setValue(OWNER, value.getOid());
    }
  }
  
  public boolean isOwnerWritable()
  {
    return isWritable(OWNER);
  }
  
  public boolean isOwnerReadable()
  {
    return isReadable(OWNER);
  }
  
  public boolean isOwnerModified()
  {
    return isModified(OWNER);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getOwnerMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(OWNER).getAttributeMdDTO();
  }
  
  public Boolean getPublished()
  {
    return com.runwaysdk.constants.MdAttributeBooleanUtil.getTypeSafeValue(getValue(PUBLISHED));
  }
  
  public void setPublished(Boolean value)
  {
    if(value == null)
    {
      setValue(PUBLISHED, "");
    }
    else
    {
      setValue(PUBLISHED, java.lang.Boolean.toString(value));
    }
  }
  
  public boolean isPublishedWritable()
  {
    return isWritable(PUBLISHED);
  }
  
  public boolean isPublishedReadable()
  {
    return isReadable(PUBLISHED);
  }
  
  public boolean isPublishedModified()
  {
    return isModified(PUBLISHED);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeBooleanMdDTO getPublishedMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeBooleanMdDTO) getAttributeDTO(PUBLISHED).getAttributeMdDTO();
  }
  
  public Long getSeq()
  {
    return com.runwaysdk.constants.MdAttributeLongUtil.getTypeSafeValue(getValue(SEQ));
  }
  
  public boolean isSeqWritable()
  {
    return isWritable(SEQ);
  }
  
  public boolean isSeqReadable()
  {
    return isReadable(SEQ);
  }
  
  public boolean isSeqModified()
  {
    return isModified(SEQ);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeNumberMdDTO getSeqMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeNumberMdDTO) getAttributeDTO(SEQ).getAttributeMdDTO();
  }
  
  public String getSiteMaster()
  {
    return getValue(SITEMASTER);
  }
  
  public boolean isSiteMasterWritable()
  {
    return isWritable(SITEMASTER);
  }
  
  public boolean isSiteMasterReadable()
  {
    return isReadable(SITEMASTER);
  }
  
  public boolean isSiteMasterModified()
  {
    return isModified(SITEMASTER);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getSiteMasterMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(SITEMASTER).getAttributeMdDTO();
  }
  
  public com.runwaysdk.system.gis.mapping.LayerSQLBuilderDTO getSqlBuilder()
  {
    if(getValue(SQLBUILDER) == null || getValue(SQLBUILDER).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.gis.mapping.LayerSQLBuilderDTO.get(getRequest(), getValue(SQLBUILDER));
    }
  }
  
  public String getSqlBuilderId()
  {
    return getValue(SQLBUILDER);
  }
  
  public void setSqlBuilder(com.runwaysdk.system.gis.mapping.LayerSQLBuilderDTO value)
  {
    if(value == null)
    {
      setValue(SQLBUILDER, "");
    }
    else
    {
      setValue(SQLBUILDER, value.getOid());
    }
  }
  
  public boolean isSqlBuilderWritable()
  {
    return isWritable(SQLBUILDER);
  }
  
  public boolean isSqlBuilderReadable()
  {
    return isReadable(SQLBUILDER);
  }
  
  public boolean isSqlBuilderModified()
  {
    return isModified(SQLBUILDER);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getSqlBuilderMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(SQLBUILDER).getAttributeMdDTO();
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.gis.mapping.ThematicAttributeDTO> getAllHasThematicAttribute()
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.mapping.ThematicAttributeDTO>) getRequest().getChildren(this.getOid(), com.runwaysdk.system.gis.mapping.HasThematicAttributeDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.gis.mapping.ThematicAttributeDTO> getAllHasThematicAttribute(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.mapping.ThematicAttributeDTO>) clientRequestIF.getChildren(oid, com.runwaysdk.system.gis.mapping.HasThematicAttributeDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.gis.mapping.HasThematicAttributeDTO> getAllHasThematicAttributeRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.mapping.HasThematicAttributeDTO>) getRequest().getChildRelationships(this.getOid(), com.runwaysdk.system.gis.mapping.HasThematicAttributeDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.gis.mapping.HasThematicAttributeDTO> getAllHasThematicAttributeRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.mapping.HasThematicAttributeDTO>) clientRequestIF.getChildRelationships(oid, com.runwaysdk.system.gis.mapping.HasThematicAttributeDTO.CLASS);
  }
  
  public com.runwaysdk.system.gis.mapping.HasThematicAttributeDTO addHasThematicAttribute(com.runwaysdk.system.gis.mapping.ThematicAttributeDTO child)
  {
    return (com.runwaysdk.system.gis.mapping.HasThematicAttributeDTO) getRequest().addChild(this.getOid(), child.getOid(), com.runwaysdk.system.gis.mapping.HasThematicAttributeDTO.CLASS);
  }
  
  public static com.runwaysdk.system.gis.mapping.HasThematicAttributeDTO addHasThematicAttribute(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid, com.runwaysdk.system.gis.mapping.ThematicAttributeDTO child)
  {
    return (com.runwaysdk.system.gis.mapping.HasThematicAttributeDTO) clientRequestIF.addChild(oid, child.getOid(), com.runwaysdk.system.gis.mapping.HasThematicAttributeDTO.CLASS);
  }
  
  public void removeHasThematicAttribute(com.runwaysdk.system.gis.mapping.HasThematicAttributeDTO relationship)
  {
    getRequest().deleteChild(relationship.getOid());
  }
  
  public static void removeHasThematicAttribute(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.gis.mapping.HasThematicAttributeDTO relationship)
  {
    clientRequestIF.deleteChild(relationship.getOid());
  }
  
  public void removeAllHasThematicAttribute()
  {
    getRequest().deleteChildren(this.getOid(), com.runwaysdk.system.gis.mapping.HasThematicAttributeDTO.CLASS);
  }
  
  public static void removeAllHasThematicAttribute(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    clientRequestIF.deleteChildren(oid, com.runwaysdk.system.gis.mapping.HasThematicAttributeDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.gis.mapping.GeneratedMapDTO> getAllContainingMap()
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.mapping.GeneratedMapDTO>) getRequest().getParents(this.getOid(), com.runwaysdk.system.gis.mapping.HasLayerDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.gis.mapping.GeneratedMapDTO> getAllContainingMap(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.mapping.GeneratedMapDTO>) clientRequestIF.getParents(oid, com.runwaysdk.system.gis.mapping.HasLayerDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.gis.mapping.HasLayerDTO> getAllContainingMapRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.mapping.HasLayerDTO>) getRequest().getParentRelationships(this.getOid(), com.runwaysdk.system.gis.mapping.HasLayerDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.gis.mapping.HasLayerDTO> getAllContainingMapRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.mapping.HasLayerDTO>) clientRequestIF.getParentRelationships(oid, com.runwaysdk.system.gis.mapping.HasLayerDTO.CLASS);
  }
  
  public com.runwaysdk.system.gis.mapping.HasLayerDTO addContainingMap(com.runwaysdk.system.gis.mapping.GeneratedMapDTO parent)
  {
    return (com.runwaysdk.system.gis.mapping.HasLayerDTO) getRequest().addParent(parent.getOid(), this.getOid(), com.runwaysdk.system.gis.mapping.HasLayerDTO.CLASS);
  }
  
  public static com.runwaysdk.system.gis.mapping.HasLayerDTO addContainingMap(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid, com.runwaysdk.system.gis.mapping.GeneratedMapDTO parent)
  {
    return (com.runwaysdk.system.gis.mapping.HasLayerDTO) clientRequestIF.addParent(parent.getOid(), oid, com.runwaysdk.system.gis.mapping.HasLayerDTO.CLASS);
  }
  
  public void removeContainingMap(com.runwaysdk.system.gis.mapping.HasLayerDTO relationship)
  {
    getRequest().deleteParent(relationship.getOid());
  }
  
  public static void removeContainingMap(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.gis.mapping.HasLayerDTO relationship)
  {
    clientRequestIF.deleteParent(relationship.getOid());
  }
  
  public void removeAllContainingMap()
  {
    getRequest().deleteParents(this.getOid(), com.runwaysdk.system.gis.mapping.HasLayerDTO.CLASS);
  }
  
  public static void removeAllContainingMap(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    clientRequestIF.deleteParents(oid, com.runwaysdk.system.gis.mapping.HasLayerDTO.CLASS);
  }
  
  public static com.runwaysdk.system.gis.mapping.LayerDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String oid)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(oid);
    
    return (com.runwaysdk.system.gis.mapping.LayerDTO) dto;
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
  
  public static com.runwaysdk.system.gis.mapping.LayerQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.gis.mapping.LayerQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.gis.mapping.LayerDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.gis.mapping.LayerDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.gis.mapping.LayerDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.gis.mapping.LayerDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.gis.mapping.LayerDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.gis.mapping.LayerDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.gis.mapping.LayerDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
