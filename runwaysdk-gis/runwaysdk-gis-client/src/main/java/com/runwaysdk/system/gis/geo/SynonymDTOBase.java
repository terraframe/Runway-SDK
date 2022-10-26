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
package com.runwaysdk.system.gis.geo;

@com.runwaysdk.business.ClassSignature(hash = -1607185476)
public abstract class SynonymDTOBase extends com.runwaysdk.business.ontology.TermDTO
{
  public final static String CLASS = "com.runwaysdk.system.gis.geo.Synonym";
  private static final long serialVersionUID = -1607185476;
  
  protected SynonymDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected SynonymDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public final static java.lang.String CREATEDATE = "createDate";
  public final static java.lang.String CREATEDBY = "createdBy";
  public final static java.lang.String DISPLAYLABEL = "displayLabel";
  public final static java.lang.String ENTITYDOMAIN = "entityDomain";
  public final static java.lang.String OID = "oid";
  public final static java.lang.String KEYNAME = "keyName";
  public final static java.lang.String LASTUPDATEDATE = "lastUpdateDate";
  public final static java.lang.String LASTUPDATEDBY = "lastUpdatedBy";
  public final static java.lang.String LOCKEDBY = "lockedBy";
  public final static java.lang.String OWNER = "owner";
  public final static java.lang.String SEQ = "seq";
  public final static java.lang.String SITEMASTER = "siteMaster";
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
  
