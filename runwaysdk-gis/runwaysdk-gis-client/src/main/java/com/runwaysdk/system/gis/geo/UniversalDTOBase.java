/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
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
package com.runwaysdk.system.gis.geo;

@com.runwaysdk.business.ClassSignature(hash = -2090690864)
public abstract class UniversalDTOBase extends com.runwaysdk.business.ontology.TermDTO
{
  public final static String CLASS = "com.runwaysdk.system.gis.geo.Universal";
  private static final long serialVersionUID = -2090690864;
  
  protected UniversalDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected UniversalDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String CREATEDATE = "createDate";
  public static java.lang.String CREATEDBY = "createdBy";
  public static java.lang.String DESCRIPTION = "description";
  public static java.lang.String DISPLAYLABEL = "displayLabel";
  public static java.lang.String ENTITYDOMAIN = "entityDomain";
  public static java.lang.String ID = "id";
  public static java.lang.String KEYNAME = "keyName";
  public static java.lang.String LASTUPDATEDATE = "lastUpdateDate";
  public static java.lang.String LASTUPDATEDBY = "lastUpdatedBy";
  public static java.lang.String LOCKEDBY = "lockedBy";
  public static java.lang.String OWNER = "owner";
  public static java.lang.String SEQ = "seq";
  public static java.lang.String SITEMASTER = "siteMaster";
  public static java.lang.String TYPE = "type";
  public static java.lang.String UNIVERSALID = "universalId";
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
  
  public com.runwaysdk.system.gis.geo.UniversalDescriptionDTO getDescription()
  {
    return (com.runwaysdk.system.gis.geo.UniversalDescriptionDTO) this.getAttributeStructDTO(DESCRIPTION).getStructDTO();
  }
  
  public boolean isDescriptionWritable()
  {
    return isWritable(DESCRIPTION);
  }
  
  public boolean isDescriptionReadable()
  {
    return isReadable(DESCRIPTION);
  }
  
