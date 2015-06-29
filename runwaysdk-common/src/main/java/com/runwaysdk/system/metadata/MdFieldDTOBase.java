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

@com.runwaysdk.business.ClassSignature(hash = 1667732589)
public abstract class MdFieldDTOBase extends com.runwaysdk.system.metadata.MetadataDTO
{
  public final static String CLASS = "com.runwaysdk.system.metadata.MdField";
  private static final long serialVersionUID = 1667732589;
  
  protected MdFieldDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected MdFieldDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String DISPLAYLABEL = "displayLabel";
  public static java.lang.String FIELDCONDITION = "fieldCondition";
  public static java.lang.String FIELDNAME = "fieldName";
  public static java.lang.String FIELDORDER = "fieldOrder";
  public static java.lang.String REQUIRED = "required";
  public com.runwaysdk.system.metadata.MdFieldDisplayLabelDTO getDisplayLabel()
  {
    return (com.runwaysdk.system.metadata.MdFieldDisplayLabelDTO) this.getAttributeStructDTO(DISPLAYLABEL).getStructDTO();
  }
  
  public boolean isDisplayLabelWritable()
  {
    return isWritable(DISPLAYLABEL);
  }
  
  public boolean isDisplayLabelReadable()
  {
    return isReadable(DISPLAYLABEL);
  }
  
  public boolean isDisplayLabelModified()
  {
    return isModified(DISPLAYLABEL);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeLocalCharacterMdDTO getDisplayLabelMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeLocalCharacterMdDTO) getAttributeDTO(DISPLAYLABEL).getAttributeMdDTO();
  }
  
  public com.runwaysdk.system.metadata.FieldConditionDTO getFieldCondition()
  {
    if(getValue(FIELDCONDITION) == null || getValue(FIELDCONDITION).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.metadata.FieldConditionDTO.get(getRequest(), getValue(FIELDCONDITION));
    }
  }
  
  public String getFieldConditionId()
  {
    return getValue(FIELDCONDITION);
  }
  
  public void setFieldCondition(com.runwaysdk.system.metadata.FieldConditionDTO value)
  {
    if(value == null)
    {
      setValue(FIELDCONDITION, "");
    }
    else
    {
      setValue(FIELDCONDITION, value.getId());
    }
  }
  
  public boolean isFieldConditionWritable()
  {
    return isWritable(FIELDCONDITION);
  }
  
  public boolean isFieldConditionReadable()
  {
    return isReadable(FIELDCONDITION);
  }
  
  public boolean isFieldConditionModified()
  {
    return isModified(FIELDCONDITION);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getFieldConditionMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(FIELDCONDITION).getAttributeMdDTO();
  }
  
  public String getFieldName()
  {
    return getValue(FIELDNAME);
  }
  
  public void setFieldName(String value)
  {
    if(value == null)
    {
      setValue(FIELDNAME, "");
    }
    else
    {
      setValue(FIELDNAME, value);
    }
  }
  
  public boolean isFieldNameWritable()
  {
    return isWritable(FIELDNAME);
  }
  
  public boolean isFieldNameReadable()
  {
    return isReadable(FIELDNAME);
  }
  
  public boolean isFieldNameModified()
  {
    return isModified(FIELDNAME);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getFieldNameMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(FIELDNAME).getAttributeMdDTO();
  }
  
  public Integer getFieldOrder()
  {
    return com.runwaysdk.constants.MdAttributeIntegerUtil.getTypeSafeValue(getValue(FIELDORDER));
  }
  
  public void setFieldOrder(Integer value)
  {
    if(value == null)
    {
      setValue(FIELDORDER, "");
    }
    else
    {
      setValue(FIELDORDER, java.lang.Integer.toString(value));
    }
  }
  
  public boolean isFieldOrderWritable()
  {
    return isWritable(FIELDORDER);
  }
  
  public boolean isFieldOrderReadable()
  {
    return isReadable(FIELDORDER);
  }
  
  public boolean isFieldOrderModified()
  {
    return isModified(FIELDORDER);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeNumberMdDTO getFieldOrderMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeNumberMdDTO) getAttributeDTO(FIELDORDER).getAttributeMdDTO();
  }
  
  public Boolean getRequired()
  {
    return com.runwaysdk.constants.MdAttributeBooleanUtil.getTypeSafeValue(getValue(REQUIRED));
  }
  
  public void setRequired(Boolean value)
  {
    if(value == null)
    {
      setValue(REQUIRED, "");
    }
    else
    {
      setValue(REQUIRED, java.lang.Boolean.toString(value));
    }
  }
  
  public boolean isRequiredWritable()
  {
    return isWritable(REQUIRED);
  }
  
  public boolean isRequiredReadable()
  {
    return isReadable(REQUIRED);
  }
  
  public boolean isRequiredModified()
  {
    return isModified(REQUIRED);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeBooleanMdDTO getRequiredMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeBooleanMdDTO) getAttributeDTO(REQUIRED).getAttributeMdDTO();
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.MdFormDTO> getAllMdForm()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdFormDTO>) getRequest().getParents(this.getId(), com.runwaysdk.system.metadata.FormFieldDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.MdFormDTO> getAllMdForm(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdFormDTO>) clientRequestIF.getParents(id, com.runwaysdk.system.metadata.FormFieldDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.FormFieldDTO> getAllMdFormRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.FormFieldDTO>) getRequest().getParentRelationships(this.getId(), com.runwaysdk.system.metadata.FormFieldDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.FormFieldDTO> getAllMdFormRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.FormFieldDTO>) clientRequestIF.getParentRelationships(id, com.runwaysdk.system.metadata.FormFieldDTO.CLASS);
  }
  
  public com.runwaysdk.system.metadata.FormFieldDTO addMdForm(com.runwaysdk.system.metadata.MdFormDTO parent)
  {
    return (com.runwaysdk.system.metadata.FormFieldDTO) getRequest().addParent(parent.getId(), this.getId(), com.runwaysdk.system.metadata.FormFieldDTO.CLASS);
  }
  
  public static com.runwaysdk.system.metadata.FormFieldDTO addMdForm(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id, com.runwaysdk.system.metadata.MdFormDTO parent)
  {
    return (com.runwaysdk.system.metadata.FormFieldDTO) clientRequestIF.addParent(parent.getId(), id, com.runwaysdk.system.metadata.FormFieldDTO.CLASS);
  }
  
  public void removeMdForm(com.runwaysdk.system.metadata.FormFieldDTO relationship)
  {
    getRequest().deleteParent(relationship.getId());
  }
  
  public static void removeMdForm(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.metadata.FormFieldDTO relationship)
  {
    clientRequestIF.deleteParent(relationship.getId());
  }
  
  public void removeAllMdForm()
  {
    getRequest().deleteParents(this.getId(), com.runwaysdk.system.metadata.FormFieldDTO.CLASS);
  }
  
  public static void removeAllMdForm(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    clientRequestIF.deleteParents(id, com.runwaysdk.system.metadata.FormFieldDTO.CLASS);
  }
  
  public static com.runwaysdk.system.metadata.MdFieldDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String id)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(id);
    
    return (com.runwaysdk.system.metadata.MdFieldDTO) dto;
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
  
  public static com.runwaysdk.system.metadata.MdFieldQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.metadata.MdFieldQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.metadata.MdFieldDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdFieldDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdFieldDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdFieldDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdFieldDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdFieldDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdFieldDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
