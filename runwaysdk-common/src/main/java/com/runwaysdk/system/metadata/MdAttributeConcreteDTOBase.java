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

@com.runwaysdk.business.ClassSignature(hash = 713552044)
public abstract class MdAttributeConcreteDTOBase extends com.runwaysdk.system.metadata.MdAttributeDTO
{
  public final static String CLASS = "com.runwaysdk.system.metadata.MdAttributeConcrete";
  private static final long serialVersionUID = 713552044;
  
  protected MdAttributeConcreteDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected MdAttributeConcreteDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String ATTRIBUTENAME = "attributeName";
  public static java.lang.String COLUMNNAME = "columnName";
  public static java.lang.String DEFININGMDCLASS = "definingMdClass";
  public static java.lang.String DISPLAYLABEL = "displayLabel";
  public static java.lang.String GENERATEACCESSOR = "generateAccessor";
  public static java.lang.String GETTERVISIBILITY = "getterVisibility";
  public static java.lang.String IMMUTABLE = "immutable";
  public static java.lang.String INDEXNAME = "indexName";
  public static java.lang.String INDEXTYPE = "indexType";
  public static java.lang.String REQUIRED = "required";
  public static java.lang.String SETTERVISIBILITY = "setterVisibility";
  public static java.lang.String SYSTEM = "system";
  public String getAttributeName()
  {
    return getValue(ATTRIBUTENAME);
  }
  
  public void setAttributeName(String value)
  {
    if(value == null)
    {
      setValue(ATTRIBUTENAME, "");
    }
    else
    {
      setValue(ATTRIBUTENAME, value);
    }
  }
  
  public boolean isAttributeNameWritable()
  {
    return isWritable(ATTRIBUTENAME);
  }
  
  public boolean isAttributeNameReadable()
  {
    return isReadable(ATTRIBUTENAME);
  }
  
  public boolean isAttributeNameModified()
  {
    return isModified(ATTRIBUTENAME);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getAttributeNameMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(ATTRIBUTENAME).getAttributeMdDTO();
  }
  
  public String getColumnName()
  {
    return getValue(COLUMNNAME);
  }
  
  public void setColumnName(String value)
  {
    if(value == null)
    {
      setValue(COLUMNNAME, "");
    }
    else
    {
      setValue(COLUMNNAME, value);
    }
  }
  
  public boolean isColumnNameWritable()
  {
    return isWritable(COLUMNNAME);
  }
  
  public boolean isColumnNameReadable()
  {
    return isReadable(COLUMNNAME);
  }
  
  public boolean isColumnNameModified()
  {
    return isModified(COLUMNNAME);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getColumnNameMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(COLUMNNAME).getAttributeMdDTO();
  }
  
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
      setValue(DEFININGMDCLASS, value.getId());
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
  
  public Boolean getGenerateAccessor()
  {
    return com.runwaysdk.constants.MdAttributeBooleanUtil.getTypeSafeValue(getValue(GENERATEACCESSOR));
  }
  
  public void setGenerateAccessor(Boolean value)
  {
    if(value == null)
    {
      setValue(GENERATEACCESSOR, "");
    }
    else
    {
      setValue(GENERATEACCESSOR, java.lang.Boolean.toString(value));
    }
  }
  
  public boolean isGenerateAccessorWritable()
  {
    return isWritable(GENERATEACCESSOR);
  }
  
  public boolean isGenerateAccessorReadable()
  {
    return isReadable(GENERATEACCESSOR);
  }
  
  public boolean isGenerateAccessorModified()
  {
    return isModified(GENERATEACCESSOR);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeBooleanMdDTO getGenerateAccessorMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeBooleanMdDTO) getAttributeDTO(GENERATEACCESSOR).getAttributeMdDTO();
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<com.runwaysdk.system.metadata.VisibilityModifierDTO> getGetterVisibility()
  {
    return (java.util.List<com.runwaysdk.system.metadata.VisibilityModifierDTO>) com.runwaysdk.transport.conversion.ConversionFacade.convertEnumDTOsFromEnumNames(getRequest(), com.runwaysdk.system.metadata.VisibilityModifierDTO.CLASS, getEnumNames(GETTERVISIBILITY));
  }
  
  public java.util.List<String> getGetterVisibilityEnumNames()
  {
    return getEnumNames(GETTERVISIBILITY);
  }
  
