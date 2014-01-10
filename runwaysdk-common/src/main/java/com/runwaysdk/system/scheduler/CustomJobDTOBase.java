package com.runwaysdk.system.scheduler;

@com.runwaysdk.business.ClassSignature(hash = -275440794)
public abstract class CustomJobDTOBase extends com.runwaysdk.system.scheduler.JobDTO
{
  public final static String CLASS = "com.runwaysdk.system.scheduler.CustomJob";
  private static final long serialVersionUID = -275440794;
  
  protected CustomJobDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected CustomJobDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String CLASSNAME = "className";
  public String getClassName()
  {
    return getValue(CLASSNAME);
  }
  
  public void setClassName(String value)
  {
    if(value == null)
    {
      setValue(CLASSNAME, "");
    }
    else
    {
      setValue(CLASSNAME, value);
    }
  }
  
  public boolean isClassNameWritable()
  {
    return isWritable(CLASSNAME);
  }
  
  public boolean isClassNameReadable()
  {
    return isReadable(CLASSNAME);
  }
  
  public boolean isClassNameModified()
  {
    return isModified(CLASSNAME);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getClassNameMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(CLASSNAME).getAttributeMdDTO();
  }
  
  public static com.runwaysdk.system.scheduler.CustomJobDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String id)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(id);
    
    return (com.runwaysdk.system.scheduler.CustomJobDTO) dto;
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
  
  public static com.runwaysdk.system.scheduler.CustomJobQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.scheduler.CustomJobQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.scheduler.CustomJobDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.scheduler.CustomJobDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.scheduler.CustomJobDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.scheduler.CustomJobDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.scheduler.CustomJobDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.scheduler.CustomJobDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.scheduler.CustomJobDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
