package com.runwaysdk.system.metadata;

@com.runwaysdk.business.ClassSignature(hash = -1357308165)
public abstract class MdAttributeRatioDTOBase extends com.runwaysdk.system.metadata.MdAttributeConcreteDTO
{
  public final static String CLASS = "com.runwaysdk.system.metadata.MdAttributeRatio";
  private static final long serialVersionUID = -1357308165;
  
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
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.RatioDTO> getAllGetReferencedRatio()
  {
    return (java.util.List<? extends com.runwaysdk.system.RatioDTO>) getRequest().getChildren(this.getId(), com.runwaysdk.system.metadata.AttributeRatioDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.RatioDTO> getAllGetReferencedRatio(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.RatioDTO>) clientRequestIF.getChildren(id, com.runwaysdk.system.metadata.AttributeRatioDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.AttributeRatioDTO> getAllGetReferencedRatioRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.AttributeRatioDTO>) getRequest().getChildRelationships(this.getId(), com.runwaysdk.system.metadata.AttributeRatioDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.AttributeRatioDTO> getAllGetReferencedRatioRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.AttributeRatioDTO>) clientRequestIF.getChildRelationships(id, com.runwaysdk.system.metadata.AttributeRatioDTO.CLASS);
  }
  
  public com.runwaysdk.system.metadata.AttributeRatioDTO addGetReferencedRatio(com.runwaysdk.system.RatioDTO child)
  {
    return (com.runwaysdk.system.metadata.AttributeRatioDTO) getRequest().addChild(this.getId(), child.getId(), com.runwaysdk.system.metadata.AttributeRatioDTO.CLASS);
  }
  
  public static com.runwaysdk.system.metadata.AttributeRatioDTO addGetReferencedRatio(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id, com.runwaysdk.system.RatioDTO child)
  {
    return (com.runwaysdk.system.metadata.AttributeRatioDTO) clientRequestIF.addChild(id, child.getId(), com.runwaysdk.system.metadata.AttributeRatioDTO.CLASS);
  }
  
  public void removeGetReferencedRatio(com.runwaysdk.system.metadata.AttributeRatioDTO relationship)
  {
    getRequest().deleteChild(relationship.getId());
  }
  
  public static void removeGetReferencedRatio(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.metadata.AttributeRatioDTO relationship)
  {
    clientRequestIF.deleteChild(relationship.getId());
  }
  
  public void removeAllGetReferencedRatio()
  {
    getRequest().deleteChildren(this.getId(), com.runwaysdk.system.metadata.AttributeRatioDTO.CLASS);
  }
  
  public static void removeAllGetReferencedRatio(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    clientRequestIF.deleteChildren(id, com.runwaysdk.system.metadata.AttributeRatioDTO.CLASS);
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
