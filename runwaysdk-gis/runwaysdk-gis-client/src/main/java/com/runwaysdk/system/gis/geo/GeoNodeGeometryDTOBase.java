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
package com.runwaysdk.system.gis.geo;

@com.runwaysdk.business.ClassSignature(hash = 1832280874)
public abstract class GeoNodeGeometryDTOBase extends com.runwaysdk.system.gis.geo.GeoNodeDTO
{
  public final static String CLASS = "com.runwaysdk.system.gis.geo.GeoNodeGeometry";
  private static final long serialVersionUID = 1832280874;
  
  protected GeoNodeGeometryDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected GeoNodeGeometryDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String DISPLAYLABELATTRIBUTE = "displayLabelAttribute";
  public static java.lang.String GEOMETRYATTRIBUTE = "geometryAttribute";
  public static java.lang.String IDENTIFIERATTRIBUTE = "identifierAttribute";
  public static java.lang.String MULTIPOLYGONATTRIBUTE = "multiPolygonAttribute";
  public static java.lang.String POINTATTRIBUTE = "pointAttribute";
  public com.runwaysdk.system.metadata.MdAttributeDTO getDisplayLabelAttribute()
  {
    if(getValue(DISPLAYLABELATTRIBUTE) == null || getValue(DISPLAYLABELATTRIBUTE).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.metadata.MdAttributeDTO.get(getRequest(), getValue(DISPLAYLABELATTRIBUTE));
    }
  }
  
  public String getDisplayLabelAttributeId()
  {
    return getValue(DISPLAYLABELATTRIBUTE);
  }
  
  public void setDisplayLabelAttribute(com.runwaysdk.system.metadata.MdAttributeDTO value)
  {
    if(value == null)
    {
      setValue(DISPLAYLABELATTRIBUTE, "");
    }
    else
    {
      setValue(DISPLAYLABELATTRIBUTE, value.getOid());
    }
  }
  
  public boolean isDisplayLabelAttributeWritable()
  {
    return isWritable(DISPLAYLABELATTRIBUTE);
  }
  
  public boolean isDisplayLabelAttributeReadable()
  {
    return isReadable(DISPLAYLABELATTRIBUTE);
  }
  
