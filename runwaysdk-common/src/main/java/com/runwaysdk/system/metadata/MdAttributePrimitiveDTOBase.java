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

@com.runwaysdk.business.ClassSignature(hash = 452016428)
public abstract class MdAttributePrimitiveDTOBase extends com.runwaysdk.system.metadata.MdAttributeConcreteDTO
{
  public final static String CLASS = "com.runwaysdk.system.metadata.MdAttributePrimitive";
  private static final long serialVersionUID = 452016428;
  
  protected MdAttributePrimitiveDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected MdAttributePrimitiveDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String EXPRESSION = "expression";
  public static java.lang.String ISEXPRESSION = "isExpression";
  public String getExpression()
  {
    return getValue(EXPRESSION);
  }
  
  public void setExpression(String value)
  {
    if(value == null)
    {
      setValue(EXPRESSION, "");
    }
    else
    {
      setValue(EXPRESSION, value);
    }
  }
  
  public boolean isExpressionWritable()
  {
    return isWritable(EXPRESSION);
  }
  
  public boolean isExpressionReadable()
  {
    return isReadable(EXPRESSION);
  }
  
  public boolean isExpressionModified()
  {
    return isModified(EXPRESSION);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeTextMdDTO getExpressionMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeTextMdDTO) getAttributeDTO(EXPRESSION).getAttributeMdDTO();
  }
  
  public Boolean getIsExpression()
  {
    return com.runwaysdk.constants.MdAttributeBooleanUtil.getTypeSafeValue(getValue(ISEXPRESSION));
  }
  
  public void setIsExpression(Boolean value)
  {
    if(value == null)
    {
      setValue(ISEXPRESSION, "");
    }
    else
    {
      setValue(ISEXPRESSION, java.lang.Boolean.toString(value));
    }
  }
  
  public boolean isIsExpressionWritable()
  {
    return isWritable(ISEXPRESSION);
  }
  
  public boolean isIsExpressionReadable()
  {
    return isReadable(ISEXPRESSION);
  }
  
  public boolean isIsExpressionModified()
  {
    return isModified(ISEXPRESSION);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeBooleanMdDTO getIsExpressionMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeBooleanMdDTO) getAttributeDTO(ISEXPRESSION).getAttributeMdDTO();
  }
  
  public static com.runwaysdk.system.metadata.MdAttributePrimitiveDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String id)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(id);
    
    return (com.runwaysdk.system.metadata.MdAttributePrimitiveDTO) dto;
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
  
  public static com.runwaysdk.system.metadata.MdAttributePrimitiveQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.metadata.MdAttributePrimitiveQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.metadata.MdAttributePrimitiveDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdAttributePrimitiveDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdAttributePrimitiveDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdAttributePrimitiveDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdAttributePrimitiveDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdAttributePrimitiveDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdAttributePrimitiveDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
