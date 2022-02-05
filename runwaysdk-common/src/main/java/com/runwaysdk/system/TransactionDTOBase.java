package com.runwaysdk.system;

@com.runwaysdk.business.ClassSignature(hash = -1814541037)
public abstract class TransactionDTOBase extends com.runwaysdk.business.StructDTO
{
  public final static String CLASS = "com.runwaysdk.system.Transaction";
  @SuppressWarnings("unused")
  private static final long serialVersionUID = -1814541037;
  
  protected TransactionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given StructDTO into a new DTO.
  * 
  * @param structDTO The StructDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected TransactionDTOBase(com.runwaysdk.business.StructDTO structDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(structDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String ACTION = "action";
  public static java.lang.String DATAOBJECTID = "dataObjectID";
  public static java.lang.String KEYNAME = "keyName";
  public static java.lang.String OID = "oid";
  public static java.lang.String SITEMASTER = "siteMaster";
  public static java.lang.String TRANSACTIONID = "transactionID";
  public String getAction()
  {
    return getValue(ACTION);
  }
  
  public void setAction(String value)
  {
    if(value == null)
    {
      setValue(ACTION, "");
    }
    else
    {
      setValue(ACTION, value);
    }
  }
  
  public boolean isActionWritable()
  {
    return isWritable(ACTION);
  }
  
  public boolean isActionReadable()
  {
    return isReadable(ACTION);
  }
  
  public boolean isActionModified()
  {
    return isModified(ACTION);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getActionMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(ACTION).getAttributeMdDTO();
  }
  
  public String getDataObjectID()
  {
    return getValue(DATAOBJECTID);
  }
  
  public void setDataObjectID(String value)
  {
    if(value == null)
    {
      setValue(DATAOBJECTID, "");
    }
    else
    {
      setValue(DATAOBJECTID, value);
    }
  }
  
  public boolean isDataObjectIDWritable()
  {
    return isWritable(DATAOBJECTID);
  }
  
  public boolean isDataObjectIDReadable()
  {
    return isReadable(DATAOBJECTID);
  }
  
  public boolean isDataObjectIDModified()
  {
    return isModified(DATAOBJECTID);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getDataObjectIDMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(DATAOBJECTID).getAttributeMdDTO();
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
  
  public Integer getTransactionID()
  {
    return com.runwaysdk.constants.MdAttributeIntegerUtil.getTypeSafeValue(getValue(TRANSACTIONID));
  }
  
  public void setTransactionID(Integer value)
  {
    if(value == null)
    {
      setValue(TRANSACTIONID, "");
    }
    else
    {
      setValue(TRANSACTIONID, java.lang.Integer.toString(value));
    }
  }
  
  public boolean isTransactionIDWritable()
  {
    return isWritable(TRANSACTIONID);
  }
  
  public boolean isTransactionIDReadable()
  {
    return isReadable(TRANSACTIONID);
  }
  
  public boolean isTransactionIDModified()
  {
    return isModified(TRANSACTIONID);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeNumberMdDTO getTransactionIDMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeNumberMdDTO) getAttributeDTO(TRANSACTIONID).getAttributeMdDTO();
  }
  
  public static TransactionDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String oid)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(oid);
    
    return (TransactionDTO) dto;
  }
  
  public void apply()
  {
    if(isNewInstance())
    {
      getRequest().createStruct(this);
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
  
  public static com.runwaysdk.system.TransactionQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.TransactionQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.TransactionDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
}
