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

@com.runwaysdk.business.ClassSignature(hash = -1702054576)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to Universal.java
 *
 * @author Autogenerated by RunwaySDK
 */
public abstract class UniversalBase extends com.runwaysdk.business.ontology.Term
{
  private static final com.runwaysdk.business.ontology.OntologyStrategyIF strategy;
  static 
  {
    strategy =  com.runwaysdk.business.ontology.Term.assignStrategy("com.runwaysdk.system.gis.geo.Universal");
  }
  public final static String CLASS = "com.runwaysdk.system.gis.geo.Universal";
  public static java.lang.String CREATEDATE = "createDate";
  public static java.lang.String CREATEDBY = "createdBy";
  public static java.lang.String DESCRIPTION = "description";
  private com.runwaysdk.business.Struct description = null;
  
  public static java.lang.String DISPLAYLABEL = "displayLabel";
  private com.runwaysdk.business.Struct displayLabel = null;
  
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
  private static final long serialVersionUID = -1702054576;
  
  public UniversalBase()
  {
    super();
    description = super.getStruct("description");
    displayLabel = super.getStruct("displayLabel");
  }
  
  public java.util.Date getCreateDate()
  {
    return com.runwaysdk.constants.MdAttributeDateTimeUtil.getTypeSafeValue(getValue(CREATEDATE));
  }
  
