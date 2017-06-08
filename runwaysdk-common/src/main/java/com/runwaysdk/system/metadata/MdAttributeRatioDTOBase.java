package com.runwaysdk.system.metadata;

@com.runwaysdk.business.ClassSignature(hash = -1725456725)
public abstract class MdAttributeRatioDTOBase extends com.runwaysdk.system.metadata.MdAttributeConcreteDTO
{
  public final static String CLASS = "com.runwaysdk.system.metadata.MdAttributeRatio";
  private static final long serialVersionUID = -1725456725;
  
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
  
  public static java.lang.String LEFTOPERAND = "leftOperand";
  public static java.lang.String OPERATOR = "operator";
  public static java.lang.String RIGHTOPERAND = "rightOperand";
  public com.runwaysdk.system.metadata.MdAttributeDTO getLeftOperand()
  {
    if(getValue(LEFTOPERAND) == null || getValue(LEFTOPERAND).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.metadata.MdAttributeDTO.get(getRequest(), getValue(LEFTOPERAND));
    }
  }
  
  public String getLeftOperandId()
  {
    return getValue(LEFTOPERAND);
  }
  
  public void setLeftOperand(com.runwaysdk.system.metadata.MdAttributeDTO value)
  {
    if(value == null)
    {
      setValue(LEFTOPERAND, "");
    }
    else
    {
      setValue(LEFTOPERAND, value.getId());
    }
  }
  
  public boolean isLeftOperandWritable()
  {
    return isWritable(LEFTOPERAND);
  }
  
  public boolean isLeftOperandReadable()
  {
    return isReadable(LEFTOPERAND);
  }
  
  public boolean isLeftOperandModified()
  {
    return isModified(LEFTOPERAND);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getLeftOperandMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(LEFTOPERAND).getAttributeMdDTO();
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<com.runwaysdk.system.RatioOperatorsDTO> getOperator()
  {
    return (java.util.List<com.runwaysdk.system.RatioOperatorsDTO>) com.runwaysdk.transport.conversion.ConversionFacade.convertEnumDTOsFromEnumNames(getRequest(), com.runwaysdk.system.RatioOperatorsDTO.CLASS, getEnumNames(OPERATOR));
  }
  
  public java.util.List<String> getOperatorEnumNames()
  {
    return getEnumNames(OPERATOR);
  }
  
  public void addOperator(com.runwaysdk.system.RatioOperatorsDTO enumDTO)
  {
    addEnumItem(OPERATOR, enumDTO.toString());
  }
  
  public void removeOperator(com.runwaysdk.system.RatioOperatorsDTO enumDTO)
  {
    removeEnumItem(OPERATOR, enumDTO.toString());
  }
  
  public void clearOperator()
  {
    clearEnum(OPERATOR);
  }
  
  public boolean isOperatorWritable()
  {
    return isWritable(OPERATOR);
  }
  
  public boolean isOperatorReadable()
  {
    return isReadable(OPERATOR);
  }
  
  public boolean isOperatorModified()
  {
    return isModified(OPERATOR);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeEnumerationMdDTO getOperatorMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeEnumerationMdDTO) getAttributeDTO(OPERATOR).getAttributeMdDTO();
  }
  
  public com.runwaysdk.system.metadata.MdAttributeDTO getRightOperand()
  {
    if(getValue(RIGHTOPERAND) == null || getValue(RIGHTOPERAND).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.metadata.MdAttributeDTO.get(getRequest(), getValue(RIGHTOPERAND));
    }
  }
  
  public String getRightOperandId()
  {
    return getValue(RIGHTOPERAND);
  }
  
  public void setRightOperand(com.runwaysdk.system.metadata.MdAttributeDTO value)
  {
    if(value == null)
    {
      setValue(RIGHTOPERAND, "");
    }
    else
    {
      setValue(RIGHTOPERAND, value.getId());
    }
  }
  
  public boolean isRightOperandWritable()
  {
    return isWritable(RIGHTOPERAND);
  }
  
  public boolean isRightOperandReadable()
  {
    return isReadable(RIGHTOPERAND);
  }
  
  public boolean isRightOperandModified()
  {
    return isModified(RIGHTOPERAND);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getRightOperandMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(RIGHTOPERAND).getAttributeMdDTO();
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
