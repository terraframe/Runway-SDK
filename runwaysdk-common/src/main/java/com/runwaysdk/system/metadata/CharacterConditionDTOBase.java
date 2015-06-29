/**
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved.
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

@com.runwaysdk.business.ClassSignature(hash = 1039416481)
public abstract class CharacterConditionDTOBase extends com.runwaysdk.system.metadata.FieldConditionDTO
{
  public final static String CLASS = "com.runwaysdk.system.metadata.CharacterCondition";
  private static final long serialVersionUID = 1039416481;
  
  protected CharacterConditionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected CharacterConditionDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String DEFININGMDFIELD = "definingMdField";
  public static java.lang.String OPERATION = "operation";
  public static java.lang.String VALUE = "value";
  public com.runwaysdk.system.metadata.MdFieldDTO getDefiningMdField()
  {
    if(getValue(DEFININGMDFIELD) == null || getValue(DEFININGMDFIELD).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.metadata.MdFieldDTO.get(getRequest(), getValue(DEFININGMDFIELD));
    }
  }
  
  public String getDefiningMdFieldId()
  {
    return getValue(DEFININGMDFIELD);
  }
  
  public void setDefiningMdField(com.runwaysdk.system.metadata.MdFieldDTO value)
  {
    if(value == null)
    {
      setValue(DEFININGMDFIELD, "");
    }
    else
    {
      setValue(DEFININGMDFIELD, value.getId());
    }
  }
  
  public boolean isDefiningMdFieldWritable()
  {
    return isWritable(DEFININGMDFIELD);
  }
  
  public boolean isDefiningMdFieldReadable()
  {
    return isReadable(DEFININGMDFIELD);
  }
  
  public boolean isDefiningMdFieldModified()
  {
    return isModified(DEFININGMDFIELD);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getDefiningMdFieldMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(DEFININGMDFIELD).getAttributeMdDTO();
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<com.runwaysdk.system.CharacterOperationDTO> getOperation()
  {
    return (java.util.List<com.runwaysdk.system.CharacterOperationDTO>) com.runwaysdk.transport.conversion.ConversionFacade.convertEnumDTOsFromEnumNames(getRequest(), com.runwaysdk.system.CharacterOperationDTO.CLASS, getEnumNames(OPERATION));
  }
  
  public java.util.List<String> getOperationEnumNames()
  {
    return getEnumNames(OPERATION);
  }
  
  public void addOperation(com.runwaysdk.system.CharacterOperationDTO enumDTO)
  {
    addEnumItem(OPERATION, enumDTO.toString());
  }
  
  public void removeOperation(com.runwaysdk.system.CharacterOperationDTO enumDTO)
  {
    removeEnumItem(OPERATION, enumDTO.toString());
  }
  
  public void clearOperation()
  {
    clearEnum(OPERATION);
  }
  
  public boolean isOperationWritable()
  {
    return isWritable(OPERATION);
  }
  
  public boolean isOperationReadable()
  {
    return isReadable(OPERATION);
  }
  
  public boolean isOperationModified()
  {
    return isModified(OPERATION);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeEnumerationMdDTO getOperationMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeEnumerationMdDTO) getAttributeDTO(OPERATION).getAttributeMdDTO();
  }
  
  public String getValue()
  {
    return getValue(VALUE);
  }
  
  public void setValue(String value)
  {
    if(value == null)
    {
      setValue(VALUE, "");
    }
    else
    {
      setValue(VALUE, value);
    }
  }
  
  public boolean isValueWritable()
  {
    return isWritable(VALUE);
  }
  
  public boolean isValueReadable()
  {
    return isReadable(VALUE);
  }
  
  public boolean isValueModified()
  {
    return isModified(VALUE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getValueMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(VALUE).getAttributeMdDTO();
  }
  
  public static com.runwaysdk.system.metadata.CharacterConditionDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String id)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(id);
    
    return (com.runwaysdk.system.metadata.CharacterConditionDTO) dto;
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
  
  public static com.runwaysdk.system.metadata.CharacterConditionQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.metadata.CharacterConditionQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.metadata.CharacterConditionDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.metadata.CharacterConditionDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.CharacterConditionDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.metadata.CharacterConditionDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.metadata.CharacterConditionDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.CharacterConditionDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.metadata.CharacterConditionDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
