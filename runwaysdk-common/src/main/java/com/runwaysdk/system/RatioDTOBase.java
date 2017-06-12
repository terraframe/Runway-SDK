package com.runwaysdk.system;

@com.runwaysdk.business.ClassSignature(hash = -1383140066)
public abstract class RatioDTOBase extends com.runwaysdk.system.RatioElementDTO
{
  public final static String CLASS = "com.runwaysdk.system.Ratio";
  private static final long serialVersionUID = -1383140066;
  
  protected RatioDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected RatioDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
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
  public com.runwaysdk.system.RatioElementDTO getLeftOperand()
  {
    if(getValue(LEFTOPERAND) == null || getValue(LEFTOPERAND).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.RatioElementDTO.get(getRequest(), getValue(LEFTOPERAND));
    }
  }
  
  public String getLeftOperandId()
  {
    return getValue(LEFTOPERAND);
  }
  
  public void setLeftOperand(com.runwaysdk.system.RatioElementDTO value)
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
  public java.util.List<com.runwaysdk.system.RatioOperatorDTO> getOperator()
  {
    return (java.util.List<com.runwaysdk.system.RatioOperatorDTO>) com.runwaysdk.transport.conversion.ConversionFacade.convertEnumDTOsFromEnumNames(getRequest(), com.runwaysdk.system.RatioOperatorDTO.CLASS, getEnumNames(OPERATOR));
  }
  
  public java.util.List<String> getOperatorEnumNames()
  {
    return getEnumNames(OPERATOR);
  }
  
  public void addOperator(com.runwaysdk.system.RatioOperatorDTO enumDTO)
  {
    addEnumItem(OPERATOR, enumDTO.toString());
  }
  
  public void removeOperator(com.runwaysdk.system.RatioOperatorDTO enumDTO)
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
  
  public com.runwaysdk.system.RatioElementDTO getRightOperand()
  {
    if(getValue(RIGHTOPERAND) == null || getValue(RIGHTOPERAND).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.RatioElementDTO.get(getRequest(), getValue(RIGHTOPERAND));
    }
  }
  
  public String getRightOperandId()
  {
    return getValue(RIGHTOPERAND);
  }
  
  public void setRightOperand(com.runwaysdk.system.RatioElementDTO value)
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
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.MdAttributeRatioDTO> getAllGetMdAttributeRatio()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdAttributeRatioDTO>) getRequest().getParents(this.getId(), com.runwaysdk.system.metadata.AttributeRatioDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.MdAttributeRatioDTO> getAllGetMdAttributeRatio(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdAttributeRatioDTO>) clientRequestIF.getParents(id, com.runwaysdk.system.metadata.AttributeRatioDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.AttributeRatioDTO> getAllGetMdAttributeRatioRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.AttributeRatioDTO>) getRequest().getParentRelationships(this.getId(), com.runwaysdk.system.metadata.AttributeRatioDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.AttributeRatioDTO> getAllGetMdAttributeRatioRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.AttributeRatioDTO>) clientRequestIF.getParentRelationships(id, com.runwaysdk.system.metadata.AttributeRatioDTO.CLASS);
  }
  
  public com.runwaysdk.system.metadata.AttributeRatioDTO addGetMdAttributeRatio(com.runwaysdk.system.metadata.MdAttributeRatioDTO parent)
  {
    return (com.runwaysdk.system.metadata.AttributeRatioDTO) getRequest().addParent(parent.getId(), this.getId(), com.runwaysdk.system.metadata.AttributeRatioDTO.CLASS);
  }
  
  public static com.runwaysdk.system.metadata.AttributeRatioDTO addGetMdAttributeRatio(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id, com.runwaysdk.system.metadata.MdAttributeRatioDTO parent)
  {
    return (com.runwaysdk.system.metadata.AttributeRatioDTO) clientRequestIF.addParent(parent.getId(), id, com.runwaysdk.system.metadata.AttributeRatioDTO.CLASS);
  }
  
  public void removeGetMdAttributeRatio(com.runwaysdk.system.metadata.AttributeRatioDTO relationship)
  {
    getRequest().deleteParent(relationship.getId());
  }
  
  public static void removeGetMdAttributeRatio(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.metadata.AttributeRatioDTO relationship)
  {
    clientRequestIF.deleteParent(relationship.getId());
  }
  
  public void removeAllGetMdAttributeRatio()
  {
    getRequest().deleteParents(this.getId(), com.runwaysdk.system.metadata.AttributeRatioDTO.CLASS);
  }
  
  public static void removeAllGetMdAttributeRatio(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    clientRequestIF.deleteParents(id, com.runwaysdk.system.metadata.AttributeRatioDTO.CLASS);
  }
  
  public static com.runwaysdk.system.RatioDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String id)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(id);
    
    return (com.runwaysdk.system.RatioDTO) dto;
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
  
  public static com.runwaysdk.system.RatioQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.RatioQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.RatioDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.RatioDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.RatioDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.RatioDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.RatioDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.RatioDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.RatioDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
