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
package com.runwaysdk.system.gis.metadata.graph;

@com.runwaysdk.business.ClassSignature(hash = 314375954)
public abstract class MdGeoVertexDTOBase extends com.runwaysdk.system.metadata.MdVertexDTO
{
  public final static String CLASS = "com.runwaysdk.system.gis.metadata.graph.MdGeoVertex";
  private static final long serialVersionUID = 314375954;
  
  protected MdGeoVertexDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected MdGeoVertexDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public final static java.lang.String GEOMETRYTYPE = "geometryType";
  public final static java.lang.String ISGEOMETRYEDITABLE = "isGeometryEditable";
  @SuppressWarnings("unchecked")
  public java.util.List<com.runwaysdk.system.gis.geo.GeometryTypeDTO> getGeometryType()
  {
    return (java.util.List<com.runwaysdk.system.gis.geo.GeometryTypeDTO>) com.runwaysdk.transport.conversion.ConversionFacade.convertEnumDTOsFromEnumNames(getRequest(), com.runwaysdk.system.gis.geo.GeometryTypeDTO.CLASS, getEnumNames(GEOMETRYTYPE));
  }
  
  public java.util.List<String> getGeometryTypeEnumNames()
  {
    return getEnumNames(GEOMETRYTYPE);
  }
  
  public void addGeometryType(com.runwaysdk.system.gis.geo.GeometryTypeDTO enumDTO)
  {
    addEnumItem(GEOMETRYTYPE, enumDTO.toString());
  }
  
  public void removeGeometryType(com.runwaysdk.system.gis.geo.GeometryTypeDTO enumDTO)
  {
    removeEnumItem(GEOMETRYTYPE, enumDTO.toString());
  }
  
  public void clearGeometryType()
  {
    clearEnum(GEOMETRYTYPE);
  }
  
  public boolean isGeometryTypeWritable()
  {
    return isWritable(GEOMETRYTYPE);
  }
  
  public boolean isGeometryTypeReadable()
  {
    return isReadable(GEOMETRYTYPE);
  }
  
  public boolean isGeometryTypeModified()
  {
    return isModified(GEOMETRYTYPE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeEnumerationMdDTO getGeometryTypeMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeEnumerationMdDTO) getAttributeDTO(GEOMETRYTYPE).getAttributeMdDTO();
  }
  
  public Boolean getIsGeometryEditable()
  {
    return com.runwaysdk.constants.MdAttributeBooleanUtil.getTypeSafeValue(getValue(ISGEOMETRYEDITABLE));
  }
  
  public void setIsGeometryEditable(Boolean value)
  {
    if(value == null)
    {
      setValue(ISGEOMETRYEDITABLE, "");
    }
    else
    {
      setValue(ISGEOMETRYEDITABLE, java.lang.Boolean.toString(value));
    }
  }
  
  public boolean isIsGeometryEditableWritable()
  {
    return isWritable(ISGEOMETRYEDITABLE);
  }
  
  public boolean isIsGeometryEditableReadable()
  {
    return isReadable(ISGEOMETRYEDITABLE);
  }
  
  public boolean isIsGeometryEditableModified()
  {
    return isModified(ISGEOMETRYEDITABLE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeBooleanMdDTO getIsGeometryEditableMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeBooleanMdDTO) getAttributeDTO(ISGEOMETRYEDITABLE).getAttributeMdDTO();
  }
  
  public static com.runwaysdk.system.gis.metadata.graph.MdGeoVertexDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String oid)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(oid);
    
    return (com.runwaysdk.system.gis.metadata.graph.MdGeoVertexDTO) dto;
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
  
  public static com.runwaysdk.system.gis.metadata.graph.MdGeoVertexQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.gis.metadata.graph.MdGeoVertexQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.gis.metadata.graph.MdGeoVertexDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.gis.metadata.graph.MdGeoVertexDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.gis.metadata.graph.MdGeoVertexDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.gis.metadata.graph.MdGeoVertexDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.gis.metadata.graph.MdGeoVertexDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.gis.metadata.graph.MdGeoVertexDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.gis.metadata.graph.MdGeoVertexDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
