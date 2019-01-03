/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.system.metadata;

@com.runwaysdk.business.ClassSignature(hash = -1079557914)
public abstract class IndicatorCompositeDTOBase extends com.runwaysdk.system.metadata.IndicatorElementDTO
{
  public final static String CLASS = "com.runwaysdk.system.metadata.IndicatorComposite";
  private static final long serialVersionUID = -1079557914;
  
  protected IndicatorCompositeDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected IndicatorCompositeDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String LEFTOPERAND = "leftOperand";
  public static java.lang.String OPERATOR = "operator";
  public static java.lang.String PERCENTAGE = "percentage";
  public static java.lang.String RIGHTOPERAND = "rightOperand";
  public com.runwaysdk.system.metadata.IndicatorElementDTO getLeftOperand()
  {
    if(getValue(LEFTOPERAND) == null || getValue(LEFTOPERAND).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.metadata.IndicatorElementDTO.get(getRequest(), getValue(LEFTOPERAND));
    }
  }
  
  public String getLeftOperandId()
  {
    return getValue(LEFTOPERAND);
  }
  
  public void setLeftOperand(com.runwaysdk.system.metadata.IndicatorElementDTO value)
  {
    if(value == null)
    {
      setValue(LEFTOPERAND, "");
    }
    else
    {
      setValue(LEFTOPERAND, value.getOid());
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
  public java.util.List<com.runwaysdk.system.metadata.IndicatorOperatorDTO> getOperator()
  {
    return (java.util.List<com.runwaysdk.system.metadata.IndicatorOperatorDTO>) com.runwaysdk.transport.conversion.ConversionFacade.convertEnumDTOsFromEnumNames(getRequest(), com.runwaysdk.system.metadata.IndicatorOperatorDTO.CLASS, getEnumNames(OPERATOR));
  }
  
  public java.util.List<String> getOperatorEnumNames()
  {
    return getEnumNames(OPERATOR);
  }
  
  public void addOperator(com.runwaysdk.system.metadata.IndicatorOperatorDTO enumDTO)
  {
    addEnumItem(OPERATOR, enumDTO.toString());
  }
  
  public void removeOperator(com.runwaysdk.system.metadata.IndicatorOperatorDTO enumDTO)
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
  
  public Boolean getPercentage()
  {
    return com.runwaysdk.constants.MdAttributeBooleanUtil.getTypeSafeValue(getValue(PERCENTAGE));
  }
  
  public void setPercentage(Boolean value)
  {
    if(value == null)
    {
      setValue(PERCENTAGE, "");
    }
    else
    {
      setValue(PERCENTAGE, java.lang.Boolean.toString(value));
    }
  }
  
  public boolean isPercentageWritable()
  {
    return isWritable(PERCENTAGE);
  }
  
  public boolean isPercentageReadable()
  {
    return isReadable(PERCENTAGE);
  }
  
  public boolean isPercentageModified()
  {
    return isModified(PERCENTAGE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeBooleanMdDTO getPercentageMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeBooleanMdDTO) getAttributeDTO(PERCENTAGE).getAttributeMdDTO();
  }
  
  public com.runwaysdk.system.metadata.IndicatorElementDTO getRightOperand()
  {
    if(getValue(RIGHTOPERAND) == null || getValue(RIGHTOPERAND).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.metadata.IndicatorElementDTO.get(getRequest(), getValue(RIGHTOPERAND));
    }
  }
  
  public String getRightOperandId()
  {
    return getValue(RIGHTOPERAND);
  }
  
  public void setRightOperand(com.runwaysdk.system.metadata.IndicatorElementDTO value)
  {
    if(value == null)
    {
      setValue(RIGHTOPERAND, "");
    }
    else
    {
      setValue(RIGHTOPERAND, value.getOid());
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
  public java.util.List<? extends com.runwaysdk.system.metadata.MdAttributeIndicatorDTO> getAllGetMdAttributeIndicator()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdAttributeIndicatorDTO>) getRequest().getParents(this.getOid(), com.runwaysdk.system.metadata.AttributeIndicatorDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.MdAttributeIndicatorDTO> getAllGetMdAttributeIndicator(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdAttributeIndicatorDTO>) clientRequestIF.getParents(oid, com.runwaysdk.system.metadata.AttributeIndicatorDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.AttributeIndicatorDTO> getAllGetMdAttributeIndicatorRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.AttributeIndicatorDTO>) getRequest().getParentRelationships(this.getOid(), com.runwaysdk.system.metadata.AttributeIndicatorDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.AttributeIndicatorDTO> getAllGetMdAttributeIndicatorRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.AttributeIndicatorDTO>) clientRequestIF.getParentRelationships(oid, com.runwaysdk.system.metadata.AttributeIndicatorDTO.CLASS);
  }
  
  public com.runwaysdk.system.metadata.AttributeIndicatorDTO addGetMdAttributeIndicator(com.runwaysdk.system.metadata.MdAttributeIndicatorDTO parent)
  {
    return (com.runwaysdk.system.metadata.AttributeIndicatorDTO) getRequest().addParent(parent.getOid(), this.getOid(), com.runwaysdk.system.metadata.AttributeIndicatorDTO.CLASS);
  }
  
  public static com.runwaysdk.system.metadata.AttributeIndicatorDTO addGetMdAttributeIndicator(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid, com.runwaysdk.system.metadata.MdAttributeIndicatorDTO parent)
  {
    return (com.runwaysdk.system.metadata.AttributeIndicatorDTO) clientRequestIF.addParent(parent.getOid(), oid, com.runwaysdk.system.metadata.AttributeIndicatorDTO.CLASS);
  }
  
  public void removeGetMdAttributeIndicator(com.runwaysdk.system.metadata.AttributeIndicatorDTO relationship)
  {
    getRequest().deleteParent(relationship.getOid());
  }
  
  public static void removeGetMdAttributeIndicator(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.metadata.AttributeIndicatorDTO relationship)
  {
    clientRequestIF.deleteParent(relationship.getOid());
  }
  
  public void removeAllGetMdAttributeIndicator()
  {
    getRequest().deleteParents(this.getOid(), com.runwaysdk.system.metadata.AttributeIndicatorDTO.CLASS);
  }
  
  public static void removeAllGetMdAttributeIndicator(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    clientRequestIF.deleteParents(oid, com.runwaysdk.system.metadata.AttributeIndicatorDTO.CLASS);
  }
  
  public static com.runwaysdk.system.metadata.IndicatorCompositeDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String oid)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(oid);
    
    return (com.runwaysdk.system.metadata.IndicatorCompositeDTO) dto;
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
  
  public static com.runwaysdk.system.metadata.IndicatorCompositeQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.metadata.IndicatorCompositeQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.metadata.IndicatorCompositeDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.metadata.IndicatorCompositeDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.IndicatorCompositeDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.metadata.IndicatorCompositeDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.metadata.IndicatorCompositeDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.IndicatorCompositeDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.metadata.IndicatorCompositeDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
