package com.runwaysdk.system;

@com.runwaysdk.business.ClassSignature(hash = -112489993)
public abstract class MathOperationsDTOBase extends com.runwaysdk.system.EnumerationMasterDTO
{
  public final static String CLASS = "com.runwaysdk.system.MathOperations";
  private static final long serialVersionUID = -112489993;
  
  protected MathOperationsDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected MathOperationsDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String OPERATIONSYMBOL = "operationSymbol";
  public String getOperationSymbol()
  {
    return getValue(OPERATIONSYMBOL);
  }
  
  public void setOperationSymbol(String value)
  {
    if(value == null)
    {
      setValue(OPERATIONSYMBOL, "");
    }
    else
    {
      setValue(OPERATIONSYMBOL, value);
    }
  }
  
  public boolean isOperationSymbolWritable()
  {
    return isWritable(OPERATIONSYMBOL);
  }
  
  public boolean isOperationSymbolReadable()
  {
    return isReadable(OPERATIONSYMBOL);
  }
  
  public boolean isOperationSymbolModified()
  {
    return isModified(OPERATIONSYMBOL);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getOperationSymbolMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(OPERATIONSYMBOL).getAttributeMdDTO();
  }
  
  public static com.runwaysdk.system.MathOperationsDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String id)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(id);
    
    return (com.runwaysdk.system.MathOperationsDTO) dto;
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
  
  public static com.runwaysdk.system.MathOperationsQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.MathOperationsQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.MathOperationsDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.MathOperationsDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.MathOperationsDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.MathOperationsDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.MathOperationsDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.MathOperationsDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.MathOperationsDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
