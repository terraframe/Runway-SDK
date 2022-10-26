/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK GIS(tm).
 *
 * Runway SDK GIS(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway SDK GIS(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK GIS(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.system.gis.metadata;

@com.runwaysdk.business.ClassSignature(hash = 210276567)
public abstract class MdAttributeGeometryDTOBase extends com.runwaysdk.system.metadata.MdAttributeConcreteDTO
{
  public final static String CLASS = "com.runwaysdk.system.gis.metadata.MdAttributeGeometry";
  private static final long serialVersionUID = 210276567;
  
  protected MdAttributeGeometryDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected MdAttributeGeometryDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public final static java.lang.String DIMENSION = "dimension";
  public final static java.lang.String SRID = "srid";
  public Integer getDimension()
  {
    return com.runwaysdk.constants.MdAttributeIntegerUtil.getTypeSafeValue(getValue(DIMENSION));
  }
  
  public void setDimension(Integer value)
  {
    if(value == null)
    {
      setValue(DIMENSION, "");
    }
    else
    {
      setValue(DIMENSION, java.lang.Integer.toString(value));
    }
  }
  
  public boolean isDimensionWritable()
  {
    return isWritable(DIMENSION);
  }
  
  public boolean isDimensionReadable()
  {
    return isReadable(DIMENSION);
  }
  
  public boolean isDimensionModified()
  {
    return isModified(DIMENSION);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeNumberMdDTO getDimensionMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeNumberMdDTO) getAttributeDTO(DIMENSION).getAttributeMdDTO();
  }
  
  public Integer getSrid()
  {
    return com.runwaysdk.constants.MdAttributeIntegerUtil.getTypeSafeValue(getValue(SRID));
  }
  
  public void setSrid(Integer value)
  {
    if(value == null)
    {
      setValue(SRID, "");
    }
    else
    {
      setValue(SRID, java.lang.Integer.toString(value));
    }
  }
  
  public boolean isSridWritable()
  {
    return isWritable(SRID);
  }
  
  public boolean isSridReadable()
  {
    return isReadable(SRID);
  }
  
  public boolean isSridModified()
  {
    return isModified(SRID);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeNumberMdDTO getSridMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeNumberMdDTO) getAttributeDTO(SRID).getAttributeMdDTO();
  }
  
  public static com.runwaysdk.system.gis.metadata.MdAttributeGeometryDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String oid)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(oid);
    
    return (com.runwaysdk.system.gis.metadata.MdAttributeGeometryDTO) dto;
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
  
  public static com.runwaysdk.system.gis.metadata.MdAttributeGeometryQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.gis.metadata.MdAttributeGeometryQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.gis.metadata.MdAttributeGeometryDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.gis.metadata.MdAttributeGeometryDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.gis.metadata.MdAttributeGeometryDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.gis.metadata.MdAttributeGeometryDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.gis.metadata.MdAttributeGeometryDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.gis.metadata.MdAttributeGeometryDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.gis.metadata.MdAttributeGeometryDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