  public void addGetterVisibility(com.runwaysdk.system.metadata.VisibilityModifierDTO enumDTO)
  {
    addEnumItem(GETTERVISIBILITY, enumDTO.toString());
  }
  
  public void removeGetterVisibility(com.runwaysdk.system.metadata.VisibilityModifierDTO enumDTO)
  {
    removeEnumItem(GETTERVISIBILITY, enumDTO.toString());
  }
  
  public void clearGetterVisibility()
  {
    clearEnum(GETTERVISIBILITY);
  }
  
  public boolean isGetterVisibilityWritable()
  {
    return isWritable(GETTERVISIBILITY);
  }
  
  public boolean isGetterVisibilityReadable()
  {
    return isReadable(GETTERVISIBILITY);
  }
  
  public boolean isGetterVisibilityModified()
  {
    return isModified(GETTERVISIBILITY);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeEnumerationMdDTO getGetterVisibilityMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeEnumerationMdDTO) getAttributeDTO(GETTERVISIBILITY).getAttributeMdDTO();
  }
  
  public Boolean getImmutable()
  {
    return com.runwaysdk.constants.MdAttributeBooleanUtil.getTypeSafeValue(getValue(IMMUTABLE));
  }
  
  public void setImmutable(Boolean value)
  {
    if(value == null)
    {
      setValue(IMMUTABLE, "");
    }
    else
    {
      setValue(IMMUTABLE, java.lang.Boolean.toString(value));
    }
  }
  
  public boolean isImmutableWritable()
  {
    return isWritable(IMMUTABLE);
  }
  
  public boolean isImmutableReadable()
  {
    return isReadable(IMMUTABLE);
  }
  
  public boolean isImmutableModified()
  {
    return isModified(IMMUTABLE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeBooleanMdDTO getImmutableMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeBooleanMdDTO) getAttributeDTO(IMMUTABLE).getAttributeMdDTO();
  }
  
  public String getIndexName()
  {
    return getValue(INDEXNAME);
  }
  
  public boolean isIndexNameWritable()
  {
    return isWritable(INDEXNAME);
  }
  
  public boolean isIndexNameReadable()
  {
    return isReadable(INDEXNAME);
  }
  
  public boolean isIndexNameModified()
  {
    return isModified(INDEXNAME);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getIndexNameMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(INDEXNAME).getAttributeMdDTO();
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<com.runwaysdk.system.metadata.MdAttributeIndicesDTO> getIndexType()
  {
    return (java.util.List<com.runwaysdk.system.metadata.MdAttributeIndicesDTO>) com.runwaysdk.transport.conversion.ConversionFacade.convertEnumDTOsFromEnumNames(getRequest(), com.runwaysdk.system.metadata.MdAttributeIndicesDTO.CLASS, getEnumNames(INDEXTYPE));
  }
  
  public java.util.List<String> getIndexTypeEnumNames()
  {
    return getEnumNames(INDEXTYPE);
  }
  
  public void addIndexType(com.runwaysdk.system.metadata.MdAttributeIndicesDTO enumDTO)
  {
    addEnumItem(INDEXTYPE, enumDTO.toString());
  }
  
  public void removeIndexType(com.runwaysdk.system.metadata.MdAttributeIndicesDTO enumDTO)
  {
    removeEnumItem(INDEXTYPE, enumDTO.toString());
  }
  
  public void clearIndexType()
  {
    clearEnum(INDEXTYPE);
  }
  
  public boolean isIndexTypeWritable()
  {
    return isWritable(INDEXTYPE);
  }
  
  public boolean isIndexTypeReadable()
  {
    return isReadable(INDEXTYPE);
  }
  
  public boolean isIndexTypeModified()
  {
    return isModified(INDEXTYPE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeEnumerationMdDTO getIndexTypeMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeEnumerationMdDTO) getAttributeDTO(INDEXTYPE).getAttributeMdDTO();
  }
  
  public Boolean getRequired()
  {
    return com.runwaysdk.constants.MdAttributeBooleanUtil.getTypeSafeValue(getValue(REQUIRED));
  }
  
  public void setRequired(Boolean value)
  {
    if(value == null)
    {
      setValue(REQUIRED, "");
    }
    else
    {
      setValue(REQUIRED, java.lang.Boolean.toString(value));
    }
  }
  
  public boolean isRequiredWritable()
  {
    return isWritable(REQUIRED);
  }
  
  public boolean isRequiredReadable()
  {
    return isReadable(REQUIRED);
  }
  
  public boolean isRequiredModified()
  {
    return isModified(REQUIRED);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeBooleanMdDTO getRequiredMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeBooleanMdDTO) getAttributeDTO(REQUIRED).getAttributeMdDTO();
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<com.runwaysdk.system.metadata.VisibilityModifierDTO> getSetterVisibility()
  {
    return (java.util.List<com.runwaysdk.system.metadata.VisibilityModifierDTO>) com.runwaysdk.transport.conversion.ConversionFacade.convertEnumDTOsFromEnumNames(getRequest(), com.runwaysdk.system.metadata.VisibilityModifierDTO.CLASS, getEnumNames(SETTERVISIBILITY));
  }
  
  public java.util.List<String> getSetterVisibilityEnumNames()
  {
    return getEnumNames(SETTERVISIBILITY);
  }
  
  public void addSetterVisibility(com.runwaysdk.system.metadata.VisibilityModifierDTO enumDTO)
  {
    addEnumItem(SETTERVISIBILITY, enumDTO.toString());
  }
  
  public void removeSetterVisibility(com.runwaysdk.system.metadata.VisibilityModifierDTO enumDTO)
  {
    removeEnumItem(SETTERVISIBILITY, enumDTO.toString());
  }
  
  public void clearSetterVisibility()
  {
    clearEnum(SETTERVISIBILITY);
  }
  
  public boolean isSetterVisibilityWritable()
  {
    return isWritable(SETTERVISIBILITY);
  }
  
  public boolean isSetterVisibilityReadable()
  {
    return isReadable(SETTERVISIBILITY);
  }
  
  public boolean isSetterVisibilityModified()
  {
    return isModified(SETTERVISIBILITY);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeEnumerationMdDTO getSetterVisibilityMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeEnumerationMdDTO) getAttributeDTO(SETTERVISIBILITY).getAttributeMdDTO();
  }
  
  public Boolean getSystem()
  {
    return com.runwaysdk.constants.MdAttributeBooleanUtil.getTypeSafeValue(getValue(SYSTEM));
  }
  
  public void setSystem(Boolean value)
  {
    if(value == null)
    {
      setValue(SYSTEM, "");
    }
    else
    {
      setValue(SYSTEM, java.lang.Boolean.toString(value));
    }
  }
  
  public boolean isSystemWritable()
  {
    return isWritable(SYSTEM);
  }
  
  public boolean isSystemReadable()
  {
    return isReadable(SYSTEM);
  }
  
  public boolean isSystemModified()
  {
    return isModified(SYSTEM);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeBooleanMdDTO getSystemMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeBooleanMdDTO) getAttributeDTO(SYSTEM).getAttributeMdDTO();
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.MdAttributeVirtualDTO> getAllVirtualAttribute()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdAttributeVirtualDTO>) getRequest().getChildren(this.getId(), com.runwaysdk.system.metadata.VirtualizeAttributeDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.MdAttributeVirtualDTO> getAllVirtualAttribute(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdAttributeVirtualDTO>) clientRequestIF.getChildren(id, com.runwaysdk.system.metadata.VirtualizeAttributeDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.VirtualizeAttributeDTO> getAllVirtualAttributeRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.VirtualizeAttributeDTO>) getRequest().getChildRelationships(this.getId(), com.runwaysdk.system.metadata.VirtualizeAttributeDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.VirtualizeAttributeDTO> getAllVirtualAttributeRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.VirtualizeAttributeDTO>) clientRequestIF.getChildRelationships(id, com.runwaysdk.system.metadata.VirtualizeAttributeDTO.CLASS);
  }
  
  public com.runwaysdk.system.metadata.VirtualizeAttributeDTO addVirtualAttribute(com.runwaysdk.system.metadata.MdAttributeVirtualDTO child)
  {
    return (com.runwaysdk.system.metadata.VirtualizeAttributeDTO) getRequest().addChild(this.getId(), child.getId(), com.runwaysdk.system.metadata.VirtualizeAttributeDTO.CLASS);
  }
  
  public static com.runwaysdk.system.metadata.VirtualizeAttributeDTO addVirtualAttribute(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id, com.runwaysdk.system.metadata.MdAttributeVirtualDTO child)
  {
    return (com.runwaysdk.system.metadata.VirtualizeAttributeDTO) clientRequestIF.addChild(id, child.getId(), com.runwaysdk.system.metadata.VirtualizeAttributeDTO.CLASS);
  }
  
  public void removeVirtualAttribute(com.runwaysdk.system.metadata.VirtualizeAttributeDTO relationship)
  {
    getRequest().deleteChild(relationship.getId());
  }
  
  public static void removeVirtualAttribute(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.metadata.VirtualizeAttributeDTO relationship)
  {
    clientRequestIF.deleteChild(relationship.getId());
  }
  
  public void removeAllVirtualAttribute()
  {
    getRequest().deleteChildren(this.getId(), com.runwaysdk.system.metadata.VirtualizeAttributeDTO.CLASS);
  }
  
  public static void removeAllVirtualAttribute(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    clientRequestIF.deleteChildren(id, com.runwaysdk.system.metadata.VirtualizeAttributeDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.MdClassDTO> getAllDefiningClass()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdClassDTO>) getRequest().getParents(this.getId(), com.runwaysdk.system.metadata.ClassAttributeConcreteDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.MdClassDTO> getAllDefiningClass(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdClassDTO>) clientRequestIF.getParents(id, com.runwaysdk.system.metadata.ClassAttributeConcreteDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.ClassAttributeConcreteDTO> getAllDefiningClassRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.ClassAttributeConcreteDTO>) getRequest().getParentRelationships(this.getId(), com.runwaysdk.system.metadata.ClassAttributeConcreteDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.ClassAttributeConcreteDTO> getAllDefiningClassRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.ClassAttributeConcreteDTO>) clientRequestIF.getParentRelationships(id, com.runwaysdk.system.metadata.ClassAttributeConcreteDTO.CLASS);
  }
  
  public com.runwaysdk.system.metadata.ClassAttributeConcreteDTO addDefiningClass(com.runwaysdk.system.metadata.MdClassDTO parent)
  {
    return (com.runwaysdk.system.metadata.ClassAttributeConcreteDTO) getRequest().addParent(parent.getId(), this.getId(), com.runwaysdk.system.metadata.ClassAttributeConcreteDTO.CLASS);
  }
  
  public static com.runwaysdk.system.metadata.ClassAttributeConcreteDTO addDefiningClass(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id, com.runwaysdk.system.metadata.MdClassDTO parent)
  {
    return (com.runwaysdk.system.metadata.ClassAttributeConcreteDTO) clientRequestIF.addParent(parent.getId(), id, com.runwaysdk.system.metadata.ClassAttributeConcreteDTO.CLASS);
  }
  
  public void removeDefiningClass(com.runwaysdk.system.metadata.ClassAttributeConcreteDTO relationship)
  {
    getRequest().deleteParent(relationship.getId());
  }
  
  public static void removeDefiningClass(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.metadata.ClassAttributeConcreteDTO relationship)
  {
    clientRequestIF.deleteParent(relationship.getId());
  }
  
  public void removeAllDefiningClass()
  {
    getRequest().deleteParents(this.getId(), com.runwaysdk.system.metadata.ClassAttributeConcreteDTO.CLASS);
  }
  
  public static void removeAllDefiningClass(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    clientRequestIF.deleteParents(id, com.runwaysdk.system.metadata.ClassAttributeConcreteDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.MdIndexDTO> getAllIndex()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdIndexDTO>) getRequest().getParents(this.getId(), com.runwaysdk.system.metadata.IndexAttributeDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.MdIndexDTO> getAllIndex(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdIndexDTO>) clientRequestIF.getParents(id, com.runwaysdk.system.metadata.IndexAttributeDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.IndexAttributeDTO> getAllIndexRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.IndexAttributeDTO>) getRequest().getParentRelationships(this.getId(), com.runwaysdk.system.metadata.IndexAttributeDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.IndexAttributeDTO> getAllIndexRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.IndexAttributeDTO>) clientRequestIF.getParentRelationships(id, com.runwaysdk.system.metadata.IndexAttributeDTO.CLASS);
  }
  
  public com.runwaysdk.system.metadata.IndexAttributeDTO addIndex(com.runwaysdk.system.metadata.MdIndexDTO parent)
  {
    return (com.runwaysdk.system.metadata.IndexAttributeDTO) getRequest().addParent(parent.getId(), this.getId(), com.runwaysdk.system.metadata.IndexAttributeDTO.CLASS);
  }
  
  public static com.runwaysdk.system.metadata.IndexAttributeDTO addIndex(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id, com.runwaysdk.system.metadata.MdIndexDTO parent)
  {
    return (com.runwaysdk.system.metadata.IndexAttributeDTO) clientRequestIF.addParent(parent.getId(), id, com.runwaysdk.system.metadata.IndexAttributeDTO.CLASS);
  }
  
  public void removeIndex(com.runwaysdk.system.metadata.IndexAttributeDTO relationship)
  {
    getRequest().deleteParent(relationship.getId());
  }
  
  public static void removeIndex(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.metadata.IndexAttributeDTO relationship)
  {
    clientRequestIF.deleteParent(relationship.getId());
  }
  
  public void removeAllIndex()
  {
    getRequest().deleteParents(this.getId(), com.runwaysdk.system.metadata.IndexAttributeDTO.CLASS);
  }
  
  public static void removeAllIndex(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    clientRequestIF.deleteParents(id, com.runwaysdk.system.metadata.IndexAttributeDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.MdDimensionDTO> getAllGetMdDimension()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdDimensionDTO>) getRequest().getParents(this.getId(), com.runwaysdk.system.metadata.DimensionDefinesLocalStructAttributeDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.MdDimensionDTO> getAllGetMdDimension(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdDimensionDTO>) clientRequestIF.getParents(id, com.runwaysdk.system.metadata.DimensionDefinesLocalStructAttributeDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.DimensionDefinesLocalStructAttributeDTO> getAllGetMdDimensionRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.DimensionDefinesLocalStructAttributeDTO>) getRequest().getParentRelationships(this.getId(), com.runwaysdk.system.metadata.DimensionDefinesLocalStructAttributeDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.DimensionDefinesLocalStructAttributeDTO> getAllGetMdDimensionRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.DimensionDefinesLocalStructAttributeDTO>) clientRequestIF.getParentRelationships(id, com.runwaysdk.system.metadata.DimensionDefinesLocalStructAttributeDTO.CLASS);
  }
  
  public com.runwaysdk.system.metadata.DimensionDefinesLocalStructAttributeDTO addGetMdDimension(com.runwaysdk.system.metadata.MdDimensionDTO parent)
  {
    return (com.runwaysdk.system.metadata.DimensionDefinesLocalStructAttributeDTO) getRequest().addParent(parent.getId(), this.getId(), com.runwaysdk.system.metadata.DimensionDefinesLocalStructAttributeDTO.CLASS);
  }
  
  public static com.runwaysdk.system.metadata.DimensionDefinesLocalStructAttributeDTO addGetMdDimension(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id, com.runwaysdk.system.metadata.MdDimensionDTO parent)
  {
    return (com.runwaysdk.system.metadata.DimensionDefinesLocalStructAttributeDTO) clientRequestIF.addParent(parent.getId(), id, com.runwaysdk.system.metadata.DimensionDefinesLocalStructAttributeDTO.CLASS);
  }
  
  public void removeGetMdDimension(com.runwaysdk.system.metadata.DimensionDefinesLocalStructAttributeDTO relationship)
  {
    getRequest().deleteParent(relationship.getId());
  }
  
  public static void removeGetMdDimension(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.metadata.DimensionDefinesLocalStructAttributeDTO relationship)
  {
    clientRequestIF.deleteParent(relationship.getId());
  }
  
  public void removeAllGetMdDimension()
  {
    getRequest().deleteParents(this.getId(), com.runwaysdk.system.metadata.DimensionDefinesLocalStructAttributeDTO.CLASS);
  }
  
  public static void removeAllGetMdDimension(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    clientRequestIF.deleteParents(id, com.runwaysdk.system.metadata.DimensionDefinesLocalStructAttributeDTO.CLASS);
  }
  
  public static com.runwaysdk.system.metadata.MdAttributeConcreteDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String id)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(id);
    
    return (com.runwaysdk.system.metadata.MdAttributeConcreteDTO) dto;
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
  
  public static com.runwaysdk.system.metadata.MdAttributeConcreteQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.metadata.MdAttributeConcreteQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.metadata.MdAttributeConcreteDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdAttributeConcreteDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdAttributeConcreteDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdAttributeConcreteDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdAttributeConcreteDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdAttributeConcreteDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdAttributeConcreteDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