  public com.runwaysdk.system.gis.geo.SynonymDisplayLabelDTO getDisplayLabel()
  {
    return (com.runwaysdk.system.gis.geo.SynonymDisplayLabelDTO) this.getAttributeStructDTO(DISPLAYLABEL).getStructDTO();
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
  
  public static final com.runwaysdk.business.ontology.TermAndRelDTO create(com.runwaysdk.constants.ClientRequestIF clientRequest, com.runwaysdk.system.gis.geo.SynonymDTO synonym, java.lang.String geoId)
  {
    String[] _declaredTypes = new String[]{"com.runwaysdk.system.gis.geo.Synonym", "java.lang.String"};
    Object[] _parameters = new Object[]{synonym, geoId};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.gis.geo.SynonymDTO.CLASS, "create", _declaredTypes);
    return (com.runwaysdk.business.ontology.TermAndRelDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.gis.geo.GeoEntityDTO> getAllGeoEntity()
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.geo.GeoEntityDTO>) getRequest().getParents(this.getOid(), com.runwaysdk.system.gis.geo.SynonymRelationshipDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.gis.geo.GeoEntityDTO> getAllGeoEntity(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.geo.GeoEntityDTO>) clientRequestIF.getParents(oid, com.runwaysdk.system.gis.geo.SynonymRelationshipDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.gis.geo.SynonymRelationshipDTO> getAllGeoEntityRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.geo.SynonymRelationshipDTO>) getRequest().getParentRelationships(this.getOid(), com.runwaysdk.system.gis.geo.SynonymRelationshipDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.gis.geo.SynonymRelationshipDTO> getAllGeoEntityRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.geo.SynonymRelationshipDTO>) clientRequestIF.getParentRelationships(oid, com.runwaysdk.system.gis.geo.SynonymRelationshipDTO.CLASS);
  }
  
  public com.runwaysdk.system.gis.geo.SynonymRelationshipDTO addGeoEntity(com.runwaysdk.system.gis.geo.GeoEntityDTO parent)
  {
    return (com.runwaysdk.system.gis.geo.SynonymRelationshipDTO) getRequest().addParent(parent.getOid(), this.getOid(), com.runwaysdk.system.gis.geo.SynonymRelationshipDTO.CLASS);
  }
  
  public static com.runwaysdk.system.gis.geo.SynonymRelationshipDTO addGeoEntity(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid, com.runwaysdk.system.gis.geo.GeoEntityDTO parent)
  {
    return (com.runwaysdk.system.gis.geo.SynonymRelationshipDTO) clientRequestIF.addParent(parent.getOid(), oid, com.runwaysdk.system.gis.geo.SynonymRelationshipDTO.CLASS);
  }
  
  public void removeGeoEntity(com.runwaysdk.system.gis.geo.SynonymRelationshipDTO relationship)
  {
    getRequest().deleteParent(relationship.getOid());
  }
  
  public static void removeGeoEntity(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.gis.geo.SynonymRelationshipDTO relationship)
  {
    clientRequestIF.deleteParent(relationship.getOid());
  }
  
  public void removeAllGeoEntity()
  {
    getRequest().deleteParents(this.getOid(), com.runwaysdk.system.gis.geo.SynonymRelationshipDTO.CLASS);
  }
  
  public static void removeAllGeoEntity(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    clientRequestIF.deleteParents(oid, com.runwaysdk.system.gis.geo.SynonymRelationshipDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.MdAttributeMultiTermDTO> getAllSynonymMultiTermAttributeRoots()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdAttributeMultiTermDTO>) getRequest().getParents(this.getOid(), com.runwaysdk.system.gis.geo.SynonymMultiTermAttributeRootDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.MdAttributeMultiTermDTO> getAllSynonymMultiTermAttributeRoots(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdAttributeMultiTermDTO>) clientRequestIF.getParents(oid, com.runwaysdk.system.gis.geo.SynonymMultiTermAttributeRootDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.gis.geo.SynonymMultiTermAttributeRootDTO> getAllSynonymMultiTermAttributeRootsRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.geo.SynonymMultiTermAttributeRootDTO>) getRequest().getParentRelationships(this.getOid(), com.runwaysdk.system.gis.geo.SynonymMultiTermAttributeRootDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.gis.geo.SynonymMultiTermAttributeRootDTO> getAllSynonymMultiTermAttributeRootsRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.geo.SynonymMultiTermAttributeRootDTO>) clientRequestIF.getParentRelationships(oid, com.runwaysdk.system.gis.geo.SynonymMultiTermAttributeRootDTO.CLASS);
  }
  
  public com.runwaysdk.system.gis.geo.SynonymMultiTermAttributeRootDTO addSynonymMultiTermAttributeRoots(com.runwaysdk.system.metadata.MdAttributeMultiTermDTO parent)
  {
    return (com.runwaysdk.system.gis.geo.SynonymMultiTermAttributeRootDTO) getRequest().addParent(parent.getOid(), this.getOid(), com.runwaysdk.system.gis.geo.SynonymMultiTermAttributeRootDTO.CLASS);
  }
  
  public static com.runwaysdk.system.gis.geo.SynonymMultiTermAttributeRootDTO addSynonymMultiTermAttributeRoots(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid, com.runwaysdk.system.metadata.MdAttributeMultiTermDTO parent)
  {
    return (com.runwaysdk.system.gis.geo.SynonymMultiTermAttributeRootDTO) clientRequestIF.addParent(parent.getOid(), oid, com.runwaysdk.system.gis.geo.SynonymMultiTermAttributeRootDTO.CLASS);
  }
  
  public void removeSynonymMultiTermAttributeRoots(com.runwaysdk.system.gis.geo.SynonymMultiTermAttributeRootDTO relationship)
  {
    getRequest().deleteParent(relationship.getOid());
  }
  
  public static void removeSynonymMultiTermAttributeRoots(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.gis.geo.SynonymMultiTermAttributeRootDTO relationship)
  {
    clientRequestIF.deleteParent(relationship.getOid());
  }
  
  public void removeAllSynonymMultiTermAttributeRoots()
  {
    getRequest().deleteParents(this.getOid(), com.runwaysdk.system.gis.geo.SynonymMultiTermAttributeRootDTO.CLASS);
  }
  
  public static void removeAllSynonymMultiTermAttributeRoots(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    clientRequestIF.deleteParents(oid, com.runwaysdk.system.gis.geo.SynonymMultiTermAttributeRootDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.MdAttributeTermDTO> getAllSynonymTermAttributeRoots()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdAttributeTermDTO>) getRequest().getParents(this.getOid(), com.runwaysdk.system.gis.geo.SynonymTermAttributeRootDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.MdAttributeTermDTO> getAllSynonymTermAttributeRoots(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdAttributeTermDTO>) clientRequestIF.getParents(oid, com.runwaysdk.system.gis.geo.SynonymTermAttributeRootDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.gis.geo.SynonymTermAttributeRootDTO> getAllSynonymTermAttributeRootsRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.geo.SynonymTermAttributeRootDTO>) getRequest().getParentRelationships(this.getOid(), com.runwaysdk.system.gis.geo.SynonymTermAttributeRootDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.gis.geo.SynonymTermAttributeRootDTO> getAllSynonymTermAttributeRootsRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.geo.SynonymTermAttributeRootDTO>) clientRequestIF.getParentRelationships(oid, com.runwaysdk.system.gis.geo.SynonymTermAttributeRootDTO.CLASS);
  }
  
  public com.runwaysdk.system.gis.geo.SynonymTermAttributeRootDTO addSynonymTermAttributeRoots(com.runwaysdk.system.metadata.MdAttributeTermDTO parent)
  {
    return (com.runwaysdk.system.gis.geo.SynonymTermAttributeRootDTO) getRequest().addParent(parent.getOid(), this.getOid(), com.runwaysdk.system.gis.geo.SynonymTermAttributeRootDTO.CLASS);
  }
  
  public static com.runwaysdk.system.gis.geo.SynonymTermAttributeRootDTO addSynonymTermAttributeRoots(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid, com.runwaysdk.system.metadata.MdAttributeTermDTO parent)
  {
    return (com.runwaysdk.system.gis.geo.SynonymTermAttributeRootDTO) clientRequestIF.addParent(parent.getOid(), oid, com.runwaysdk.system.gis.geo.SynonymTermAttributeRootDTO.CLASS);
  }
  
  public void removeSynonymTermAttributeRoots(com.runwaysdk.system.gis.geo.SynonymTermAttributeRootDTO relationship)
  {
    getRequest().deleteParent(relationship.getOid());
  }
  
  public static void removeSynonymTermAttributeRoots(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.gis.geo.SynonymTermAttributeRootDTO relationship)
  {
    clientRequestIF.deleteParent(relationship.getOid());
  }
  
  public void removeAllSynonymTermAttributeRoots()
  {
    getRequest().deleteParents(this.getOid(), com.runwaysdk.system.gis.geo.SynonymTermAttributeRootDTO.CLASS);
  }
  
  public static void removeAllSynonymTermAttributeRoots(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    clientRequestIF.deleteParents(oid, com.runwaysdk.system.gis.geo.SynonymTermAttributeRootDTO.CLASS);
  }
  
  public static com.runwaysdk.system.gis.geo.SynonymDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String oid)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(oid);
    
    return (com.runwaysdk.system.gis.geo.SynonymDTO) dto;
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
  
  public static com.runwaysdk.system.gis.geo.SynonymQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.gis.geo.SynonymQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.gis.geo.SynonymDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.gis.geo.SynonymDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.gis.geo.SynonymDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.gis.geo.SynonymDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.gis.geo.SynonymDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.gis.geo.SynonymDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.gis.geo.SynonymDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