  public void validateCreateDate()
  {
    this.validateAttribute(CREATEDATE);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeDateTimeDAOIF getCreateDateMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.system.gis.geo.Universal.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeDateTimeDAOIF)mdClassIF.definesAttribute(CREATEDATE);
  }
  
  public com.runwaysdk.system.SingleActor getCreatedBy()
  {
    if (getValue(CREATEDBY).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.SingleActor.get(getValue(CREATEDBY));
    }
  }
  
  public String getCreatedById()
  {
    return getValue(CREATEDBY);
  }
  
  public void validateCreatedBy()
  {
    this.validateAttribute(CREATEDBY);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF getCreatedByMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.system.gis.geo.Universal.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF)mdClassIF.definesAttribute(CREATEDBY);
  }
  
  public com.runwaysdk.system.gis.geo.UniversalDescription getDescription()
  {
    return (com.runwaysdk.system.gis.geo.UniversalDescription) description;
  }
  
  public void validateDescription()
  {
    this.validateAttribute(DESCRIPTION);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeLocalCharacterDAOIF getDescriptionMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.system.gis.geo.Universal.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeLocalCharacterDAOIF)mdClassIF.definesAttribute(DESCRIPTION);
  }
  
  public com.runwaysdk.system.gis.geo.UniversalDisplayLabel getDisplayLabel()
  {
    return (com.runwaysdk.system.gis.geo.UniversalDisplayLabel) displayLabel;
  }
  
  public void validateDisplayLabel()
  {
    this.validateAttribute(DISPLAYLABEL);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeLocalCharacterDAOIF getDisplayLabelMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.system.gis.geo.Universal.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeLocalCharacterDAOIF)mdClassIF.definesAttribute(DISPLAYLABEL);
  }
  
  public com.runwaysdk.system.metadata.MdDomain getEntityDomain()
  {
    if (getValue(ENTITYDOMAIN).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.metadata.MdDomain.get(getValue(ENTITYDOMAIN));
    }
  }
  
  public String getEntityDomainId()
  {
    return getValue(ENTITYDOMAIN);
  }
  
  public void validateEntityDomain()
  {
    this.validateAttribute(ENTITYDOMAIN);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF getEntityDomainMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.system.gis.geo.Universal.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF)mdClassIF.definesAttribute(ENTITYDOMAIN);
  }
  
  public void setEntityDomain(com.runwaysdk.system.metadata.MdDomain value)
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
  
  public String getId()
  {
    return getValue(ID);
  }
  
  public void validateId()
  {
    this.validateAttribute(ID);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF getIdMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.system.gis.geo.Universal.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF)mdClassIF.definesAttribute(ID);
  }
  
  public String getKeyName()
  {
    return getValue(KEYNAME);
  }
  
  public void validateKeyName()
  {
    this.validateAttribute(KEYNAME);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF getKeyNameMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.system.gis.geo.Universal.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF)mdClassIF.definesAttribute(KEYNAME);
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
  
  public java.util.Date getLastUpdateDate()
  {
    return com.runwaysdk.constants.MdAttributeDateTimeUtil.getTypeSafeValue(getValue(LASTUPDATEDATE));
  }
  
  public void validateLastUpdateDate()
  {
    this.validateAttribute(LASTUPDATEDATE);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeDateTimeDAOIF getLastUpdateDateMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.system.gis.geo.Universal.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeDateTimeDAOIF)mdClassIF.definesAttribute(LASTUPDATEDATE);
  }
  
  public com.runwaysdk.system.SingleActor getLastUpdatedBy()
  {
    if (getValue(LASTUPDATEDBY).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.SingleActor.get(getValue(LASTUPDATEDBY));
    }
  }
  
  public String getLastUpdatedById()
  {
    return getValue(LASTUPDATEDBY);
  }
  
  public void validateLastUpdatedBy()
  {
    this.validateAttribute(LASTUPDATEDBY);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF getLastUpdatedByMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.system.gis.geo.Universal.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF)mdClassIF.definesAttribute(LASTUPDATEDBY);
  }
  
  public com.runwaysdk.system.SingleActor getLockedBy()
  {
    if (getValue(LOCKEDBY).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.SingleActor.get(getValue(LOCKEDBY));
    }
  }
  
  public String getLockedById()
  {
    return getValue(LOCKEDBY);
  }
  
  public void validateLockedBy()
  {
    this.validateAttribute(LOCKEDBY);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF getLockedByMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.system.gis.geo.Universal.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF)mdClassIF.definesAttribute(LOCKEDBY);
  }
  
  public com.runwaysdk.system.Actor getOwner()
  {
    if (getValue(OWNER).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.Actor.get(getValue(OWNER));
    }
  }
  
  public String getOwnerId()
  {
    return getValue(OWNER);
  }
  
  public void validateOwner()
  {
    this.validateAttribute(OWNER);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF getOwnerMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.system.gis.geo.Universal.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF)mdClassIF.definesAttribute(OWNER);
  }
  
  public void setOwner(com.runwaysdk.system.Actor value)
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
  
  public Long getSeq()
  {
    return com.runwaysdk.constants.MdAttributeLongUtil.getTypeSafeValue(getValue(SEQ));
  }
  
  public void validateSeq()
  {
    this.validateAttribute(SEQ);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeLongDAOIF getSeqMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.system.gis.geo.Universal.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeLongDAOIF)mdClassIF.definesAttribute(SEQ);
  }
  
  public String getSiteMaster()
  {
    return getValue(SITEMASTER);
  }
  
  public void validateSiteMaster()
  {
    this.validateAttribute(SITEMASTER);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF getSiteMasterMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.system.gis.geo.Universal.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF)mdClassIF.definesAttribute(SITEMASTER);
  }
  
  public String getType()
  {
    return getValue(TYPE);
  }
  
  public void validateType()
  {
    this.validateAttribute(TYPE);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF getTypeMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.system.gis.geo.Universal.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF)mdClassIF.definesAttribute(TYPE);
  }
  
  public String getUniversalId()
  {
    return getValue(UNIVERSALID);
  }
  
  public void validateUniversalId()
  {
    this.validateAttribute(UNIVERSALID);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF getUniversalIdMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.system.gis.geo.Universal.CLASS);
    return (com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF)mdClassIF.definesAttribute(UNIVERSALID);
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
  
  protected String getDeclaredType()
  {
    return CLASS;
  }
  
  public com.runwaysdk.system.gis.geo.AllowedIn addContains(com.runwaysdk.system.gis.geo.Universal universal)
  {
    return (com.runwaysdk.system.gis.geo.AllowedIn) addChild(universal, com.runwaysdk.system.gis.geo.AllowedIn.CLASS);
  }
  
  public void removeContains(com.runwaysdk.system.gis.geo.Universal universal)
  {
    removeAllChildren(universal, com.runwaysdk.system.gis.geo.AllowedIn.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.gis.geo.Universal> getAllContains()
  {
    return (com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.gis.geo.Universal>) getChildren(com.runwaysdk.system.gis.geo.AllowedIn.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.gis.geo.AllowedIn> getAllContainsRel()
  {
    return (com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.gis.geo.AllowedIn>) getChildRelationships(com.runwaysdk.system.gis.geo.AllowedIn.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public com.runwaysdk.system.gis.geo.AllowedIn getContainsRel(com.runwaysdk.system.gis.geo.Universal universal)
  {
    com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.gis.geo.AllowedIn> iterator = (com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.gis.geo.AllowedIn>) getRelationshipsWithChild(universal, com.runwaysdk.system.gis.geo.AllowedIn.CLASS);
    try
    {
      if (iterator.hasNext())
      {
        return iterator.next();
      }
      else
      {
        return null;
      }
    }
    finally
    {
      iterator.close();
    }
  }
  
  public com.runwaysdk.system.gis.geo.IsARelationship addSubType(com.runwaysdk.system.gis.geo.Universal universal)
  {
    return (com.runwaysdk.system.gis.geo.IsARelationship) addChild(universal, com.runwaysdk.system.gis.geo.IsARelationship.CLASS);
  }
  
  public void removeSubType(com.runwaysdk.system.gis.geo.Universal universal)
  {
    removeAllChildren(universal, com.runwaysdk.system.gis.geo.IsARelationship.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.gis.geo.Universal> getAllSubType()
  {
    return (com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.gis.geo.Universal>) getChildren(com.runwaysdk.system.gis.geo.IsARelationship.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.gis.geo.IsARelationship> getAllSubTypeRel()
  {
    return (com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.gis.geo.IsARelationship>) getChildRelationships(com.runwaysdk.system.gis.geo.IsARelationship.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public com.runwaysdk.system.gis.geo.IsARelationship getSubTypeRel(com.runwaysdk.system.gis.geo.Universal universal)
  {
    com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.gis.geo.IsARelationship> iterator = (com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.gis.geo.IsARelationship>) getRelationshipsWithChild(universal, com.runwaysdk.system.gis.geo.IsARelationship.CLASS);
    try
    {
      if (iterator.hasNext())
      {
        return iterator.next();
      }
      else
      {
        return null;
      }
    }
    finally
    {
      iterator.close();
    }
  }
  
  public com.runwaysdk.system.gis.geo.AllowedIn addAllowedIn(com.runwaysdk.system.gis.geo.Universal universal)
  {
    return (com.runwaysdk.system.gis.geo.AllowedIn) addParent(universal, com.runwaysdk.system.gis.geo.AllowedIn.CLASS);
  }
  
  public void removeAllowedIn(com.runwaysdk.system.gis.geo.Universal universal)
  {
    removeAllParents(universal, com.runwaysdk.system.gis.geo.AllowedIn.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.gis.geo.Universal> getAllAllowedIn()
  {
    return (com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.gis.geo.Universal>) getParents(com.runwaysdk.system.gis.geo.AllowedIn.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.gis.geo.AllowedIn> getAllAllowedInRel()
  {
    return (com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.gis.geo.AllowedIn>) getParentRelationships(com.runwaysdk.system.gis.geo.AllowedIn.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public com.runwaysdk.system.gis.geo.AllowedIn getAllowedInRel(com.runwaysdk.system.gis.geo.Universal universal)
  {
    com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.gis.geo.AllowedIn> iterator = (com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.gis.geo.AllowedIn>) getRelationshipsWithParent(universal, com.runwaysdk.system.gis.geo.AllowedIn.CLASS);
    try
    {
      if (iterator.hasNext())
      {
        return iterator.next();
      }
      else
      {
        return null;
      }
    }
    finally
    {
      iterator.close();
    }
  }
  
  public com.runwaysdk.system.gis.geo.IsARelationship addSuperType(com.runwaysdk.system.gis.geo.Universal universal)
  {
    return (com.runwaysdk.system.gis.geo.IsARelationship) addParent(universal, com.runwaysdk.system.gis.geo.IsARelationship.CLASS);
  }
  
  public void removeSuperType(com.runwaysdk.system.gis.geo.Universal universal)
  {
    removeAllParents(universal, com.runwaysdk.system.gis.geo.IsARelationship.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.gis.geo.Universal> getAllSuperType()
  {
    return (com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.gis.geo.Universal>) getParents(com.runwaysdk.system.gis.geo.IsARelationship.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.gis.geo.IsARelationship> getAllSuperTypeRel()
  {
    return (com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.gis.geo.IsARelationship>) getParentRelationships(com.runwaysdk.system.gis.geo.IsARelationship.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public com.runwaysdk.system.gis.geo.IsARelationship getSuperTypeRel(com.runwaysdk.system.gis.geo.Universal universal)
  {
    com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.gis.geo.IsARelationship> iterator = (com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.gis.geo.IsARelationship>) getRelationshipsWithParent(universal, com.runwaysdk.system.gis.geo.IsARelationship.CLASS);
    try
    {
      if (iterator.hasNext())
      {
        return iterator.next();
      }
      else
      {
        return null;
      }
    }
    finally
    {
      iterator.close();
    }
  }
  
  public com.runwaysdk.system.gis.geo.UniversalMultiTermAttributeRoot addUniversalMultiTermAttributeRoots(com.runwaysdk.system.metadata.MdAttributeMultiTerm mdAttributeMultiTerm)
  {
    return (com.runwaysdk.system.gis.geo.UniversalMultiTermAttributeRoot) addParent(mdAttributeMultiTerm, com.runwaysdk.system.gis.geo.UniversalMultiTermAttributeRoot.CLASS);
  }
  
  public void removeUniversalMultiTermAttributeRoots(com.runwaysdk.system.metadata.MdAttributeMultiTerm mdAttributeMultiTerm)
  {
    removeAllParents(mdAttributeMultiTerm, com.runwaysdk.system.gis.geo.UniversalMultiTermAttributeRoot.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.metadata.MdAttributeMultiTerm> getAllUniversalMultiTermAttributeRoots()
  {
    return (com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.metadata.MdAttributeMultiTerm>) getParents(com.runwaysdk.system.gis.geo.UniversalMultiTermAttributeRoot.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.gis.geo.UniversalMultiTermAttributeRoot> getAllUniversalMultiTermAttributeRootsRel()
  {
    return (com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.gis.geo.UniversalMultiTermAttributeRoot>) getParentRelationships(com.runwaysdk.system.gis.geo.UniversalMultiTermAttributeRoot.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.gis.geo.UniversalMultiTermAttributeRoot> getUniversalMultiTermAttributeRootsRel(com.runwaysdk.system.metadata.MdAttributeMultiTerm mdAttributeMultiTerm)
  {
    return (com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.gis.geo.UniversalMultiTermAttributeRoot>) getRelationshipsWithParent(mdAttributeMultiTerm, com.runwaysdk.system.gis.geo.UniversalMultiTermAttributeRoot.CLASS);
  }
  
  public com.runwaysdk.system.gis.geo.UniversalTermAttributeRoot addUniversalTermAttributeRoots(com.runwaysdk.system.metadata.MdAttributeTerm mdAttributeTerm)
  {
    return (com.runwaysdk.system.gis.geo.UniversalTermAttributeRoot) addParent(mdAttributeTerm, com.runwaysdk.system.gis.geo.UniversalTermAttributeRoot.CLASS);
  }
  
  public void removeUniversalTermAttributeRoots(com.runwaysdk.system.metadata.MdAttributeTerm mdAttributeTerm)
  {
    removeAllParents(mdAttributeTerm, com.runwaysdk.system.gis.geo.UniversalTermAttributeRoot.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.metadata.MdAttributeTerm> getAllUniversalTermAttributeRoots()
  {
    return (com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.metadata.MdAttributeTerm>) getParents(com.runwaysdk.system.gis.geo.UniversalTermAttributeRoot.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.gis.geo.UniversalTermAttributeRoot> getAllUniversalTermAttributeRootsRel()
  {
    return (com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.gis.geo.UniversalTermAttributeRoot>) getParentRelationships(com.runwaysdk.system.gis.geo.UniversalTermAttributeRoot.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.gis.geo.UniversalTermAttributeRoot> getUniversalTermAttributeRootsRel(com.runwaysdk.system.metadata.MdAttributeTerm mdAttributeTerm)
  {
    return (com.runwaysdk.query.OIterator<? extends com.runwaysdk.system.gis.geo.UniversalTermAttributeRoot>) getRelationshipsWithParent(mdAttributeTerm, com.runwaysdk.system.gis.geo.UniversalTermAttributeRoot.CLASS);
  }
  
  public static Universal get(String id)
  {
    return (Universal) com.runwaysdk.business.Business.get(id);
  }
  
  public static Universal getByKey(String key)
  {
    return (Universal) com.runwaysdk.business.Business.get(CLASS, key);
  }
  
  public static com.runwaysdk.business.ontology.OntologyStrategyIF getStrategy()
  {
    return strategy;
  }
  
  public static com.runwaysdk.business.ontology.TermAndRel create(com.runwaysdk.system.gis.geo.Universal dto, java.lang.String parentId, java.lang.String relationshipType)
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.system.gis.geo.Universal.java";
    throw new com.runwaysdk.dataaccess.metadata.ForbiddenMethodException(msg);
  }
  
  public static com.runwaysdk.system.gis.geo.Universal getRoot()
  {
    String msg = "This method should never be invoked.  It should be overwritten in com.runwaysdk.system.gis.geo.Universal.java";
    throw new com.runwaysdk.dataaccess.metadata.ForbiddenMethodException(msg);
  }
  
  public static Universal lock(java.lang.String id)
  {
    Universal _instance = Universal.get(id);
    _instance.lock();
    
    return _instance;
  }
  
  public static Universal unlock(java.lang.String id)
  {
    Universal _instance = Universal.get(id);
    _instance.unlock();
    
    return _instance;
  }
  
  public String toString()
  {
    if (this.isNew())
    {
      return "New: "+ this.getClassDisplayLabel();
    }
    else
    {
      return super.toString();
    }
  }
}