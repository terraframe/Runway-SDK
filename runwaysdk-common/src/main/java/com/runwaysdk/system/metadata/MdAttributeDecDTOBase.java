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

@com.runwaysdk.business.ClassSignature(hash = 451784945)
public abstract class MdAttributeDecDTOBase extends com.runwaysdk.system.metadata.MdAttributeNumberDTO
{
  public final static String CLASS = "com.runwaysdk.system.metadata.MdAttributeDec";
  private static final long serialVersionUID = 451784945;
  
  protected MdAttributeDecDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected MdAttributeDecDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String DATABASEDECIMAL = "databaseDecimal";
  public static java.lang.String DATABASELENGTH = "databaseLength";
  public Integer getDatabaseDecimal()
  {
    return com.runwaysdk.constants.MdAttributeIntegerUtil.getTypeSafeValue(getValue(DATABASEDECIMAL));
  }
  
  public void setDatabaseDecimal(Integer value)
  {
    if(value == null)
    {
      setValue(DATABASEDECIMAL, "");
    }
    else
    {
      setValue(DATABASEDECIMAL, java.lang.Integer.toString(value));
    }
  }
  
  public boolean isDatabaseDecimalWritable()
  {
    return isWritable(DATABASEDECIMAL);
  }
  
  public boolean isDatabaseDecimalReadable()
  {
    return isReadable(DATABASEDECIMAL);
  }
  
  public boolean isDatabaseDecimalModified()
  {
    return isModified(DATABASEDECIMAL);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeNumberMdDTO getDatabaseDecimalMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeNumberMdDTO) getAttributeDTO(DATABASEDECIMAL).getAttributeMdDTO();
  }
  
  public Integer getDatabaseLength()
  {
    return com.runwaysdk.constants.MdAttributeIntegerUtil.getTypeSafeValue(getValue(DATABASELENGTH));
  }
  
  public void setDatabaseLength(Integer value)
  {
    if(value == null)
    {
      setValue(DATABASELENGTH, "");
    }
    else
    {
      setValue(DATABASELENGTH, java.lang.Integer.toString(value));
    }
  }
  
  public boolean isDatabaseLengthWritable()
  {
    return isWritable(DATABASELENGTH);
  }
  
  public boolean isDatabaseLengthReadable()
  {
    return isReadable(DATABASELENGTH);
  }
  
  public boolean isDatabaseLengthModified()
  {
    return isModified(DATABASELENGTH);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeNumberMdDTO getDatabaseLengthMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeNumberMdDTO) getAttributeDTO(DATABASELENGTH).getAttributeMdDTO();
  }
  
  public static com.runwaysdk.system.metadata.MdAttributeDecDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String oid)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(oid);
    
    return (com.runwaysdk.system.metadata.MdAttributeDecDTO) dto;
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
  
  public static com.runwaysdk.system.metadata.MdAttributeDecQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.metadata.MdAttributeDecQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.metadata.MdAttributeDecDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdAttributeDecDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdAttributeDecDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdAttributeDecDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdAttributeDecDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdAttributeDecDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdAttributeDecDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
