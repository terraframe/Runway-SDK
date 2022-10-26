/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
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

@com.runwaysdk.business.ClassSignature(hash = -1534009833)
public abstract class MdClassDimensionDTOBase extends com.runwaysdk.system.metadata.MetadataDTO
{
  public final static String CLASS = "com.runwaysdk.system.metadata.MdClassDimension";
  private static final long serialVersionUID = -1534009833;
  
  protected MdClassDimensionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected MdClassDimensionDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public final static java.lang.String DEFININGMDCLASS = "definingMdClass";
  public final static java.lang.String DEFININGMDDIMENSION = "definingMdDimension";
  public com.runwaysdk.system.metadata.MdClassDTO getDefiningMdClass()
  {
    if(getValue(DEFININGMDCLASS) == null || getValue(DEFININGMDCLASS).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.metadata.MdClassDTO.get(getRequest(), getValue(DEFININGMDCLASS));
    }
  }
  
  public String getDefiningMdClassId()
  {
    return getValue(DEFININGMDCLASS);
  }
  
  public void setDefiningMdClass(com.runwaysdk.system.metadata.MdClassDTO value)
  {
    if(value == null)
    {
      setValue(DEFININGMDCLASS, "");
    }
    else
    {
      setValue(DEFININGMDCLASS, value.getOid());
    }
  }
  
  public boolean isDefiningMdClassWritable()
  {
    return isWritable(DEFININGMDCLASS);
  }
  
  public boolean isDefiningMdClassReadable()
  {
    return isReadable(DEFININGMDCLASS);
  }
  
  public boolean isDefiningMdClassModified()
  {
    return isModified(DEFININGMDCLASS);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getDefiningMdClassMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(DEFININGMDCLASS).getAttributeMdDTO();
  }
  
  public com.runwaysdk.system.metadata.MdDimensionDTO getDefiningMdDimension()
  {
    if(getValue(DEFININGMDDIMENSION) == null || getValue(DEFININGMDDIMENSION).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.metadata.MdDimensionDTO.get(getRequest(), getValue(DEFININGMDDIMENSION));
    }
  }
  
  public String getDefiningMdDimensionId()
  {
    return getValue(DEFININGMDDIMENSION);
  }
  
  public void setDefiningMdDimension(com.runwaysdk.system.metadata.MdDimensionDTO value)
  {
    if(value == null)
    {
      setValue(DEFININGMDDIMENSION, "");
    }
    else
    {
      setValue(DEFININGMDDIMENSION, value.getOid());
    }
  }
  
  public boolean isDefiningMdDimensionWritable()
  {
    return isWritable(DEFININGMDDIMENSION);
  }
  
  public boolean isDefiningMdDimensionReadable()
  {
    return isReadable(DEFININGMDDIMENSION);
  }
  
  public boolean isDefiningMdDimensionModified()
  {
    return isModified(DEFININGMDDIMENSION);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getDefiningMdDimensionMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(DEFININGMDDIMENSION).getAttributeMdDTO();
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.MdClassDTO> getAllMdClasses()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdClassDTO>) getRequest().getParents(this.getOid(), com.runwaysdk.system.metadata.ClassHasDimensionDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.MdClassDTO> getAllMdClasses(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdClassDTO>) clientRequestIF.getParents(oid, com.runwaysdk.system.metadata.ClassHasDimensionDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.ClassHasDimensionDTO> getAllMdClassesRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.ClassHasDimensionDTO>) getRequest().getParentRelationships(this.getOid(), com.runwaysdk.system.metadata.ClassHasDimensionDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.ClassHasDimensionDTO> getAllMdClassesRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.ClassHasDimensionDTO>) clientRequestIF.getParentRelationships(oid, com.runwaysdk.system.metadata.ClassHasDimensionDTO.CLASS);
  }
  
  public com.runwaysdk.system.metadata.ClassHasDimensionDTO addMdClasses(com.runwaysdk.system.metadata.MdClassDTO parent)
  {
    return (com.runwaysdk.system.metadata.ClassHasDimensionDTO) getRequest().addParent(parent.getOid(), this.getOid(), com.runwaysdk.system.metadata.ClassHasDimensionDTO.CLASS);
  }
  
