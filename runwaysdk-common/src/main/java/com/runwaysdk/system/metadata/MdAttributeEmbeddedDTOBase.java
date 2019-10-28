package com.runwaysdk.system.metadata;

@com.runwaysdk.business.ClassSignature(hash = 1900722321)
public abstract class MdAttributeEmbeddedDTOBase extends com.runwaysdk.system.metadata.MdAttributeConcreteDTO
{
  public final static String CLASS = "com.runwaysdk.system.metadata.MdAttributeEmbedded";
  private static final long serialVersionUID = 1900722321;
  
  protected MdAttributeEmbeddedDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected MdAttributeEmbeddedDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String EMBEDDEDMDCLASS = "embeddedMdClass";
  public com.runwaysdk.system.metadata.MdClassDTO getEmbeddedMdClass()
  {
    if(getValue(EMBEDDEDMDCLASS) == null || getValue(EMBEDDEDMDCLASS).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.metadata.MdClassDTO.get(getRequest(), getValue(EMBEDDEDMDCLASS));
    }
  }
  
  public String getEmbeddedMdClassOid()
  {
    return getValue(EMBEDDEDMDCLASS);
  }
  
  public void setEmbeddedMdClass(com.runwaysdk.system.metadata.MdClassDTO value)
  {
    if(value == null)
    {
      setValue(EMBEDDEDMDCLASS, "");
    }
    else
    {
      setValue(EMBEDDEDMDCLASS, value.getOid());
    }
  }
  
  public boolean isEmbeddedMdClassWritable()
  {
    return isWritable(EMBEDDEDMDCLASS);
  }
  
  public boolean isEmbeddedMdClassReadable()
  {
    return isReadable(EMBEDDEDMDCLASS);
  }
  
  public boolean isEmbeddedMdClassModified()
  {
    return isModified(EMBEDDEDMDCLASS);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getEmbeddedMdClassMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(EMBEDDEDMDCLASS).getAttributeMdDTO();
  }
  
  public static com.runwaysdk.system.metadata.MdAttributeEmbeddedDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String oid)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(oid);
    
    return (com.runwaysdk.system.metadata.MdAttributeEmbeddedDTO) dto;
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
  
  public static com.runwaysdk.system.metadata.MdAttributeEmbeddedQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.metadata.MdAttributeEmbeddedQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.metadata.MdAttributeEmbeddedDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdAttributeEmbeddedDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdAttributeEmbeddedDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdAttributeEmbeddedDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdAttributeEmbeddedDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdAttributeEmbeddedDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdAttributeEmbeddedDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
