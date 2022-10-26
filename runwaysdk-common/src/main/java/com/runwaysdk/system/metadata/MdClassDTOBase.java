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

@com.runwaysdk.business.ClassSignature(hash = 313947917)
public abstract class MdClassDTOBase extends com.runwaysdk.system.metadata.MdTypeDTO
{
  public final static String CLASS = "com.runwaysdk.system.metadata.MdClass";
  private static final long serialVersionUID = 313947917;
  
  protected MdClassDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected MdClassDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public final static java.lang.String PUBLISH = "publish";
  public final static java.lang.String STUBCLASS = "stubClass";
  public final static java.lang.String STUBDTOCLASS = "stubDTOclass";
  public final static java.lang.String STUBDTOSOURCE = "stubDTOsource";
  public final static java.lang.String STUBSOURCE = "stubSource";
  public Boolean getPublish()
  {
    return com.runwaysdk.constants.MdAttributeBooleanUtil.getTypeSafeValue(getValue(PUBLISH));
  }
  
  public void setPublish(Boolean value)
  {
    if(value == null)
    {
      setValue(PUBLISH, "");
    }
    else
    {
      setValue(PUBLISH, java.lang.Boolean.toString(value));
    }
  }
  
  public boolean isPublishWritable()
  {
    return isWritable(PUBLISH);
  }
  
  public boolean isPublishReadable()
  {
    return isReadable(PUBLISH);
  }
  