  public static com.runwaysdk.system.metadata.ClassHasDimensionDTO addMdClasses(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid, com.runwaysdk.system.metadata.MdClassDTO parent)
  {
    return (com.runwaysdk.system.metadata.ClassHasDimensionDTO) clientRequestIF.addParent(parent.getOid(), oid, com.runwaysdk.system.metadata.ClassHasDimensionDTO.CLASS);
  }
  
  public void removeMdClasses(com.runwaysdk.system.metadata.ClassHasDimensionDTO relationship)
  {
    getRequest().deleteParent(relationship.getOid());
  }
  
  public static void removeMdClasses(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.metadata.ClassHasDimensionDTO relationship)
  {
    clientRequestIF.deleteParent(relationship.getOid());
  }
  
  public void removeAllMdClasses()
  {
    getRequest().deleteParents(this.getOid(), com.runwaysdk.system.metadata.ClassHasDimensionDTO.CLASS);
  }
  
  public static void removeAllMdClasses(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    clientRequestIF.deleteParents(oid, com.runwaysdk.system.metadata.ClassHasDimensionDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.MdDimensionDTO> getAllMdDimensions()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdDimensionDTO>) getRequest().getParents(this.getOid(), com.runwaysdk.system.metadata.DimensionHasClassDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.MdDimensionDTO> getAllMdDimensions(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdDimensionDTO>) clientRequestIF.getParents(oid, com.runwaysdk.system.metadata.DimensionHasClassDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.DimensionHasClassDTO> getAllMdDimensionsRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.DimensionHasClassDTO>) getRequest().getParentRelationships(this.getOid(), com.runwaysdk.system.metadata.DimensionHasClassDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.DimensionHasClassDTO> getAllMdDimensionsRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.DimensionHasClassDTO>) clientRequestIF.getParentRelationships(oid, com.runwaysdk.system.metadata.DimensionHasClassDTO.CLASS);
  }
  
  public com.runwaysdk.system.metadata.DimensionHasClassDTO addMdDimensions(com.runwaysdk.system.metadata.MdDimensionDTO parent)
  {
    return (com.runwaysdk.system.metadata.DimensionHasClassDTO) getRequest().addParent(parent.getOid(), this.getOid(), com.runwaysdk.system.metadata.DimensionHasClassDTO.CLASS);
  }
  
  public static com.runwaysdk.system.metadata.DimensionHasClassDTO addMdDimensions(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid, com.runwaysdk.system.metadata.MdDimensionDTO parent)
  {
    return (com.runwaysdk.system.metadata.DimensionHasClassDTO) clientRequestIF.addParent(parent.getOid(), oid, com.runwaysdk.system.metadata.DimensionHasClassDTO.CLASS);
  }
  
  public void removeMdDimensions(com.runwaysdk.system.metadata.DimensionHasClassDTO relationship)
  {
    getRequest().deleteParent(relationship.getOid());
  }
  
  public static void removeMdDimensions(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.metadata.DimensionHasClassDTO relationship)
  {
    clientRequestIF.deleteParent(relationship.getOid());
  }
  
  public void removeAllMdDimensions()
  {
    getRequest().deleteParents(this.getOid(), com.runwaysdk.system.metadata.DimensionHasClassDTO.CLASS);
  }
  
  public static void removeAllMdDimensions(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    clientRequestIF.deleteParents(oid, com.runwaysdk.system.metadata.DimensionHasClassDTO.CLASS);
  }
  
  public static com.runwaysdk.system.metadata.MdClassDimensionDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String oid)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(oid);
    
    return (com.runwaysdk.system.metadata.MdClassDimensionDTO) dto;
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
  
  public static com.runwaysdk.system.metadata.MdClassDimensionQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.metadata.MdClassDimensionQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.metadata.MdClassDimensionDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdClassDimensionDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdClassDimensionDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdClassDimensionDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdClassDimensionDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdClassDimensionDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdClassDimensionDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
