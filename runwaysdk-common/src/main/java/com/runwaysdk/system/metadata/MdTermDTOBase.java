package com.runwaysdk.system.metadata;

@com.runwaysdk.business.ClassSignature(hash = -1244418031)
public abstract class MdTermDTOBase extends com.runwaysdk.system.metadata.MdBusinessDTO
{
  public final static String CLASS = "com.runwaysdk.system.metadata.MdTerm";
  private static final long serialVersionUID = -1244418031;
  
  protected MdTermDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected MdTermDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String STRATEGY = "strategy";
  public com.runwaysdk.system.metadata.ontology.OntologyStrategyDTO getStrategy()
  {
    if(getValue(STRATEGY) == null || getValue(STRATEGY).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.metadata.ontology.OntologyStrategyDTO.get(getRequest(), getValue(STRATEGY));
    }
  }
  
  public String getStrategyId()
  {
    return getValue(STRATEGY);
  }
  
  public void setStrategy(com.runwaysdk.system.metadata.ontology.OntologyStrategyDTO value)
  {
    if(value == null)
    {
      setValue(STRATEGY, "");
    }
    else
    {
      setValue(STRATEGY, value.getId());
    }
  }
  
  public boolean isStrategyWritable()
  {
    return isWritable(STRATEGY);
  }
  
  public boolean isStrategyReadable()
  {
    return isReadable(STRATEGY);
  }
  
  public boolean isStrategyModified()
  {
    return isModified(STRATEGY);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getStrategyMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(STRATEGY).getAttributeMdDTO();
  }
  
  public static com.runwaysdk.system.metadata.MdTermDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String id)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(id);
    
    return (com.runwaysdk.system.metadata.MdTermDTO) dto;
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
  
  public static com.runwaysdk.system.metadata.MdTermQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.metadata.MdTermQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.metadata.MdTermDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdTermDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdTermDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdTermDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdTermDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdTermDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdTermDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
