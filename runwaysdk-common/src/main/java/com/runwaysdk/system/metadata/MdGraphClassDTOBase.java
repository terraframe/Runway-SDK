package com.runwaysdk.system.metadata;

@com.runwaysdk.business.ClassSignature(hash = 1862731666)
public abstract class MdGraphClassDTOBase extends com.runwaysdk.system.metadata.MdClassDTO
{
  public final static String CLASS = "com.runwaysdk.system.metadata.MdGraphClass";
  private static final long serialVersionUID = 1862731666;
  
  protected MdGraphClassDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected MdGraphClassDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String DBCLASSNAME = "dbClassName";
  public static java.lang.String ENABLECHANGEOVERTIME = "enableChangeOverTime";
  public String getDbClassName()
  {
    return getValue(DBCLASSNAME);
  }
  
  public void setDbClassName(String value)
  {
    if(value == null)
    {
      setValue(DBCLASSNAME, "");
    }
    else
    {
      setValue(DBCLASSNAME, value);
    }
  }
  
  public boolean isDbClassNameWritable()
  {
    return isWritable(DBCLASSNAME);
  }
  
  public boolean isDbClassNameReadable()
  {
    return isReadable(DBCLASSNAME);
  }
  
  public boolean isDbClassNameModified()
  {
    return isModified(DBCLASSNAME);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getDbClassNameMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(DBCLASSNAME).getAttributeMdDTO();
  }
  
  public Boolean getEnableChangeOverTime()
  {
    return com.runwaysdk.constants.MdAttributeBooleanUtil.getTypeSafeValue(getValue(ENABLECHANGEOVERTIME));
  }
  
  public void setEnableChangeOverTime(Boolean value)
  {
    if(value == null)
    {
      setValue(ENABLECHANGEOVERTIME, "");
    }
    else
    {
      setValue(ENABLECHANGEOVERTIME, java.lang.Boolean.toString(value));
    }
  }
  
  public boolean isEnableChangeOverTimeWritable()
  {
    return isWritable(ENABLECHANGEOVERTIME);
  }
  
  public boolean isEnableChangeOverTimeReadable()
  {
    return isReadable(ENABLECHANGEOVERTIME);
  }
  
  public boolean isEnableChangeOverTimeModified()
  {
    return isModified(ENABLECHANGEOVERTIME);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeBooleanMdDTO getEnableChangeOverTimeMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeBooleanMdDTO) getAttributeDTO(ENABLECHANGEOVERTIME).getAttributeMdDTO();
  }
  
  public static com.runwaysdk.system.metadata.MdGraphClassDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String oid)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(oid);
    
    return (com.runwaysdk.system.metadata.MdGraphClassDTO) dto;
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
  
  public static com.runwaysdk.system.metadata.MdGraphClassQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.metadata.MdGraphClassQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.metadata.MdGraphClassDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdGraphClassDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdGraphClassDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdGraphClassDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdGraphClassDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdGraphClassDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdGraphClassDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
