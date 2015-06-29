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

@com.runwaysdk.business.ClassSignature(hash = 1756880950)
public abstract class MdWebTextDTOBase extends com.runwaysdk.system.metadata.MdWebPrimitiveDTO
{
  public final static String CLASS = "com.runwaysdk.system.metadata.MdWebText";
  private static final long serialVersionUID = 1756880950;
  
  protected MdWebTextDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected MdWebTextDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String HEIGHT = "height";
  public static java.lang.String WIDTH = "width";
  public Integer getHeight()
  {
    return com.runwaysdk.constants.MdAttributeIntegerUtil.getTypeSafeValue(getValue(HEIGHT));
  }
  
  public void setHeight(Integer value)
  {
    if(value == null)
    {
      setValue(HEIGHT, "");
    }
    else
    {
      setValue(HEIGHT, java.lang.Integer.toString(value));
    }
  }
  
  public boolean isHeightWritable()
  {
    return isWritable(HEIGHT);
  }
  
  public boolean isHeightReadable()
  {
    return isReadable(HEIGHT);
  }
  
  public boolean isHeightModified()
  {
    return isModified(HEIGHT);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeNumberMdDTO getHeightMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeNumberMdDTO) getAttributeDTO(HEIGHT).getAttributeMdDTO();
  }
  
  public Integer getWidth()
  {
    return com.runwaysdk.constants.MdAttributeIntegerUtil.getTypeSafeValue(getValue(WIDTH));
  }
  
  public void setWidth(Integer value)
  {
    if(value == null)
    {
      setValue(WIDTH, "");
    }
    else
    {
      setValue(WIDTH, java.lang.Integer.toString(value));
    }
  }
  
  public boolean isWidthWritable()
  {
    return isWritable(WIDTH);
  }
  
  public boolean isWidthReadable()
  {
    return isReadable(WIDTH);
  }
  
  public boolean isWidthModified()
  {
    return isModified(WIDTH);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeNumberMdDTO getWidthMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeNumberMdDTO) getAttributeDTO(WIDTH).getAttributeMdDTO();
  }
  
  public static com.runwaysdk.system.metadata.MdWebTextDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String id)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(id);
    
    return (com.runwaysdk.system.metadata.MdWebTextDTO) dto;
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
  
  public static com.runwaysdk.system.metadata.MdWebTextQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.metadata.MdWebTextQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.metadata.MdWebTextDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdWebTextDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdWebTextDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdWebTextDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdWebTextDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdWebTextDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdWebTextDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
