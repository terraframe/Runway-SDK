/*******************************************************************************
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
 ******************************************************************************/
package com.runwaysdk.system.metadata;

@com.runwaysdk.business.ClassSignature(hash = 1000202266)
public abstract class MdDimensionDTOBase extends com.runwaysdk.system.metadata.MetadataDTO
{
  public final static String CLASS = "com.runwaysdk.system.metadata.MdDimension";
  private static final long serialVersionUID = 1000202266;
  
  protected MdDimensionDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected MdDimensionDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String DISPLAYLABEL = "displayLabel";
  public static java.lang.String NAME = "name";
  public com.runwaysdk.system.metadata.MetadataDisplayLabelDTO getDisplayLabel()
  {
    return (com.runwaysdk.system.metadata.MetadataDisplayLabelDTO) this.getAttributeStructDTO(DISPLAYLABEL).getStructDTO();
  }
  
  public boolean isDisplayLabelWritable()
  {
    return isWritable(DISPLAYLABEL);
  }
  
  public boolean isDisplayLabelReadable()
  {
    return isReadable(DISPLAYLABEL);
  }
  
  public boolean isDisplayLabelModified()
  {
    return isModified(DISPLAYLABEL);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeLocalCharacterMdDTO getDisplayLabelMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeLocalCharacterMdDTO) getAttributeDTO(DISPLAYLABEL).getAttributeMdDTO();
  }
  
  public String getName()
  {
    return getValue(NAME);
  }
  
  public void setName(String value)
  {
    if(value == null)
    {
      setValue(NAME, "");
    }
    else
    {
      setValue(NAME, value);
    }
  }
  
  public boolean isNameWritable()
  {
    return isWritable(NAME);
  }
  
  public boolean isNameReadable()
  {
    return isReadable(NAME);
  }
  
  public boolean isNameModified()
  {
    return isModified(NAME);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getNameMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(NAME).getAttributeMdDTO();
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.MdAttributeDimensionDTO> getAllMdAttributeDimension()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdAttributeDimensionDTO>) getRequest().getChildren(this.getId(), com.runwaysdk.system.metadata.DimensionHasAttributeDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.MdAttributeDimensionDTO> getAllMdAttributeDimension(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdAttributeDimensionDTO>) clientRequestIF.getChildren(id, com.runwaysdk.system.metadata.DimensionHasAttributeDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.DimensionHasAttributeDTO> getAllMdAttributeDimensionRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.DimensionHasAttributeDTO>) getRequest().getChildRelationships(this.getId(), com.runwaysdk.system.metadata.DimensionHasAttributeDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.DimensionHasAttributeDTO> getAllMdAttributeDimensionRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.DimensionHasAttributeDTO>) clientRequestIF.getChildRelationships(id, com.runwaysdk.system.metadata.DimensionHasAttributeDTO.CLASS);
  }
  
  public com.runwaysdk.system.metadata.DimensionHasAttributeDTO addMdAttributeDimension(com.runwaysdk.system.metadata.MdAttributeDimensionDTO child)
  {
    return (com.runwaysdk.system.metadata.DimensionHasAttributeDTO) getRequest().addChild(this.getId(), child.getId(), com.runwaysdk.system.metadata.DimensionHasAttributeDTO.CLASS);
  }
  
  public static com.runwaysdk.system.metadata.DimensionHasAttributeDTO addMdAttributeDimension(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id, com.runwaysdk.system.metadata.MdAttributeDimensionDTO child)
  {
    return (com.runwaysdk.system.metadata.DimensionHasAttributeDTO) clientRequestIF.addChild(id, child.getId(), com.runwaysdk.system.metadata.DimensionHasAttributeDTO.CLASS);
  }
  
  public void removeMdAttributeDimension(com.runwaysdk.system.metadata.DimensionHasAttributeDTO relationship)
  {
    getRequest().deleteChild(relationship.getId());
  }
  
  public static void removeMdAttributeDimension(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.metadata.DimensionHasAttributeDTO relationship)
  {
    clientRequestIF.deleteChild(relationship.getId());
  }
  
  public void removeAllMdAttributeDimension()
  {
    getRequest().deleteChildren(this.getId(), com.runwaysdk.system.metadata.DimensionHasAttributeDTO.CLASS);
  }
  
  public static void removeAllMdAttributeDimension(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    clientRequestIF.deleteChildren(id, com.runwaysdk.system.metadata.DimensionHasAttributeDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.MdClassDimensionDTO> getAllMdClassDimensions()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdClassDimensionDTO>) getRequest().getChildren(this.getId(), com.runwaysdk.system.metadata.DimensionHasClassDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.MdClassDimensionDTO> getAllMdClassDimensions(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdClassDimensionDTO>) clientRequestIF.getChildren(id, com.runwaysdk.system.metadata.DimensionHasClassDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.DimensionHasClassDTO> getAllMdClassDimensionsRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.DimensionHasClassDTO>) getRequest().getChildRelationships(this.getId(), com.runwaysdk.system.metadata.DimensionHasClassDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.DimensionHasClassDTO> getAllMdClassDimensionsRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.DimensionHasClassDTO>) clientRequestIF.getChildRelationships(id, com.runwaysdk.system.metadata.DimensionHasClassDTO.CLASS);
  }
  
  public com.runwaysdk.system.metadata.DimensionHasClassDTO addMdClassDimensions(com.runwaysdk.system.metadata.MdClassDimensionDTO child)
  {
    return (com.runwaysdk.system.metadata.DimensionHasClassDTO) getRequest().addChild(this.getId(), child.getId(), com.runwaysdk.system.metadata.DimensionHasClassDTO.CLASS);
  }
  
  public static com.runwaysdk.system.metadata.DimensionHasClassDTO addMdClassDimensions(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id, com.runwaysdk.system.metadata.MdClassDimensionDTO child)
  {
    return (com.runwaysdk.system.metadata.DimensionHasClassDTO) clientRequestIF.addChild(id, child.getId(), com.runwaysdk.system.metadata.DimensionHasClassDTO.CLASS);
  }
  
  public void removeMdClassDimensions(com.runwaysdk.system.metadata.DimensionHasClassDTO relationship)
  {
    getRequest().deleteChild(relationship.getId());
  }
  
  public static void removeMdClassDimensions(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.metadata.DimensionHasClassDTO relationship)
  {
    clientRequestIF.deleteChild(relationship.getId());
  }
  
  public void removeAllMdClassDimensions()
  {
    getRequest().deleteChildren(this.getId(), com.runwaysdk.system.metadata.DimensionHasClassDTO.CLASS);
  }
  
  public static void removeAllMdClassDimensions(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    clientRequestIF.deleteChildren(id, com.runwaysdk.system.metadata.DimensionHasClassDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.MdAttributeConcreteDTO> getAllGetMdAttributeConcrete()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdAttributeConcreteDTO>) getRequest().getChildren(this.getId(), com.runwaysdk.system.metadata.DimensionDefinesLocalStructAttributeDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.MdAttributeConcreteDTO> getAllGetMdAttributeConcrete(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdAttributeConcreteDTO>) clientRequestIF.getChildren(id, com.runwaysdk.system.metadata.DimensionDefinesLocalStructAttributeDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.DimensionDefinesLocalStructAttributeDTO> getAllGetMdAttributeConcreteRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.DimensionDefinesLocalStructAttributeDTO>) getRequest().getChildRelationships(this.getId(), com.runwaysdk.system.metadata.DimensionDefinesLocalStructAttributeDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.DimensionDefinesLocalStructAttributeDTO> getAllGetMdAttributeConcreteRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.DimensionDefinesLocalStructAttributeDTO>) clientRequestIF.getChildRelationships(id, com.runwaysdk.system.metadata.DimensionDefinesLocalStructAttributeDTO.CLASS);
  }
  
  public com.runwaysdk.system.metadata.DimensionDefinesLocalStructAttributeDTO addGetMdAttributeConcrete(com.runwaysdk.system.metadata.MdAttributeConcreteDTO child)
  {
    return (com.runwaysdk.system.metadata.DimensionDefinesLocalStructAttributeDTO) getRequest().addChild(this.getId(), child.getId(), com.runwaysdk.system.metadata.DimensionDefinesLocalStructAttributeDTO.CLASS);
  }
  
  public static com.runwaysdk.system.metadata.DimensionDefinesLocalStructAttributeDTO addGetMdAttributeConcrete(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id, com.runwaysdk.system.metadata.MdAttributeConcreteDTO child)
  {
    return (com.runwaysdk.system.metadata.DimensionDefinesLocalStructAttributeDTO) clientRequestIF.addChild(id, child.getId(), com.runwaysdk.system.metadata.DimensionDefinesLocalStructAttributeDTO.CLASS);
  }
  
  public void removeGetMdAttributeConcrete(com.runwaysdk.system.metadata.DimensionDefinesLocalStructAttributeDTO relationship)
  {
    getRequest().deleteChild(relationship.getId());
  }
  
  public static void removeGetMdAttributeConcrete(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.metadata.DimensionDefinesLocalStructAttributeDTO relationship)
  {
    clientRequestIF.deleteChild(relationship.getId());
  }
  
  public void removeAllGetMdAttributeConcrete()
  {
    getRequest().deleteChildren(this.getId(), com.runwaysdk.system.metadata.DimensionDefinesLocalStructAttributeDTO.CLASS);
  }
  
  public static void removeAllGetMdAttributeConcrete(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    clientRequestIF.deleteChildren(id, com.runwaysdk.system.metadata.DimensionDefinesLocalStructAttributeDTO.CLASS);
  }
  
  public static com.runwaysdk.system.metadata.MdDimensionDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String id)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(id);
    
    return (com.runwaysdk.system.metadata.MdDimensionDTO) dto;
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
  
  public static com.runwaysdk.system.metadata.MdDimensionQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.metadata.MdDimensionQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.metadata.MdDimensionDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdDimensionDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdDimensionDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdDimensionDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdDimensionDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdDimensionDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdDimensionDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}