  public boolean isDisplayLabelAttributeModified()
  {
    return isModified(DISPLAYLABELATTRIBUTE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getDisplayLabelAttributeMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(DISPLAYLABELATTRIBUTE).getAttributeMdDTO();
  }
  
  public com.runwaysdk.system.metadata.MdAttributeDTO getGeometryAttribute()
  {
    if(getValue(GEOMETRYATTRIBUTE) == null || getValue(GEOMETRYATTRIBUTE).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.metadata.MdAttributeDTO.get(getRequest(), getValue(GEOMETRYATTRIBUTE));
    }
  }
  
  public String getGeometryAttributeId()
  {
    return getValue(GEOMETRYATTRIBUTE);
  }
  
  public void setGeometryAttribute(com.runwaysdk.system.metadata.MdAttributeDTO value)
  {
    if(value == null)
    {
      setValue(GEOMETRYATTRIBUTE, "");
    }
    else
    {
      setValue(GEOMETRYATTRIBUTE, value.getOid());
    }
  }
  
  public boolean isGeometryAttributeWritable()
  {
    return isWritable(GEOMETRYATTRIBUTE);
  }
  
  public boolean isGeometryAttributeReadable()
  {
    return isReadable(GEOMETRYATTRIBUTE);
  }
  
  public boolean isGeometryAttributeModified()
  {
    return isModified(GEOMETRYATTRIBUTE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getGeometryAttributeMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(GEOMETRYATTRIBUTE).getAttributeMdDTO();
  }
  
  public com.runwaysdk.system.metadata.MdAttributeDTO getIdentifierAttribute()
  {
    if(getValue(IDENTIFIERATTRIBUTE) == null || getValue(IDENTIFIERATTRIBUTE).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.metadata.MdAttributeDTO.get(getRequest(), getValue(IDENTIFIERATTRIBUTE));
    }
  }
  
  public String getIdentifierAttributeId()
  {
    return getValue(IDENTIFIERATTRIBUTE);
  }
  
  public void setIdentifierAttribute(com.runwaysdk.system.metadata.MdAttributeDTO value)
  {
    if(value == null)
    {
      setValue(IDENTIFIERATTRIBUTE, "");
    }
    else
    {
      setValue(IDENTIFIERATTRIBUTE, value.getOid());
    }
  }
  
  public boolean isIdentifierAttributeWritable()
  {
    return isWritable(IDENTIFIERATTRIBUTE);
  }
  
  public boolean isIdentifierAttributeReadable()
  {
    return isReadable(IDENTIFIERATTRIBUTE);
  }
  
  public boolean isIdentifierAttributeModified()
  {
    return isModified(IDENTIFIERATTRIBUTE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getIdentifierAttributeMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(IDENTIFIERATTRIBUTE).getAttributeMdDTO();
  }
  
  public com.runwaysdk.system.gis.metadata.MdAttributeMultiPolygonDTO getMultiPolygonAttribute()
  {
    if(getValue(MULTIPOLYGONATTRIBUTE) == null || getValue(MULTIPOLYGONATTRIBUTE).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.gis.metadata.MdAttributeMultiPolygonDTO.get(getRequest(), getValue(MULTIPOLYGONATTRIBUTE));
    }
  }
  
  public String getMultiPolygonAttributeId()
  {
    return getValue(MULTIPOLYGONATTRIBUTE);
  }
  
  public void setMultiPolygonAttribute(com.runwaysdk.system.gis.metadata.MdAttributeMultiPolygonDTO value)
  {
    if(value == null)
    {
      setValue(MULTIPOLYGONATTRIBUTE, "");
    }
    else
    {
      setValue(MULTIPOLYGONATTRIBUTE, value.getOid());
    }
  }
  
  public boolean isMultiPolygonAttributeWritable()
  {
    return isWritable(MULTIPOLYGONATTRIBUTE);
  }
  
  public boolean isMultiPolygonAttributeReadable()
  {
    return isReadable(MULTIPOLYGONATTRIBUTE);
  }
  
  public boolean isMultiPolygonAttributeModified()
  {
    return isModified(MULTIPOLYGONATTRIBUTE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getMultiPolygonAttributeMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(MULTIPOLYGONATTRIBUTE).getAttributeMdDTO();
  }
  
  public com.runwaysdk.system.gis.metadata.MdAttributePointDTO getPointAttribute()
  {
    if(getValue(POINTATTRIBUTE) == null || getValue(POINTATTRIBUTE).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.gis.metadata.MdAttributePointDTO.get(getRequest(), getValue(POINTATTRIBUTE));
    }
  }
  
  public String getPointAttributeId()
  {
    return getValue(POINTATTRIBUTE);
  }
  
  public void setPointAttribute(com.runwaysdk.system.gis.metadata.MdAttributePointDTO value)
  {
    if(value == null)
    {
      setValue(POINTATTRIBUTE, "");
    }
    else
    {
      setValue(POINTATTRIBUTE, value.getOid());
    }
  }
  
  public boolean isPointAttributeWritable()
  {
    return isWritable(POINTATTRIBUTE);
  }
  
  public boolean isPointAttributeReadable()
  {
    return isReadable(POINTATTRIBUTE);
  }
  
  public boolean isPointAttributeModified()
  {
    return isModified(POINTATTRIBUTE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getPointAttributeMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(POINTATTRIBUTE).getAttributeMdDTO();
  }
  
  public static com.runwaysdk.system.gis.geo.GeoNodeGeometryDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String oid)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(oid);
    
    return (com.runwaysdk.system.gis.geo.GeoNodeGeometryDTO) dto;
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
  
  public static com.runwaysdk.system.gis.geo.GeoNodeGeometryQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.gis.geo.GeoNodeGeometryQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.gis.geo.GeoNodeGeometryDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.gis.geo.GeoNodeGeometryDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.gis.geo.GeoNodeGeometryDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.gis.geo.GeoNodeGeometryDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.gis.geo.GeoNodeGeometryDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.gis.geo.GeoNodeGeometryDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.gis.geo.GeoNodeGeometryDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
