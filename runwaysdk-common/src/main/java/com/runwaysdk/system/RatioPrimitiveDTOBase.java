package com.runwaysdk.system;

@com.runwaysdk.business.ClassSignature(hash = 36107821)
public abstract class RatioPrimitiveDTOBase extends com.runwaysdk.system.RatioElementDTO
{
  public final static String CLASS = "com.runwaysdk.system.RatioPrimitive";
  private static final long serialVersionUID = 36107821;
  
  protected RatioPrimitiveDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected RatioPrimitiveDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String MDATTRIBUTEPRIMITIVE = "mdAttributePrimitive";
  public com.runwaysdk.system.metadata.MdAttributePrimitiveDTO getMdAttributePrimitive()
  {
    if(getValue(MDATTRIBUTEPRIMITIVE) == null || getValue(MDATTRIBUTEPRIMITIVE).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.metadata.MdAttributePrimitiveDTO.get(getRequest(), getValue(MDATTRIBUTEPRIMITIVE));
    }
  }
  
  public String getMdAttributePrimitiveId()
  {
    return getValue(MDATTRIBUTEPRIMITIVE);
  }
  
  public void setMdAttributePrimitive(com.runwaysdk.system.metadata.MdAttributePrimitiveDTO value)
  {
    if(value == null)
    {
      setValue(MDATTRIBUTEPRIMITIVE, "");
    }
    else
    {
      setValue(MDATTRIBUTEPRIMITIVE, value.getId());
    }
  }
  
  public boolean isMdAttributePrimitiveWritable()
  {
    return isWritable(MDATTRIBUTEPRIMITIVE);
  }
  
  public boolean isMdAttributePrimitiveReadable()
  {
    return isReadable(MDATTRIBUTEPRIMITIVE);
  }
  
  public boolean isMdAttributePrimitiveModified()
  {
    return isModified(MDATTRIBUTEPRIMITIVE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getMdAttributePrimitiveMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(MDATTRIBUTEPRIMITIVE).getAttributeMdDTO();
  }
  
  public static com.runwaysdk.system.RatioPrimitiveDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String id)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(id);
    
    return (com.runwaysdk.system.RatioPrimitiveDTO) dto;
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
  
  public static com.runwaysdk.system.RatioPrimitiveQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.RatioPrimitiveQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.RatioPrimitiveDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.RatioPrimitiveDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.RatioPrimitiveDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.RatioPrimitiveDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.RatioPrimitiveDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.RatioPrimitiveDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.RatioPrimitiveDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
