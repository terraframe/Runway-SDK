package com.runwaysdk.system.metadata;

@com.runwaysdk.business.ClassSignature(hash = 380976279)
public abstract class MdAttributeRatioDTOBase extends com.runwaysdk.system.metadata.MdAttributeConcreteDTO
{
  public final static String CLASS = "com.runwaysdk.system.metadata.MdAttributeRatio";
  private static final long serialVersionUID = 380976279;
  
  protected MdAttributeRatioDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected MdAttributeRatioDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String RATIO = "ratio";
  public com.runwaysdk.system.RatioDTO getRatio()
  {
    if(getValue(RATIO) == null || getValue(RATIO).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.RatioDTO.get(getRequest(), getValue(RATIO));
    }
  }
  
  public String getRatioId()
  {
    return getValue(RATIO);
  }
  
  public void setRatio(com.runwaysdk.system.RatioDTO value)
  {
    if(value == null)
    {
      setValue(RATIO, "");
    }
    else
    {
      setValue(RATIO, value.getId());
    }
  }
  
  public boolean isRatioWritable()
  {
    return isWritable(RATIO);
  }
  
  public boolean isRatioReadable()
  {
    return isReadable(RATIO);
  }
  
  public boolean isRatioModified()
  {
    return isModified(RATIO);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getRatioMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(RATIO).getAttributeMdDTO();
  }
  
  public static com.runwaysdk.system.metadata.MdAttributeRatioDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String id)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(id);
    
    return (com.runwaysdk.system.metadata.MdAttributeRatioDTO) dto;
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
  
  public static com.runwaysdk.system.metadata.MdAttributeRatioQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.metadata.MdAttributeRatioQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.metadata.MdAttributeRatioDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdAttributeRatioDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdAttributeRatioDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdAttributeRatioDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdAttributeRatioDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdAttributeRatioDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdAttributeRatioDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
