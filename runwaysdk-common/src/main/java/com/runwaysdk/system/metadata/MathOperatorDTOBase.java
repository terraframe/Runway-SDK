package com.runwaysdk.system.metadata;

@com.runwaysdk.business.ClassSignature(hash = 656843933)
public abstract class MathOperatorDTOBase extends com.runwaysdk.system.EnumerationMasterDTO
{
  public final static String CLASS = "com.runwaysdk.system.metadata.MathOperator";
  private static final long serialVersionUID = 656843933;
  
  protected MathOperatorDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected MathOperatorDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String OPERATORSYMBOL = "operatorSymbol";
  public String getOperatorSymbol()
  {
    return getValue(OPERATORSYMBOL);
  }
  
  public void setOperatorSymbol(String value)
  {
    if(value == null)
    {
      setValue(OPERATORSYMBOL, "");
    }
    else
    {
      setValue(OPERATORSYMBOL, value);
    }
  }
  
  public boolean isOperatorSymbolWritable()
  {
    return isWritable(OPERATORSYMBOL);
  }
  
  public boolean isOperatorSymbolReadable()
  {
    return isReadable(OPERATORSYMBOL);
  }
  
  public boolean isOperatorSymbolModified()
  {
    return isModified(OPERATORSYMBOL);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getOperatorSymbolMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(OPERATORSYMBOL).getAttributeMdDTO();
  }
  
  public static com.runwaysdk.system.metadata.MathOperatorDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String id)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(id);
    
    return (com.runwaysdk.system.metadata.MathOperatorDTO) dto;
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
  
  public static com.runwaysdk.system.metadata.MathOperatorQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.metadata.MathOperatorQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.metadata.MathOperatorDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.metadata.MathOperatorDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MathOperatorDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MathOperatorDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.metadata.MathOperatorDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MathOperatorDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MathOperatorDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
