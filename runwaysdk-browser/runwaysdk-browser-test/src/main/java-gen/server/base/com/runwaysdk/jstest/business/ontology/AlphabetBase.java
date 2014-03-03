package com.runwaysdk.jstest.business.ontology;

@com.runwaysdk.business.ClassSignature(hash = -302890602)
/**
 * This class is generated automatically.
 * DO NOT MAKE CHANGES TO IT - THEY WILL BE OVERWRITTEN
 * Custom business logic should be added to Alphabet.java
 *
 * @author Autogenerated by RunwaySDK
 */
public abstract class AlphabetBase extends com.runwaysdk.business.ontology.Term implements com.runwaysdk.generation.loader.Reloadable
{
  private static final com.runwaysdk.business.ontology.OntologyStrategyIF strategy;
  static 
  {
    strategy =  com.runwaysdk.business.ontology.Term.assignStrategy("com.runwaysdk.jstest.business.ontology.Alphabet");
  }
  public final static String CLASS = "com.runwaysdk.jstest.business.ontology.Alphabet";
  public static java.lang.String CREATEDATE = "createDate";
  public static java.lang.String CREATEDBY = "createdBy";
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
  private static final long serialVersionUID = -302890602;
  
  public AlphabetBase()
  {
    super();
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
  
  public static com.runwaysdk.dataaccess.MdAttributeDAOIF getCreateDateMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.jstest.business.ontology.Alphabet.CLASS);
    return mdClassIF.definesAttribute(CREATEDATE);
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
  
