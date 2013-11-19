package com.runwaysdk.system.metadata;

@com.runwaysdk.business.ClassSignature(hash = -317661884)
public abstract class MdAttributeMultiReferenceDTOBase extends com.runwaysdk.system.metadata.MdAttributeConcreteDTO
{
  public final static String CLASS = "com.runwaysdk.system.metadata.MdAttributeMultiReference";
  private static final long serialVersionUID = -317661884;
  
  protected MdAttributeMultiReferenceDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected MdAttributeMultiReferenceDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String DEFAULTVALUE = "defaultValue";
  public static java.lang.String MDBUSINESS = "mdBusiness";
  public static java.lang.String TABLENAME = "tableName";
  public String getDefaultValue()
  {
    return getValue(DEFAULTVALUE);
  }
  
  public void setDefaultValue(String value)
  {
    if(value == null)
    {
      setValue(DEFAULTVALUE, "");
    }
    else
    {
      setValue(DEFAULTVALUE, value);
    }
  }
  
  public boolean isDefaultValueWritable()
  {
    return isWritable(DEFAULTVALUE);
  }
  
  public boolean isDefaultValueReadable()
  {
    return isReadable(DEFAULTVALUE);
  }
  
  public boolean isDefaultValueModified()
  {
    return isModified(DEFAULTVALUE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getDefaultValueMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(DEFAULTVALUE).getAttributeMdDTO();
  }
  
  public com.runwaysdk.system.metadata.MdBusinessDTO getMdBusiness()
  {
    if(getValue(MDBUSINESS) == null || getValue(MDBUSINESS).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.metadata.MdBusinessDTO.get(getRequest(), getValue(MDBUSINESS));
    }
  }
  
  public String getMdBusinessId()
  {
    return getValue(MDBUSINESS);
  }
  
  public void setMdBusiness(com.runwaysdk.system.metadata.MdBusinessDTO value)
  {
    if(value == null)
    {
      setValue(MDBUSINESS, "");
    }
    else
    {
      setValue(MDBUSINESS, value.getId());
    }
  }
  
  public boolean isMdBusinessWritable()
  {
    return isWritable(MDBUSINESS);
  }
  
  public boolean isMdBusinessReadable()
  {
    return isReadable(MDBUSINESS);
  }
  
  public boolean isMdBusinessModified()
  {
    return isModified(MDBUSINESS);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getMdBusinessMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(MDBUSINESS).getAttributeMdDTO();
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
  
  public static com.runwaysdk.system.metadata.MdAttributeMultiReferenceDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String id)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(id);
    
    return (com.runwaysdk.system.metadata.MdAttributeMultiReferenceDTO) dto;
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
  
  public static com.runwaysdk.system.metadata.MdAttributeMultiReferenceQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.metadata.MdAttributeMultiReferenceQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.metadata.MdAttributeMultiReferenceDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdAttributeMultiReferenceDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdAttributeMultiReferenceDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdAttributeMultiReferenceDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdAttributeMultiReferenceDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdAttributeMultiReferenceDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdAttributeMultiReferenceDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