  public boolean isDescriptionModified()
  {
    return isModified(DESCRIPTION);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeLocalCharacterMdDTO getDescriptionMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeLocalCharacterMdDTO) getAttributeDTO(DESCRIPTION).getAttributeMdDTO();
  }
  
  public com.runwaysdk.system.gis.geo.UniversalDisplayLabelDTO getDisplayLabel()
  {
    return (com.runwaysdk.system.gis.geo.UniversalDisplayLabelDTO) this.getAttributeStructDTO(DISPLAYLABEL).getStructDTO();
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
      setValue(ENTITYDOMAIN, value.getId());
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
  
  public com.runwaysdk.system.SingleActorDTO getLockedBy()
  {
    if(getValue(LOCKEDBY) == null || getValue(LOCKEDBY).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.SingleActorDTO.get(getRequest(), getValue(LOCKEDBY));
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
      setValue(OWNER, value.getId());
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
  
  public String getUniversalId()
  {
    return getValue(UNIVERSALID);
  }
  
  public void setUniversalId(String value)
  {
    if(value == null)
    {
      setValue(UNIVERSALID, "");
    }
    else
    {
      setValue(UNIVERSALID, value);
    }
  }
  
  public boolean isUniversalIdWritable()
  {
    return isWritable(UNIVERSALID);
  }
  
  public boolean isUniversalIdReadable()
  {
    return isReadable(UNIVERSALID);
  }
  
  public boolean isUniversalIdModified()
  {
    return isModified(UNIVERSALID);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getUniversalIdMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(UNIVERSALID).getAttributeMdDTO();
  }
  
  public static final com.runwaysdk.business.ontology.TermAndRelDTO create(com.runwaysdk.constants.ClientRequestIF clientRequest, com.runwaysdk.system.gis.geo.UniversalDTO dto, java.lang.String parentId, java.lang.String relationshipType)
  {
    String[] _declaredTypes = new String[]{"com.runwaysdk.system.gis.geo.Universal", "java.lang.String", "java.lang.String"};
    Object[] _parameters = new Object[]{dto, parentId, relationshipType};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.gis.geo.UniversalDTO.CLASS, "create", _declaredTypes);
    return (com.runwaysdk.business.ontology.TermAndRelDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public static final com.runwaysdk.system.gis.geo.UniversalDTO getRoot(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    String[] _declaredTypes = new String[]{};
    Object[] _parameters = new Object[]{};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.gis.geo.UniversalDTO.CLASS, "getRoot", _declaredTypes);
    return (com.runwaysdk.system.gis.geo.UniversalDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.gis.geo.UniversalDTO> getAllContains()
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.geo.UniversalDTO>) getRequest().getChildren(this.getId(), com.runwaysdk.system.gis.geo.AllowedInDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.gis.geo.UniversalDTO> getAllContains(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.geo.UniversalDTO>) clientRequestIF.getChildren(id, com.runwaysdk.system.gis.geo.AllowedInDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.gis.geo.AllowedInDTO> getAllContainsRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.geo.AllowedInDTO>) getRequest().getChildRelationships(this.getId(), com.runwaysdk.system.gis.geo.AllowedInDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.gis.geo.AllowedInDTO> getAllContainsRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.geo.AllowedInDTO>) clientRequestIF.getChildRelationships(id, com.runwaysdk.system.gis.geo.AllowedInDTO.CLASS);
  }
  
  public com.runwaysdk.system.gis.geo.AllowedInDTO addContains(com.runwaysdk.system.gis.geo.UniversalDTO child)
  {
    return (com.runwaysdk.system.gis.geo.AllowedInDTO) getRequest().addChild(this.getId(), child.getId(), com.runwaysdk.system.gis.geo.AllowedInDTO.CLASS);
  }
  
  public static com.runwaysdk.system.gis.geo.AllowedInDTO addContains(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id, com.runwaysdk.system.gis.geo.UniversalDTO child)
  {
    return (com.runwaysdk.system.gis.geo.AllowedInDTO) clientRequestIF.addChild(id, child.getId(), com.runwaysdk.system.gis.geo.AllowedInDTO.CLASS);
  }
  
  public void removeContains(com.runwaysdk.system.gis.geo.AllowedInDTO relationship)
  {
    getRequest().deleteChild(relationship.getId());
  }
  
  public static void removeContains(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.gis.geo.AllowedInDTO relationship)
  {
    clientRequestIF.deleteChild(relationship.getId());
  }
  
  public void removeAllContains()
  {
    getRequest().deleteChildren(this.getId(), com.runwaysdk.system.gis.geo.AllowedInDTO.CLASS);
  }
  
  public static void removeAllContains(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    clientRequestIF.deleteChildren(id, com.runwaysdk.system.gis.geo.AllowedInDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.gis.geo.UniversalDTO> getAllSubType()
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.geo.UniversalDTO>) getRequest().getChildren(this.getId(), com.runwaysdk.system.gis.geo.IsARelationshipDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.gis.geo.UniversalDTO> getAllSubType(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.geo.UniversalDTO>) clientRequestIF.getChildren(id, com.runwaysdk.system.gis.geo.IsARelationshipDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.gis.geo.IsARelationshipDTO> getAllSubTypeRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.geo.IsARelationshipDTO>) getRequest().getChildRelationships(this.getId(), com.runwaysdk.system.gis.geo.IsARelationshipDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.gis.geo.IsARelationshipDTO> getAllSubTypeRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.geo.IsARelationshipDTO>) clientRequestIF.getChildRelationships(id, com.runwaysdk.system.gis.geo.IsARelationshipDTO.CLASS);
  }
  
  public com.runwaysdk.system.gis.geo.IsARelationshipDTO addSubType(com.runwaysdk.system.gis.geo.UniversalDTO child)
  {
    return (com.runwaysdk.system.gis.geo.IsARelationshipDTO) getRequest().addChild(this.getId(), child.getId(), com.runwaysdk.system.gis.geo.IsARelationshipDTO.CLASS);
  }
  
  public static com.runwaysdk.system.gis.geo.IsARelationshipDTO addSubType(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id, com.runwaysdk.system.gis.geo.UniversalDTO child)
  {
    return (com.runwaysdk.system.gis.geo.IsARelationshipDTO) clientRequestIF.addChild(id, child.getId(), com.runwaysdk.system.gis.geo.IsARelationshipDTO.CLASS);
  }
  
  public void removeSubType(com.runwaysdk.system.gis.geo.IsARelationshipDTO relationship)
  {
    getRequest().deleteChild(relationship.getId());
  }
  
  public static void removeSubType(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.gis.geo.IsARelationshipDTO relationship)
  {
    clientRequestIF.deleteChild(relationship.getId());
  }
  
  public void removeAllSubType()
  {
    getRequest().deleteChildren(this.getId(), com.runwaysdk.system.gis.geo.IsARelationshipDTO.CLASS);
  }
  
  public static void removeAllSubType(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    clientRequestIF.deleteChildren(id, com.runwaysdk.system.gis.geo.IsARelationshipDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.gis.geo.UniversalDTO> getAllAllowedIn()
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.geo.UniversalDTO>) getRequest().getParents(this.getId(), com.runwaysdk.system.gis.geo.AllowedInDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.gis.geo.UniversalDTO> getAllAllowedIn(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.geo.UniversalDTO>) clientRequestIF.getParents(id, com.runwaysdk.system.gis.geo.AllowedInDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.gis.geo.AllowedInDTO> getAllAllowedInRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.geo.AllowedInDTO>) getRequest().getParentRelationships(this.getId(), com.runwaysdk.system.gis.geo.AllowedInDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.gis.geo.AllowedInDTO> getAllAllowedInRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.geo.AllowedInDTO>) clientRequestIF.getParentRelationships(id, com.runwaysdk.system.gis.geo.AllowedInDTO.CLASS);
  }
  
  public com.runwaysdk.system.gis.geo.AllowedInDTO addAllowedIn(com.runwaysdk.system.gis.geo.UniversalDTO parent)
  {
    return (com.runwaysdk.system.gis.geo.AllowedInDTO) getRequest().addParent(parent.getId(), this.getId(), com.runwaysdk.system.gis.geo.AllowedInDTO.CLASS);
  }
  
  public static com.runwaysdk.system.gis.geo.AllowedInDTO addAllowedIn(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id, com.runwaysdk.system.gis.geo.UniversalDTO parent)
  {
    return (com.runwaysdk.system.gis.geo.AllowedInDTO) clientRequestIF.addParent(parent.getId(), id, com.runwaysdk.system.gis.geo.AllowedInDTO.CLASS);
  }
  
  public void removeAllowedIn(com.runwaysdk.system.gis.geo.AllowedInDTO relationship)
  {
    getRequest().deleteParent(relationship.getId());
  }
  
  public static void removeAllowedIn(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.gis.geo.AllowedInDTO relationship)
  {
    clientRequestIF.deleteParent(relationship.getId());
  }
  
  public void removeAllAllowedIn()
  {
    getRequest().deleteParents(this.getId(), com.runwaysdk.system.gis.geo.AllowedInDTO.CLASS);
  }
  
  public static void removeAllAllowedIn(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    clientRequestIF.deleteParents(id, com.runwaysdk.system.gis.geo.AllowedInDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.gis.geo.UniversalDTO> getAllSuperType()
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.geo.UniversalDTO>) getRequest().getParents(this.getId(), com.runwaysdk.system.gis.geo.IsARelationshipDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.gis.geo.UniversalDTO> getAllSuperType(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.geo.UniversalDTO>) clientRequestIF.getParents(id, com.runwaysdk.system.gis.geo.IsARelationshipDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.gis.geo.IsARelationshipDTO> getAllSuperTypeRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.geo.IsARelationshipDTO>) getRequest().getParentRelationships(this.getId(), com.runwaysdk.system.gis.geo.IsARelationshipDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.gis.geo.IsARelationshipDTO> getAllSuperTypeRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.geo.IsARelationshipDTO>) clientRequestIF.getParentRelationships(id, com.runwaysdk.system.gis.geo.IsARelationshipDTO.CLASS);
  }
  
  public com.runwaysdk.system.gis.geo.IsARelationshipDTO addSuperType(com.runwaysdk.system.gis.geo.UniversalDTO parent)
  {
    return (com.runwaysdk.system.gis.geo.IsARelationshipDTO) getRequest().addParent(parent.getId(), this.getId(), com.runwaysdk.system.gis.geo.IsARelationshipDTO.CLASS);
  }
  
  public static com.runwaysdk.system.gis.geo.IsARelationshipDTO addSuperType(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id, com.runwaysdk.system.gis.geo.UniversalDTO parent)
  {
    return (com.runwaysdk.system.gis.geo.IsARelationshipDTO) clientRequestIF.addParent(parent.getId(), id, com.runwaysdk.system.gis.geo.IsARelationshipDTO.CLASS);
  }
  
  public void removeSuperType(com.runwaysdk.system.gis.geo.IsARelationshipDTO relationship)
  {
    getRequest().deleteParent(relationship.getId());
  }
  
  public static void removeSuperType(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.gis.geo.IsARelationshipDTO relationship)
  {
    clientRequestIF.deleteParent(relationship.getId());
  }
  
  public void removeAllSuperType()
  {
    getRequest().deleteParents(this.getId(), com.runwaysdk.system.gis.geo.IsARelationshipDTO.CLASS);
  }
  
  public static void removeAllSuperType(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    clientRequestIF.deleteParents(id, com.runwaysdk.system.gis.geo.IsARelationshipDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.MdAttributeMultiTermDTO> getAllUniversalMultiTermAttributeRoots()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdAttributeMultiTermDTO>) getRequest().getParents(this.getId(), com.runwaysdk.system.gis.geo.UniversalMultiTermAttributeRootDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.MdAttributeMultiTermDTO> getAllUniversalMultiTermAttributeRoots(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdAttributeMultiTermDTO>) clientRequestIF.getParents(id, com.runwaysdk.system.gis.geo.UniversalMultiTermAttributeRootDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.gis.geo.UniversalMultiTermAttributeRootDTO> getAllUniversalMultiTermAttributeRootsRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.geo.UniversalMultiTermAttributeRootDTO>) getRequest().getParentRelationships(this.getId(), com.runwaysdk.system.gis.geo.UniversalMultiTermAttributeRootDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.gis.geo.UniversalMultiTermAttributeRootDTO> getAllUniversalMultiTermAttributeRootsRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.geo.UniversalMultiTermAttributeRootDTO>) clientRequestIF.getParentRelationships(id, com.runwaysdk.system.gis.geo.UniversalMultiTermAttributeRootDTO.CLASS);
  }
  
  public com.runwaysdk.system.gis.geo.UniversalMultiTermAttributeRootDTO addUniversalMultiTermAttributeRoots(com.runwaysdk.system.metadata.MdAttributeMultiTermDTO parent)
  {
    return (com.runwaysdk.system.gis.geo.UniversalMultiTermAttributeRootDTO) getRequest().addParent(parent.getId(), this.getId(), com.runwaysdk.system.gis.geo.UniversalMultiTermAttributeRootDTO.CLASS);
  }
  
  public static com.runwaysdk.system.gis.geo.UniversalMultiTermAttributeRootDTO addUniversalMultiTermAttributeRoots(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id, com.runwaysdk.system.metadata.MdAttributeMultiTermDTO parent)
  {
    return (com.runwaysdk.system.gis.geo.UniversalMultiTermAttributeRootDTO) clientRequestIF.addParent(parent.getId(), id, com.runwaysdk.system.gis.geo.UniversalMultiTermAttributeRootDTO.CLASS);
  }
  
  public void removeUniversalMultiTermAttributeRoots(com.runwaysdk.system.gis.geo.UniversalMultiTermAttributeRootDTO relationship)
  {
    getRequest().deleteParent(relationship.getId());
  }
  
  public static void removeUniversalMultiTermAttributeRoots(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.gis.geo.UniversalMultiTermAttributeRootDTO relationship)
  {
    clientRequestIF.deleteParent(relationship.getId());
  }
  
  public void removeAllUniversalMultiTermAttributeRoots()
  {
    getRequest().deleteParents(this.getId(), com.runwaysdk.system.gis.geo.UniversalMultiTermAttributeRootDTO.CLASS);
  }
  
  public static void removeAllUniversalMultiTermAttributeRoots(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    clientRequestIF.deleteParents(id, com.runwaysdk.system.gis.geo.UniversalMultiTermAttributeRootDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.MdAttributeTermDTO> getAllUniversalTermAttributeRoots()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdAttributeTermDTO>) getRequest().getParents(this.getId(), com.runwaysdk.system.gis.geo.UniversalTermAttributeRootDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.MdAttributeTermDTO> getAllUniversalTermAttributeRoots(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdAttributeTermDTO>) clientRequestIF.getParents(id, com.runwaysdk.system.gis.geo.UniversalTermAttributeRootDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.gis.geo.UniversalTermAttributeRootDTO> getAllUniversalTermAttributeRootsRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.geo.UniversalTermAttributeRootDTO>) getRequest().getParentRelationships(this.getId(), com.runwaysdk.system.gis.geo.UniversalTermAttributeRootDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.gis.geo.UniversalTermAttributeRootDTO> getAllUniversalTermAttributeRootsRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.geo.UniversalTermAttributeRootDTO>) clientRequestIF.getParentRelationships(id, com.runwaysdk.system.gis.geo.UniversalTermAttributeRootDTO.CLASS);
  }
  
  public com.runwaysdk.system.gis.geo.UniversalTermAttributeRootDTO addUniversalTermAttributeRoots(com.runwaysdk.system.metadata.MdAttributeTermDTO parent)
  {
    return (com.runwaysdk.system.gis.geo.UniversalTermAttributeRootDTO) getRequest().addParent(parent.getId(), this.getId(), com.runwaysdk.system.gis.geo.UniversalTermAttributeRootDTO.CLASS);
  }
  
  public static com.runwaysdk.system.gis.geo.UniversalTermAttributeRootDTO addUniversalTermAttributeRoots(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id, com.runwaysdk.system.metadata.MdAttributeTermDTO parent)
  {
    return (com.runwaysdk.system.gis.geo.UniversalTermAttributeRootDTO) clientRequestIF.addParent(parent.getId(), id, com.runwaysdk.system.gis.geo.UniversalTermAttributeRootDTO.CLASS);
  }
  
  public void removeUniversalTermAttributeRoots(com.runwaysdk.system.gis.geo.UniversalTermAttributeRootDTO relationship)
  {
    getRequest().deleteParent(relationship.getId());
  }
  
  public static void removeUniversalTermAttributeRoots(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.gis.geo.UniversalTermAttributeRootDTO relationship)
  {
    clientRequestIF.deleteParent(relationship.getId());
  }
  
  public void removeAllUniversalTermAttributeRoots()
  {
    getRequest().deleteParents(this.getId(), com.runwaysdk.system.gis.geo.UniversalTermAttributeRootDTO.CLASS);
  }
  
  public static void removeAllUniversalTermAttributeRoots(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    clientRequestIF.deleteParents(id, com.runwaysdk.system.gis.geo.UniversalTermAttributeRootDTO.CLASS);
  }
  
  public static com.runwaysdk.system.gis.geo.UniversalDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String id)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(id);
    
    return (com.runwaysdk.system.gis.geo.UniversalDTO) dto;
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
  
  public static com.runwaysdk.system.gis.geo.UniversalQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.gis.geo.UniversalQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.gis.geo.UniversalDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.gis.geo.UniversalDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.gis.geo.UniversalDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.gis.geo.UniversalDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.gis.geo.UniversalDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.gis.geo.UniversalDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.gis.geo.UniversalDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
