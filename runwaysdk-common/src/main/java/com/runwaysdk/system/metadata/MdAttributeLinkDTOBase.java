package com.runwaysdk.system.metadata;

@com.runwaysdk.business.ClassSignature(hash = 1613571189)
public abstract class MdAttributeLinkDTOBase extends com.runwaysdk.system.metadata.MdAttributeConcreteDTO
{
  public final static String CLASS = "com.runwaysdk.system.metadata.MdAttributeLink";
  private static final long serialVersionUID = 1613571189;
  
  protected MdAttributeLinkDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected MdAttributeLinkDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String LINKMDCLASS = "linkMdClass";
  public com.runwaysdk.system.metadata.MdVertexDTO getLinkMdClass()
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
  
  public String getLinkMdClassOid()
  {
    return getValue(LINKMDCLASS);
  }
  
  public void setLinkMdClass(com.runwaysdk.system.metadata.MdVertexDTO value)
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
  
  public boolean isLinkMdClassWritable()
  {
    return isWritable(LINKMDCLASS);
  }
  
  public boolean isLinkMdClassReadable()
  {
    return isReadable(LINKMDCLASS);
  }
  
  public boolean isLinkMdClassModified()
  {
    return isModified(LINKMDCLASS);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getLinkMdClassMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(LINKMDCLASS).getAttributeMdDTO();
  }
  
  public static com.runwaysdk.system.metadata.MdAttributeLinkDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String oid)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(oid);
    
    return (com.runwaysdk.system.metadata.MdAttributeLinkDTO) dto;
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
  
  public static com.runwaysdk.system.metadata.MdAttributeLinkQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.metadata.MdAttributeLinkQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.metadata.MdAttributeLinkDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdAttributeLinkDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdAttributeLinkDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdAttributeLinkDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdAttributeLinkDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdAttributeLinkDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdAttributeLinkDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
