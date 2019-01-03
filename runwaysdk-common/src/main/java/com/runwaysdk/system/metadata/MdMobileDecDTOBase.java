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

@com.runwaysdk.business.ClassSignature(hash = 1877742560)
public abstract class MdMobileDecDTOBase extends com.runwaysdk.system.metadata.MdMobileNumberDTO
{
  public final static String CLASS = "com.runwaysdk.system.metadata.MdMobileDec";
  private static final long serialVersionUID = 1877742560;
  
  protected MdMobileDecDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected MdMobileDecDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String DECPRECISION = "decPrecision";
  public static java.lang.String DECSCALE = "decScale";
  public Integer getDecPrecision()
  {
    return com.runwaysdk.constants.MdAttributeIntegerUtil.getTypeSafeValue(getValue(DECPRECISION));
  }
  
  public void setDecPrecision(Integer value)
  {
    if(value == null)
    {
      setValue(DECPRECISION, "");
    }
    else
    {
      setValue(DECPRECISION, java.lang.Integer.toString(value));
    }
  }
  
  public boolean isDecPrecisionWritable()
  {
    return isWritable(DECPRECISION);
  }
  
  public boolean isDecPrecisionReadable()
  {
    return isReadable(DECPRECISION);
  }
  
  public boolean isDecPrecisionModified()
  {
    return isModified(DECPRECISION);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeNumberMdDTO getDecPrecisionMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeNumberMdDTO) getAttributeDTO(DECPRECISION).getAttributeMdDTO();
  }
  
  public Integer getDecScale()
  {
    return com.runwaysdk.constants.MdAttributeIntegerUtil.getTypeSafeValue(getValue(DECSCALE));
  }
  
  public void setDecScale(Integer value)
  {
    if(value == null)
    {
      setValue(DECSCALE, "");
    }
    else
    {
      setValue(DECSCALE, java.lang.Integer.toString(value));
    }
  }
  
  public boolean isDecScaleWritable()
  {
    return isWritable(DECSCALE);
  }
  
  public boolean isDecScaleReadable()
  {
    return isReadable(DECSCALE);
  }
  
  public boolean isDecScaleModified()
  {
    return isModified(DECSCALE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeNumberMdDTO getDecScaleMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeNumberMdDTO) getAttributeDTO(DECSCALE).getAttributeMdDTO();
  }
  
  public static com.runwaysdk.system.metadata.MdMobileDecDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String oid)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(oid);
    
    return (com.runwaysdk.system.metadata.MdMobileDecDTO) dto;
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
  
  public static com.runwaysdk.system.metadata.MdMobileDecQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.metadata.MdMobileDecQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.metadata.MdMobileDecDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdMobileDecDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdMobileDecDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdMobileDecDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdMobileDecDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdMobileDecDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdMobileDecDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
