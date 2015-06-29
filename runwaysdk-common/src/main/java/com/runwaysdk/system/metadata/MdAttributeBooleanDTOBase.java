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

@com.runwaysdk.business.ClassSignature(hash = -993299173)
public abstract class MdAttributeBooleanDTOBase extends com.runwaysdk.system.metadata.MdAttributePrimitiveDTO
{
  public final static String CLASS = "com.runwaysdk.system.metadata.MdAttributeBoolean";
  private static final long serialVersionUID = -993299173;
  
  protected MdAttributeBooleanDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected MdAttributeBooleanDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String DEFAULTVALUE = "defaultValue";
  public static java.lang.String NEGATIVEDISPLAYLABEL = "negativeDisplayLabel";
  public static java.lang.String POSITIVEDISPLAYLABEL = "positiveDisplayLabel";
  public Boolean getDefaultValue()
  {
    return com.runwaysdk.constants.MdAttributeBooleanUtil.getTypeSafeValue(getValue(DEFAULTVALUE));
  }
  
  public void setDefaultValue(Boolean value)
  {
    if(value == null)
    {
      setValue(DEFAULTVALUE, "");
    }
    else
    {
      setValue(DEFAULTVALUE, java.lang.Boolean.toString(value));
    }
  }
  
  public boolean isDefaultValueWritable()
  {
    return isWritable(DEFAULTVALUE);
  }
  
  public boolean isDefaultValueReadable()
  {
    return isReadable(DEFAULTVALUE);
  }
  
  public boolean isDefaultValueModified()
  {
    return isModified(DEFAULTVALUE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeBooleanMdDTO getDefaultValueMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeBooleanMdDTO) getAttributeDTO(DEFAULTVALUE).getAttributeMdDTO();
  }
  
  public com.runwaysdk.system.metadata.MetadataDisplayLabelDTO getNegativeDisplayLabel()
  {
    return (com.runwaysdk.system.metadata.MetadataDisplayLabelDTO) this.getAttributeStructDTO(NEGATIVEDISPLAYLABEL).getStructDTO();
  }
  
  public boolean isNegativeDisplayLabelWritable()
  {
    return isWritable(NEGATIVEDISPLAYLABEL);
  }
  
  public boolean isNegativeDisplayLabelReadable()
  {
    return isReadable(NEGATIVEDISPLAYLABEL);
  }
  
  public boolean isNegativeDisplayLabelModified()
  {
    return isModified(NEGATIVEDISPLAYLABEL);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeLocalCharacterMdDTO getNegativeDisplayLabelMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeLocalCharacterMdDTO) getAttributeDTO(NEGATIVEDISPLAYLABEL).getAttributeMdDTO();
  }
  
  public com.runwaysdk.system.metadata.MetadataDisplayLabelDTO getPositiveDisplayLabel()
  {
    return (com.runwaysdk.system.metadata.MetadataDisplayLabelDTO) this.getAttributeStructDTO(POSITIVEDISPLAYLABEL).getStructDTO();
  }
  
  public boolean isPositiveDisplayLabelWritable()
  {
    return isWritable(POSITIVEDISPLAYLABEL);
  }
  
  public boolean isPositiveDisplayLabelReadable()
  {
    return isReadable(POSITIVEDISPLAYLABEL);
  }
  
  public boolean isPositiveDisplayLabelModified()
  {
    return isModified(POSITIVEDISPLAYLABEL);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeLocalCharacterMdDTO getPositiveDisplayLabelMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeLocalCharacterMdDTO) getAttributeDTO(POSITIVEDISPLAYLABEL).getAttributeMdDTO();
  }
  
  public static com.runwaysdk.system.metadata.MdAttributeBooleanDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String id)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(id);
    
    return (com.runwaysdk.system.metadata.MdAttributeBooleanDTO) dto;
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
  
  public static com.runwaysdk.system.metadata.MdAttributeBooleanQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.metadata.MdAttributeBooleanQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.metadata.MdAttributeBooleanDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdAttributeBooleanDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdAttributeBooleanDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdAttributeBooleanDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdAttributeBooleanDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdAttributeBooleanDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdAttributeBooleanDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