  public boolean isPublishModified()
  {
    return isModified(PUBLISH);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeBooleanMdDTO getPublishMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeBooleanMdDTO) getAttributeDTO(PUBLISH).getAttributeMdDTO();
  }
  
  public byte[] getStubClass()
  {
    return super.getBlob(STUBCLASS);
  }
  
  public boolean isStubClassWritable()
  {
    return isWritable(STUBCLASS);
  }
  
  public boolean isStubClassReadable()
  {
    return isReadable(STUBCLASS);
  }
  
  public boolean isStubClassModified()
  {
    return isModified(STUBCLASS);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeBlobMdDTO getStubClassMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeBlobMdDTO) getAttributeDTO(STUBCLASS).getAttributeMdDTO();
  }
  
  public byte[] getStubDTOclass()
  {
    return super.getBlob(STUBDTOCLASS);
  }
  
  public boolean isStubDTOclassWritable()
  {
    return isWritable(STUBDTOCLASS);
  }
  
  public boolean isStubDTOclassReadable()
  {
    return isReadable(STUBDTOCLASS);
  }
  
  public boolean isStubDTOclassModified()
  {
    return isModified(STUBDTOCLASS);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeBlobMdDTO getStubDTOclassMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeBlobMdDTO) getAttributeDTO(STUBDTOCLASS).getAttributeMdDTO();
  }
  
  public String getStubDTOsource()
  {
    return getValue(STUBDTOSOURCE);
  }
  
  public void setStubDTOsource(String value)
  {
    if(value == null)
    {
      setValue(STUBDTOSOURCE, "");
    }
    else
    {
      setValue(STUBDTOSOURCE, value);
    }
  }
  
  public boolean isStubDTOsourceWritable()
  {
    return isWritable(STUBDTOSOURCE);
  }
  
  public boolean isStubDTOsourceReadable()
  {
    return isReadable(STUBDTOSOURCE);
  }
  
  public boolean isStubDTOsourceModified()
  {
    return isModified(STUBDTOSOURCE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeClobMdDTO getStubDTOsourceMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeClobMdDTO) getAttributeDTO(STUBDTOSOURCE).getAttributeMdDTO();
  }
  
  public String getStubSource()
  {
    return getValue(STUBSOURCE);
  }
  
  public void setStubSource(String value)
  {
    if(value == null)
    {
      setValue(STUBSOURCE, "");
    }
    else
    {
      setValue(STUBSOURCE, value);
    }
  }
  
  public boolean isStubSourceWritable()
  {
    return isWritable(STUBSOURCE);
  }
  
  public boolean isStubSourceReadable()
  {
    return isReadable(STUBSOURCE);
  }
  
  public boolean isStubSourceModified()
  {
    return isModified(STUBSOURCE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeClobMdDTO getStubSourceMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeClobMdDTO) getAttributeDTO(STUBSOURCE).getAttributeMdDTO();
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.MdAttributeDTO> getAllAttribute()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdAttributeDTO>) getRequest().getChildren(this.getOid(), com.runwaysdk.system.metadata.ClassAttributeDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.MdAttributeDTO> getAllAttribute(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdAttributeDTO>) clientRequestIF.getChildren(oid, com.runwaysdk.system.metadata.ClassAttributeDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.ClassAttributeDTO> getAllAttributeRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.ClassAttributeDTO>) getRequest().getChildRelationships(this.getOid(), com.runwaysdk.system.metadata.ClassAttributeDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.ClassAttributeDTO> getAllAttributeRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.ClassAttributeDTO>) clientRequestIF.getChildRelationships(oid, com.runwaysdk.system.metadata.ClassAttributeDTO.CLASS);
  }
  
  public com.runwaysdk.system.metadata.ClassAttributeDTO addAttribute(com.runwaysdk.system.metadata.MdAttributeDTO child)
  {
    return (com.runwaysdk.system.metadata.ClassAttributeDTO) getRequest().addChild(this.getOid(), child.getOid(), com.runwaysdk.system.metadata.ClassAttributeDTO.CLASS);
  }
  
  public static com.runwaysdk.system.metadata.ClassAttributeDTO addAttribute(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid, com.runwaysdk.system.metadata.MdAttributeDTO child)
  {
    return (com.runwaysdk.system.metadata.ClassAttributeDTO) clientRequestIF.addChild(oid, child.getOid(), com.runwaysdk.system.metadata.ClassAttributeDTO.CLASS);
  }
  
  public void removeAttribute(com.runwaysdk.system.metadata.ClassAttributeDTO relationship)
  {
    getRequest().deleteChild(relationship.getOid());
  }
  
  public static void removeAttribute(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.metadata.ClassAttributeDTO relationship)
  {
    clientRequestIF.deleteChild(relationship.getOid());
  }
  
  public void removeAllAttribute()
  {
    getRequest().deleteChildren(this.getOid(), com.runwaysdk.system.metadata.ClassAttributeDTO.CLASS);
  }
  
  public static void removeAllAttribute(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    clientRequestIF.deleteChildren(oid, com.runwaysdk.system.metadata.ClassAttributeDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.MdAttributeConcreteDTO> getAllConcreteAttribute()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdAttributeConcreteDTO>) getRequest().getChildren(this.getOid(), com.runwaysdk.system.metadata.ClassAttributeConcreteDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.MdAttributeConcreteDTO> getAllConcreteAttribute(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdAttributeConcreteDTO>) clientRequestIF.getChildren(oid, com.runwaysdk.system.metadata.ClassAttributeConcreteDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.ClassAttributeConcreteDTO> getAllConcreteAttributeRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.ClassAttributeConcreteDTO>) getRequest().getChildRelationships(this.getOid(), com.runwaysdk.system.metadata.ClassAttributeConcreteDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.ClassAttributeConcreteDTO> getAllConcreteAttributeRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.ClassAttributeConcreteDTO>) clientRequestIF.getChildRelationships(oid, com.runwaysdk.system.metadata.ClassAttributeConcreteDTO.CLASS);
  }
  
  public com.runwaysdk.system.metadata.ClassAttributeConcreteDTO addConcreteAttribute(com.runwaysdk.system.metadata.MdAttributeConcreteDTO child)
  {
    return (com.runwaysdk.system.metadata.ClassAttributeConcreteDTO) getRequest().addChild(this.getOid(), child.getOid(), com.runwaysdk.system.metadata.ClassAttributeConcreteDTO.CLASS);
  }
  
  public static com.runwaysdk.system.metadata.ClassAttributeConcreteDTO addConcreteAttribute(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid, com.runwaysdk.system.metadata.MdAttributeConcreteDTO child)
  {
    return (com.runwaysdk.system.metadata.ClassAttributeConcreteDTO) clientRequestIF.addChild(oid, child.getOid(), com.runwaysdk.system.metadata.ClassAttributeConcreteDTO.CLASS);
  }
  
  public void removeConcreteAttribute(com.runwaysdk.system.metadata.ClassAttributeConcreteDTO relationship)
  {
    getRequest().deleteChild(relationship.getOid());
  }
  
  public static void removeConcreteAttribute(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.metadata.ClassAttributeConcreteDTO relationship)
  {
    clientRequestIF.deleteChild(relationship.getOid());
  }
  
  public void removeAllConcreteAttribute()
  {
    getRequest().deleteChildren(this.getOid(), com.runwaysdk.system.metadata.ClassAttributeConcreteDTO.CLASS);
  }
  
  public static void removeAllConcreteAttribute(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    clientRequestIF.deleteChildren(oid, com.runwaysdk.system.metadata.ClassAttributeConcreteDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.MdClassDimensionDTO> getAllMdClassDimensions()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdClassDimensionDTO>) getRequest().getChildren(this.getOid(), com.runwaysdk.system.metadata.ClassHasDimensionDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.MdClassDimensionDTO> getAllMdClassDimensions(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdClassDimensionDTO>) clientRequestIF.getChildren(oid, com.runwaysdk.system.metadata.ClassHasDimensionDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.ClassHasDimensionDTO> getAllMdClassDimensionsRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.ClassHasDimensionDTO>) getRequest().getChildRelationships(this.getOid(), com.runwaysdk.system.metadata.ClassHasDimensionDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.ClassHasDimensionDTO> getAllMdClassDimensionsRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.ClassHasDimensionDTO>) clientRequestIF.getChildRelationships(oid, com.runwaysdk.system.metadata.ClassHasDimensionDTO.CLASS);
  }
  
  public com.runwaysdk.system.metadata.ClassHasDimensionDTO addMdClassDimensions(com.runwaysdk.system.metadata.MdClassDimensionDTO child)
  {
    return (com.runwaysdk.system.metadata.ClassHasDimensionDTO) getRequest().addChild(this.getOid(), child.getOid(), com.runwaysdk.system.metadata.ClassHasDimensionDTO.CLASS);
  }
  
  public static com.runwaysdk.system.metadata.ClassHasDimensionDTO addMdClassDimensions(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid, com.runwaysdk.system.metadata.MdClassDimensionDTO child)
  {
    return (com.runwaysdk.system.metadata.ClassHasDimensionDTO) clientRequestIF.addChild(oid, child.getOid(), com.runwaysdk.system.metadata.ClassHasDimensionDTO.CLASS);
  }
  
  public void removeMdClassDimensions(com.runwaysdk.system.metadata.ClassHasDimensionDTO relationship)
  {
    getRequest().deleteChild(relationship.getOid());
  }
  
  public static void removeMdClassDimensions(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.metadata.ClassHasDimensionDTO relationship)
  {
    clientRequestIF.deleteChild(relationship.getOid());
  }
  
  public void removeAllMdClassDimensions()
  {
    getRequest().deleteChildren(this.getOid(), com.runwaysdk.system.metadata.ClassHasDimensionDTO.CLASS);
  }
  
  public static void removeAllMdClassDimensions(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    clientRequestIF.deleteChildren(oid, com.runwaysdk.system.metadata.ClassHasDimensionDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.MdClassDTO> getAllSubEntity()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdClassDTO>) getRequest().getChildren(this.getOid(), com.runwaysdk.system.metadata.ClassInheritanceDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.MdClassDTO> getAllSubEntity(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdClassDTO>) clientRequestIF.getChildren(oid, com.runwaysdk.system.metadata.ClassInheritanceDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.ClassInheritanceDTO> getAllSubEntityRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.ClassInheritanceDTO>) getRequest().getChildRelationships(this.getOid(), com.runwaysdk.system.metadata.ClassInheritanceDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.ClassInheritanceDTO> getAllSubEntityRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.ClassInheritanceDTO>) clientRequestIF.getChildRelationships(oid, com.runwaysdk.system.metadata.ClassInheritanceDTO.CLASS);
  }
  
  public com.runwaysdk.system.metadata.ClassInheritanceDTO addSubEntity(com.runwaysdk.system.metadata.MdClassDTO child)
  {
    return (com.runwaysdk.system.metadata.ClassInheritanceDTO) getRequest().addChild(this.getOid(), child.getOid(), com.runwaysdk.system.metadata.ClassInheritanceDTO.CLASS);
  }
  
  public static com.runwaysdk.system.metadata.ClassInheritanceDTO addSubEntity(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid, com.runwaysdk.system.metadata.MdClassDTO child)
  {
    return (com.runwaysdk.system.metadata.ClassInheritanceDTO) clientRequestIF.addChild(oid, child.getOid(), com.runwaysdk.system.metadata.ClassInheritanceDTO.CLASS);
  }
  
  public void removeSubEntity(com.runwaysdk.system.metadata.ClassInheritanceDTO relationship)
  {
    getRequest().deleteChild(relationship.getOid());
  }
  
  public static void removeSubEntity(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.metadata.ClassInheritanceDTO relationship)
  {
    clientRequestIF.deleteChild(relationship.getOid());
  }
  
  public void removeAllSubEntity()
  {
    getRequest().deleteChildren(this.getOid(), com.runwaysdk.system.metadata.ClassInheritanceDTO.CLASS);
  }
  
  public static void removeAllSubEntity(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    clientRequestIF.deleteChildren(oid, com.runwaysdk.system.metadata.ClassInheritanceDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.MdClassDTO> getAllInheritsFromEntity()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdClassDTO>) getRequest().getParents(this.getOid(), com.runwaysdk.system.metadata.ClassInheritanceDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.MdClassDTO> getAllInheritsFromEntity(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdClassDTO>) clientRequestIF.getParents(oid, com.runwaysdk.system.metadata.ClassInheritanceDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.ClassInheritanceDTO> getAllInheritsFromEntityRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.ClassInheritanceDTO>) getRequest().getParentRelationships(this.getOid(), com.runwaysdk.system.metadata.ClassInheritanceDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.ClassInheritanceDTO> getAllInheritsFromEntityRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.ClassInheritanceDTO>) clientRequestIF.getParentRelationships(oid, com.runwaysdk.system.metadata.ClassInheritanceDTO.CLASS);
  }
  
  public com.runwaysdk.system.metadata.ClassInheritanceDTO addInheritsFromEntity(com.runwaysdk.system.metadata.MdClassDTO parent)
  {
    return (com.runwaysdk.system.metadata.ClassInheritanceDTO) getRequest().addParent(parent.getOid(), this.getOid(), com.runwaysdk.system.metadata.ClassInheritanceDTO.CLASS);
  }
  
  public static com.runwaysdk.system.metadata.ClassInheritanceDTO addInheritsFromEntity(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid, com.runwaysdk.system.metadata.MdClassDTO parent)
  {
    return (com.runwaysdk.system.metadata.ClassInheritanceDTO) clientRequestIF.addParent(parent.getOid(), oid, com.runwaysdk.system.metadata.ClassInheritanceDTO.CLASS);
  }
  
  public void removeInheritsFromEntity(com.runwaysdk.system.metadata.ClassInheritanceDTO relationship)
  {
    getRequest().deleteParent(relationship.getOid());
  }
  
  public static void removeInheritsFromEntity(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.metadata.ClassInheritanceDTO relationship)
  {
    clientRequestIF.deleteParent(relationship.getOid());
  }
  
  public void removeAllInheritsFromEntity()
  {
    getRequest().deleteParents(this.getOid(), com.runwaysdk.system.metadata.ClassInheritanceDTO.CLASS);
  }
  
  public static void removeAllInheritsFromEntity(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    clientRequestIF.deleteParents(oid, com.runwaysdk.system.metadata.ClassInheritanceDTO.CLASS);
  }
  
  public static com.runwaysdk.system.metadata.MdClassDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String oid)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(oid);
    
    return (com.runwaysdk.system.metadata.MdClassDTO) dto;
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
  
  public static com.runwaysdk.system.metadata.MdClassQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.metadata.MdClassQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.metadata.MdClassDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdClassDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdClassDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdClassDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdClassDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdClassDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdClassDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