  public static com.runwaysdk.dataaccess.MdAttributeDAOIF getCreatedByMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.jstest.business.ontology.Alphabet.CLASS);
    return mdClassIF.definesAttribute(CREATEDBY);
  }
  
  public com.runwaysdk.jstest.business.ontology.AlphabetDisplayLabel getDisplayLabel()
  {
    return (com.runwaysdk.jstest.business.ontology.AlphabetDisplayLabel) displayLabel;
  }
  
  public void validateDisplayLabel()
  {
    this.validateAttribute(DISPLAYLABEL);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeDAOIF getDisplayLabelMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.jstest.business.ontology.Alphabet.CLASS);
    return mdClassIF.definesAttribute(DISPLAYLABEL);
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
  
  public static com.runwaysdk.dataaccess.MdAttributeDAOIF getEntityDomainMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.jstest.business.ontology.Alphabet.CLASS);
    return mdClassIF.definesAttribute(ENTITYDOMAIN);
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
  
  public static com.runwaysdk.dataaccess.MdAttributeDAOIF getIdMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.jstest.business.ontology.Alphabet.CLASS);
    return mdClassIF.definesAttribute(ID);
  }
  
  public String getKeyName()
  {
    return getValue(KEYNAME);
  }
  
  public void validateKeyName()
  {
    this.validateAttribute(KEYNAME);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeDAOIF getKeyNameMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.jstest.business.ontology.Alphabet.CLASS);
    return mdClassIF.definesAttribute(KEYNAME);
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
  
  public static com.runwaysdk.dataaccess.MdAttributeDAOIF getLastUpdateDateMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.jstest.business.ontology.Alphabet.CLASS);
    return mdClassIF.definesAttribute(LASTUPDATEDATE);
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
  
  public static com.runwaysdk.dataaccess.MdAttributeDAOIF getLastUpdatedByMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.jstest.business.ontology.Alphabet.CLASS);
    return mdClassIF.definesAttribute(LASTUPDATEDBY);
  }
  
  public com.runwaysdk.system.Users getLockedBy()
  {
    if (getValue(LOCKEDBY).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.Users.get(getValue(LOCKEDBY));
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
  
  public static com.runwaysdk.dataaccess.MdAttributeDAOIF getLockedByMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.jstest.business.ontology.Alphabet.CLASS);
    return mdClassIF.definesAttribute(LOCKEDBY);
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
  
  public static com.runwaysdk.dataaccess.MdAttributeDAOIF getOwnerMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.jstest.business.ontology.Alphabet.CLASS);
    return mdClassIF.definesAttribute(OWNER);
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
  
  public static com.runwaysdk.dataaccess.MdAttributeDAOIF getSeqMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.jstest.business.ontology.Alphabet.CLASS);
    return mdClassIF.definesAttribute(SEQ);
  }
  
  public String getSiteMaster()
  {
    return getValue(SITEMASTER);
  }
  
  public void validateSiteMaster()
  {
    this.validateAttribute(SITEMASTER);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeDAOIF getSiteMasterMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.jstest.business.ontology.Alphabet.CLASS);
    return mdClassIF.definesAttribute(SITEMASTER);
  }
  
  public String getType()
  {
    return getValue(TYPE);
  }
  
  public void validateType()
  {
    this.validateAttribute(TYPE);
  }
  
  public static com.runwaysdk.dataaccess.MdAttributeDAOIF getTypeMd()
  {
    com.runwaysdk.dataaccess.MdClassDAOIF mdClassIF = com.runwaysdk.dataaccess.metadata.MdClassDAO.getMdClassDAO(com.runwaysdk.jstest.business.ontology.Alphabet.CLASS);
    return mdClassIF.definesAttribute(TYPE);
  }
  
  protected String getDeclaredType()
  {
    return CLASS;
  }
  
  public static AlphabetQuery getAllInstances(String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    AlphabetQuery query = new AlphabetQuery(new com.runwaysdk.query.QueryFactory());
    com.runwaysdk.business.Entity.getAllInstances(query, sortAttribute, ascending, pageSize, pageNumber);
    return query;
  }
  
  public com.runwaysdk.jstest.business.ontology.Sequential addChildTerm(com.runwaysdk.jstest.business.ontology.Alphabet alphabet)
  {
    return (com.runwaysdk.jstest.business.ontology.Sequential) addChild(alphabet, com.runwaysdk.jstest.business.ontology.Sequential.CLASS);
  }
  
  public void removeChildTerm(com.runwaysdk.jstest.business.ontology.Alphabet alphabet)
  {
    removeAllChildren(alphabet, com.runwaysdk.jstest.business.ontology.Sequential.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public com.runwaysdk.query.OIterator<? extends com.runwaysdk.jstest.business.ontology.Alphabet> getAllChildTerm()
  {
    return (com.runwaysdk.query.OIterator<? extends com.runwaysdk.jstest.business.ontology.Alphabet>) getChildren(com.runwaysdk.jstest.business.ontology.Sequential.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public com.runwaysdk.query.OIterator<? extends com.runwaysdk.jstest.business.ontology.Sequential> getAllChildTermRel()
  {
    return (com.runwaysdk.query.OIterator<? extends com.runwaysdk.jstest.business.ontology.Sequential>) getChildRelationships(com.runwaysdk.jstest.business.ontology.Sequential.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public com.runwaysdk.jstest.business.ontology.Sequential getChildTermRel(com.runwaysdk.jstest.business.ontology.Alphabet alphabet)
  {
    com.runwaysdk.query.OIterator<? extends com.runwaysdk.jstest.business.ontology.Sequential> iterator = (com.runwaysdk.query.OIterator<? extends com.runwaysdk.jstest.business.ontology.Sequential>) getRelationshipsWithChild(alphabet, com.runwaysdk.jstest.business.ontology.Sequential.CLASS);
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
  
  public com.runwaysdk.jstest.business.ontology.Sequential addParentTerm(com.runwaysdk.jstest.business.ontology.Alphabet alphabet)
  {
    return (com.runwaysdk.jstest.business.ontology.Sequential) addParent(alphabet, com.runwaysdk.jstest.business.ontology.Sequential.CLASS);
  }
  
  public void removeParentTerm(com.runwaysdk.jstest.business.ontology.Alphabet alphabet)
  {
    removeAllParents(alphabet, com.runwaysdk.jstest.business.ontology.Sequential.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public com.runwaysdk.query.OIterator<? extends com.runwaysdk.jstest.business.ontology.Alphabet> getAllParentTerm()
  {
    return (com.runwaysdk.query.OIterator<? extends com.runwaysdk.jstest.business.ontology.Alphabet>) getParents(com.runwaysdk.jstest.business.ontology.Sequential.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public com.runwaysdk.query.OIterator<? extends com.runwaysdk.jstest.business.ontology.Sequential> getAllParentTermRel()
  {
    return (com.runwaysdk.query.OIterator<? extends com.runwaysdk.jstest.business.ontology.Sequential>) getParentRelationships(com.runwaysdk.jstest.business.ontology.Sequential.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public com.runwaysdk.jstest.business.ontology.Sequential getParentTermRel(com.runwaysdk.jstest.business.ontology.Alphabet alphabet)
  {
    com.runwaysdk.query.OIterator<? extends com.runwaysdk.jstest.business.ontology.Sequential> iterator = (com.runwaysdk.query.OIterator<? extends com.runwaysdk.jstest.business.ontology.Sequential>) getRelationshipsWithParent(alphabet, com.runwaysdk.jstest.business.ontology.Sequential.CLASS);
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
  
  public static Alphabet get(String id)
  {
    return (Alphabet) com.runwaysdk.business.Business.get(id);
  }
  
  public static Alphabet getByKey(String key)
  {
    return (Alphabet) com.runwaysdk.business.Business.get(CLASS, key);
  }
  
  public static com.runwaysdk.business.ontology.OntologyStrategyIF getStrategy()
  {
    return strategy;
  }
  
  public static Alphabet lock(java.lang.String id)
  {
    Alphabet _instance = Alphabet.get(id);
    _instance.lock();
    
    return _instance;
  }
  
  public static Alphabet unlock(java.lang.String id)
  {
    Alphabet _instance = Alphabet.get(id);
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