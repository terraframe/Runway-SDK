package com.runwaysdk.system.metadata;

@com.runwaysdk.business.ClassSignature(hash = 1613571189)
public abstract class MdAttributeGraphReferenceDTOBase extends com.runwaysdk.system.metadata.MdAttributeConcreteDTO
{
  public final static String CLASS = "com.runwaysdk.system.metadata.MdAttributeGraphReference";
  private static final long serialVersionUID = 1613571189;
  
  protected MdAttributeGraphReferenceDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected MdAttributeGraphReferenceDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String LINKMDCLASS = "referenceMdVertex";
  public com.runwaysdk.system.metadata.MdVertexDTO getReferenceMdVertex()
  {
    if(getValue(LINKMDCLASS) == null || getValue(LINKMDCLASS).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.metadata.MdVertexDTO.get(getRequest(), getValue(LINKMDCLASS));
    }
  }
  
  public String getReferenceMdVertexOid()
  {
    return getValue(LINKMDCLASS);
  }
  
  public void setReferenceMdVertex(com.runwaysdk.system.metadata.MdVertexDTO value)
  {
    if(value == null)
    {
      setValue(LINKMDCLASS, "");
    }
    else
    {
      setValue(LINKMDCLASS, value.getOid());
    }
  }
  
  public boolean isReferenceMdVertexWritable()
  {
    return isWritable(LINKMDCLASS);
  }
  
  public boolean isReferenceMdVertexReadable()
  {
    return isReadable(LINKMDCLASS);
  }
  
  public boolean isReferenceMdVertexModified()
  {
    return isModified(LINKMDCLASS);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getReferenceMdVertexMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(LINKMDCLASS).getAttributeMdDTO();
  }
  
  public static com.runwaysdk.system.metadata.MdAttributeGraphReferenceDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String oid)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(oid);
    
    return (com.runwaysdk.system.metadata.MdAttributeGraphReferenceDTO) dto;
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
  
  public static com.runwaysdk.system.metadata.MdAttributeGraphReferenceQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.metadata.MdAttributeGraphReferenceQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.metadata.MdAttributeGraphReferenceDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdAttributeGraphReferenceDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdAttributeGraphReferenceDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdAttributeGraphReferenceDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdAttributeGraphReferenceDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdAttributeGraphReferenceDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdAttributeGraphReferenceDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}