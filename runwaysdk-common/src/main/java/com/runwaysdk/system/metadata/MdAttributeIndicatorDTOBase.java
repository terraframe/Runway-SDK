package com.runwaysdk.system.metadata;

@com.runwaysdk.business.ClassSignature(hash = -1329126206)
public abstract class MdAttributeIndicatorDTOBase extends com.runwaysdk.system.metadata.MdAttributeConcreteDTO
{
  public final static String CLASS = "com.runwaysdk.system.metadata.MdAttributeIndicator";
  private static final long serialVersionUID = -1329126206;
  
  protected MdAttributeIndicatorDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected MdAttributeIndicatorDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String INDICATORELEMENT = "indicatorElement";
  public com.runwaysdk.system.metadata.IndicatorElementDTO getIndicatorElement()
  {
    if(getValue(INDICATORELEMENT) == null || getValue(INDICATORELEMENT).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.metadata.IndicatorElementDTO.get(getRequest(), getValue(INDICATORELEMENT));
    }
  }
  
  public String getIndicatorElementId()
  {
    return getValue(INDICATORELEMENT);
  }
  
  public void setIndicatorElement(com.runwaysdk.system.metadata.IndicatorElementDTO value)
  {
    if(value == null)
    {
      setValue(INDICATORELEMENT, "");
    }
    else
    {
      setValue(INDICATORELEMENT, value.getId());
    }
  }
  
  public boolean isIndicatorElementWritable()
  {
    return isWritable(INDICATORELEMENT);
  }
  
  public boolean isIndicatorElementReadable()
  {
    return isReadable(INDICATORELEMENT);
  }
  
  public boolean isIndicatorElementModified()
  {
    return isModified(INDICATORELEMENT);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getIndicatorElementMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(INDICATORELEMENT).getAttributeMdDTO();
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.IndicatorCompositeDTO> getAllGetReferencedIndicator()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.IndicatorCompositeDTO>) getRequest().getChildren(this.getId(), com.runwaysdk.system.metadata.AttributeIndicatorDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.IndicatorCompositeDTO> getAllGetReferencedIndicator(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.IndicatorCompositeDTO>) clientRequestIF.getChildren(id, com.runwaysdk.system.metadata.AttributeIndicatorDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.AttributeIndicatorDTO> getAllGetReferencedIndicatorRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.AttributeIndicatorDTO>) getRequest().getChildRelationships(this.getId(), com.runwaysdk.system.metadata.AttributeIndicatorDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.AttributeIndicatorDTO> getAllGetReferencedIndicatorRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.AttributeIndicatorDTO>) clientRequestIF.getChildRelationships(id, com.runwaysdk.system.metadata.AttributeIndicatorDTO.CLASS);
  }
  
  public com.runwaysdk.system.metadata.AttributeIndicatorDTO addGetReferencedIndicator(com.runwaysdk.system.metadata.IndicatorCompositeDTO child)
  {
    return (com.runwaysdk.system.metadata.AttributeIndicatorDTO) getRequest().addChild(this.getId(), child.getId(), com.runwaysdk.system.metadata.AttributeIndicatorDTO.CLASS);
  }
  
  public static com.runwaysdk.system.metadata.AttributeIndicatorDTO addGetReferencedIndicator(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id, com.runwaysdk.system.metadata.IndicatorCompositeDTO child)
  {
    return (com.runwaysdk.system.metadata.AttributeIndicatorDTO) clientRequestIF.addChild(id, child.getId(), com.runwaysdk.system.metadata.AttributeIndicatorDTO.CLASS);
  }
  
  public void removeGetReferencedIndicator(com.runwaysdk.system.metadata.AttributeIndicatorDTO relationship)
  {
    getRequest().deleteChild(relationship.getId());
  }
  
  public static void removeGetReferencedIndicator(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.metadata.AttributeIndicatorDTO relationship)
  {
    clientRequestIF.deleteChild(relationship.getId());
  }
  
  public void removeAllGetReferencedIndicator()
  {
    getRequest().deleteChildren(this.getId(), com.runwaysdk.system.metadata.AttributeIndicatorDTO.CLASS);
  }
  
  public static void removeAllGetReferencedIndicator(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    clientRequestIF.deleteChildren(id, com.runwaysdk.system.metadata.AttributeIndicatorDTO.CLASS);
  }
  
  public static com.runwaysdk.system.metadata.MdAttributeIndicatorDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String id)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(id);
    
    return (com.runwaysdk.system.metadata.MdAttributeIndicatorDTO) dto;
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
  
  public static com.runwaysdk.system.metadata.MdAttributeIndicatorQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.metadata.MdAttributeIndicatorQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.metadata.MdAttributeIndicatorDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdAttributeIndicatorDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdAttributeIndicatorDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdAttributeIndicatorDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdAttributeIndicatorDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdAttributeIndicatorDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdAttributeIndicatorDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
