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

@com.runwaysdk.business.ClassSignature(hash = -1067595411)
public abstract class MdRelationshipDTOBase extends com.runwaysdk.system.metadata.MdElementDTO
{
  public final static String CLASS = "com.runwaysdk.system.metadata.MdRelationship";
  private static final long serialVersionUID = -1067595411;
  
  protected MdRelationshipDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected MdRelationshipDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String CACHEALGORITHM = "cacheAlgorithm";
  public static java.lang.String CHILDCARDINALITY = "childCardinality";
  public static java.lang.String CHILDDISPLAYLABEL = "childDisplayLabel";
  public static java.lang.String CHILDMDBUSINESS = "childMdBusiness";
  public static java.lang.String CHILDMETHOD = "childMethod";
  public static java.lang.String CHILDVISIBILITY = "childVisibility";
  public static java.lang.String COMPOSITION = "composition";
  public static java.lang.String INDEX1NAME = "index1Name";
  public static java.lang.String INDEX2NAME = "index2Name";
  public static java.lang.String PARENTCARDINALITY = "parentCardinality";
  public static java.lang.String PARENTDISPLAYLABEL = "parentDisplayLabel";
  public static java.lang.String PARENTMDBUSINESS = "parentMdBusiness";
  public static java.lang.String PARENTMETHOD = "parentMethod";
  public static java.lang.String PARENTVISIBILITY = "parentVisibility";
  public static java.lang.String SORTMDATTRIBUTE = "sortMdAttribute";
  public static java.lang.String SUPERMDRELATIONSHIP = "superMdRelationship";
  @SuppressWarnings("unchecked")
  public java.util.List<com.runwaysdk.system.metadata.RelationshipCacheDTO> getCacheAlgorithm()
  {
    return (java.util.List<com.runwaysdk.system.metadata.RelationshipCacheDTO>) com.runwaysdk.transport.conversion.ConversionFacade.convertEnumDTOsFromEnumNames(getRequest(), com.runwaysdk.system.metadata.RelationshipCacheDTO.CLASS, getEnumNames(CACHEALGORITHM));
  }
  
  public java.util.List<String> getCacheAlgorithmEnumNames()
  {
    return getEnumNames(CACHEALGORITHM);
  }
  
  public void addCacheAlgorithm(com.runwaysdk.system.metadata.RelationshipCacheDTO enumDTO)
  {
    addEnumItem(CACHEALGORITHM, enumDTO.toString());
  }
  
  public void removeCacheAlgorithm(com.runwaysdk.system.metadata.RelationshipCacheDTO enumDTO)
  {
    removeEnumItem(CACHEALGORITHM, enumDTO.toString());
  }
  
  public void clearCacheAlgorithm()
  {
    clearEnum(CACHEALGORITHM);
  }
  
  public boolean isCacheAlgorithmWritable()
  {
    return isWritable(CACHEALGORITHM);
  }
  
  public boolean isCacheAlgorithmReadable()
  {
    return isReadable(CACHEALGORITHM);
  }
  
  public boolean isCacheAlgorithmModified()
  {
    return isModified(CACHEALGORITHM);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeEnumerationMdDTO getCacheAlgorithmMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeEnumerationMdDTO) getAttributeDTO(CACHEALGORITHM).getAttributeMdDTO();
  }
  
  public String getChildCardinality()
  {
    return getValue(CHILDCARDINALITY);
  }
  
  public void setChildCardinality(String value)
  {
    if(value == null)
    {
      setValue(CHILDCARDINALITY, "");
    }
    else
    {
      setValue(CHILDCARDINALITY, value);
    }
  }
  
  public boolean isChildCardinalityWritable()
  {
    return isWritable(CHILDCARDINALITY);
  }
  
  public boolean isChildCardinalityReadable()
  {
    return isReadable(CHILDCARDINALITY);
  }
  
  public boolean isChildCardinalityModified()
  {
    return isModified(CHILDCARDINALITY);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getChildCardinalityMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(CHILDCARDINALITY).getAttributeMdDTO();
  }
  
  public com.runwaysdk.system.metadata.MetadataDisplayLabelDTO getChildDisplayLabel()
  {
    return (com.runwaysdk.system.metadata.MetadataDisplayLabelDTO) this.getAttributeStructDTO(CHILDDISPLAYLABEL).getStructDTO();
  }
  
  public boolean isChildDisplayLabelWritable()
  {
    return isWritable(CHILDDISPLAYLABEL);
  }
  
  public boolean isChildDisplayLabelReadable()
  {
    return isReadable(CHILDDISPLAYLABEL);
  }
  
  public boolean isChildDisplayLabelModified()
  {
    return isModified(CHILDDISPLAYLABEL);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeLocalCharacterMdDTO getChildDisplayLabelMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeLocalCharacterMdDTO) getAttributeDTO(CHILDDISPLAYLABEL).getAttributeMdDTO();
  }
  
  public com.runwaysdk.system.metadata.MdBusinessDTO getChildMdBusiness()
  {
    if(getValue(CHILDMDBUSINESS) == null || getValue(CHILDMDBUSINESS).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.metadata.MdBusinessDTO.get(getRequest(), getValue(CHILDMDBUSINESS));
    }
  }
  
  public String getChildMdBusinessId()
  {
    return getValue(CHILDMDBUSINESS);
  }
  
  public void setChildMdBusiness(com.runwaysdk.system.metadata.MdBusinessDTO value)
  {
    if(value == null)
    {
      setValue(CHILDMDBUSINESS, "");
    }
    else
    {
      setValue(CHILDMDBUSINESS, value.getId());
    }
  }
  
  public boolean isChildMdBusinessWritable()
  {
    return isWritable(CHILDMDBUSINESS);
  }
  
  public boolean isChildMdBusinessReadable()
  {
    return isReadable(CHILDMDBUSINESS);
  }
  
  public boolean isChildMdBusinessModified()
  {
    return isModified(CHILDMDBUSINESS);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getChildMdBusinessMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(CHILDMDBUSINESS).getAttributeMdDTO();
  }
  
  public String getChildMethod()
  {
    return getValue(CHILDMETHOD);
  }
  
  public void setChildMethod(String value)
  {
    if(value == null)
    {
      setValue(CHILDMETHOD, "");
    }
    else
    {
      setValue(CHILDMETHOD, value);
    }
  }
  
  public boolean isChildMethodWritable()
  {
    return isWritable(CHILDMETHOD);
  }
  
  public boolean isChildMethodReadable()
  {
    return isReadable(CHILDMETHOD);
  }
  
  public boolean isChildMethodModified()
  {
    return isModified(CHILDMETHOD);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getChildMethodMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(CHILDMETHOD).getAttributeMdDTO();
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<com.runwaysdk.system.metadata.VisibilityModifierDTO> getChildVisibility()
  {
    return (java.util.List<com.runwaysdk.system.metadata.VisibilityModifierDTO>) com.runwaysdk.transport.conversion.ConversionFacade.convertEnumDTOsFromEnumNames(getRequest(), com.runwaysdk.system.metadata.VisibilityModifierDTO.CLASS, getEnumNames(CHILDVISIBILITY));
  }
  
  public java.util.List<String> getChildVisibilityEnumNames()
  {
    return getEnumNames(CHILDVISIBILITY);
  }
  
  public void addChildVisibility(com.runwaysdk.system.metadata.VisibilityModifierDTO enumDTO)
  {
    addEnumItem(CHILDVISIBILITY, enumDTO.toString());
  }
  
  public void removeChildVisibility(com.runwaysdk.system.metadata.VisibilityModifierDTO enumDTO)
  {
    removeEnumItem(CHILDVISIBILITY, enumDTO.toString());
  }
  
  public void clearChildVisibility()
  {
    clearEnum(CHILDVISIBILITY);
  }
  
  public boolean isChildVisibilityWritable()
  {
    return isWritable(CHILDVISIBILITY);
  }
  
  public boolean isChildVisibilityReadable()
  {
    return isReadable(CHILDVISIBILITY);
  }
  
  public boolean isChildVisibilityModified()
  {
    return isModified(CHILDVISIBILITY);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeEnumerationMdDTO getChildVisibilityMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeEnumerationMdDTO) getAttributeDTO(CHILDVISIBILITY).getAttributeMdDTO();
  }
  
  public Boolean getComposition()
  {
    return com.runwaysdk.constants.MdAttributeBooleanUtil.getTypeSafeValue(getValue(COMPOSITION));
  }
  
  public void setComposition(Boolean value)
  {
    if(value == null)
    {
      setValue(COMPOSITION, "");
    }
    else
    {
      setValue(COMPOSITION, java.lang.Boolean.toString(value));
    }
  }
  
  public boolean isCompositionWritable()
  {
    return isWritable(COMPOSITION);
  }
  
  public boolean isCompositionReadable()
  {
    return isReadable(COMPOSITION);
  }
  
  public boolean isCompositionModified()
  {
    return isModified(COMPOSITION);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeBooleanMdDTO getCompositionMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeBooleanMdDTO) getAttributeDTO(COMPOSITION).getAttributeMdDTO();
  }
  
  public String getIndex1Name()
  {
    return getValue(INDEX1NAME);
  }
  
  public boolean isIndex1NameWritable()
  {
    return isWritable(INDEX1NAME);
  }
  
  public boolean isIndex1NameReadable()
  {
    return isReadable(INDEX1NAME);
  }
  
  public boolean isIndex1NameModified()
  {
    return isModified(INDEX1NAME);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getIndex1NameMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(INDEX1NAME).getAttributeMdDTO();
  }
  
  public String getIndex2Name()
  {
    return getValue(INDEX2NAME);
  }
  
  public boolean isIndex2NameWritable()
  {
    return isWritable(INDEX2NAME);
  }
  
  public boolean isIndex2NameReadable()
  {
    return isReadable(INDEX2NAME);
  }
  
  public boolean isIndex2NameModified()
  {
    return isModified(INDEX2NAME);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getIndex2NameMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(INDEX2NAME).getAttributeMdDTO();
  }
  
  public String getParentCardinality()
  {
    return getValue(PARENTCARDINALITY);
  }
  
  public void setParentCardinality(String value)
  {
    if(value == null)
    {
      setValue(PARENTCARDINALITY, "");
    }
    else
    {
      setValue(PARENTCARDINALITY, value);
    }
  }
  
  public boolean isParentCardinalityWritable()
  {
    return isWritable(PARENTCARDINALITY);
  }
  
  public boolean isParentCardinalityReadable()
  {
    return isReadable(PARENTCARDINALITY);
  }
  
  public boolean isParentCardinalityModified()
  {
    return isModified(PARENTCARDINALITY);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getParentCardinalityMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(PARENTCARDINALITY).getAttributeMdDTO();
  }
  
  public com.runwaysdk.system.metadata.MetadataDisplayLabelDTO getParentDisplayLabel()
  {
    return (com.runwaysdk.system.metadata.MetadataDisplayLabelDTO) this.getAttributeStructDTO(PARENTDISPLAYLABEL).getStructDTO();
  }
  
  public boolean isParentDisplayLabelWritable()
  {
    return isWritable(PARENTDISPLAYLABEL);
  }
  
  public boolean isParentDisplayLabelReadable()
  {
    return isReadable(PARENTDISPLAYLABEL);
  }
  
  public boolean isParentDisplayLabelModified()
  {
    return isModified(PARENTDISPLAYLABEL);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeLocalCharacterMdDTO getParentDisplayLabelMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeLocalCharacterMdDTO) getAttributeDTO(PARENTDISPLAYLABEL).getAttributeMdDTO();
  }
  
  public com.runwaysdk.system.metadata.MdBusinessDTO getParentMdBusiness()
  {
    if(getValue(PARENTMDBUSINESS) == null || getValue(PARENTMDBUSINESS).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.metadata.MdBusinessDTO.get(getRequest(), getValue(PARENTMDBUSINESS));
    }
  }
  
  public String getParentMdBusinessId()
  {
    return getValue(PARENTMDBUSINESS);
  }
  
  public void setParentMdBusiness(com.runwaysdk.system.metadata.MdBusinessDTO value)
  {
    if(value == null)
    {
      setValue(PARENTMDBUSINESS, "");
    }
    else
    {
      setValue(PARENTMDBUSINESS, value.getId());
    }
  }
  
  public boolean isParentMdBusinessWritable()
  {
    return isWritable(PARENTMDBUSINESS);
  }
  
  public boolean isParentMdBusinessReadable()
  {
    return isReadable(PARENTMDBUSINESS);
  }
  
  public boolean isParentMdBusinessModified()
  {
    return isModified(PARENTMDBUSINESS);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getParentMdBusinessMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(PARENTMDBUSINESS).getAttributeMdDTO();
  }
  
  public String getParentMethod()
  {
    return getValue(PARENTMETHOD);
  }
  
  public void setParentMethod(String value)
  {
    if(value == null)
    {
      setValue(PARENTMETHOD, "");
    }
    else
    {
      setValue(PARENTMETHOD, value);
    }
  }
  
  public boolean isParentMethodWritable()
  {
    return isWritable(PARENTMETHOD);
  }
  
  public boolean isParentMethodReadable()
  {
    return isReadable(PARENTMETHOD);
  }
  
  public boolean isParentMethodModified()
  {
    return isModified(PARENTMETHOD);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getParentMethodMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(PARENTMETHOD).getAttributeMdDTO();
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<com.runwaysdk.system.metadata.VisibilityModifierDTO> getParentVisibility()
  {
    return (java.util.List<com.runwaysdk.system.metadata.VisibilityModifierDTO>) com.runwaysdk.transport.conversion.ConversionFacade.convertEnumDTOsFromEnumNames(getRequest(), com.runwaysdk.system.metadata.VisibilityModifierDTO.CLASS, getEnumNames(PARENTVISIBILITY));
  }
  
  public java.util.List<String> getParentVisibilityEnumNames()
  {
    return getEnumNames(PARENTVISIBILITY);
  }
  
  public void addParentVisibility(com.runwaysdk.system.metadata.VisibilityModifierDTO enumDTO)
  {
    addEnumItem(PARENTVISIBILITY, enumDTO.toString());
  }
  
  public void removeParentVisibility(com.runwaysdk.system.metadata.VisibilityModifierDTO enumDTO)
  {
    removeEnumItem(PARENTVISIBILITY, enumDTO.toString());
  }
  
  public void clearParentVisibility()
  {
    clearEnum(PARENTVISIBILITY);
  }
  
  public boolean isParentVisibilityWritable()
  {
    return isWritable(PARENTVISIBILITY);
  }
  
  public boolean isParentVisibilityReadable()
  {
    return isReadable(PARENTVISIBILITY);
  }
  
  public boolean isParentVisibilityModified()
  {
    return isModified(PARENTVISIBILITY);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeEnumerationMdDTO getParentVisibilityMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeEnumerationMdDTO) getAttributeDTO(PARENTVISIBILITY).getAttributeMdDTO();
  }
  
  public com.runwaysdk.system.metadata.MdAttributePrimitiveDTO getSortMdAttribute()
  {
    if(getValue(SORTMDATTRIBUTE) == null || getValue(SORTMDATTRIBUTE).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.metadata.MdAttributePrimitiveDTO.get(getRequest(), getValue(SORTMDATTRIBUTE));
    }
  }
  
  public String getSortMdAttributeId()
  {
    return getValue(SORTMDATTRIBUTE);
  }
  
  public void setSortMdAttribute(com.runwaysdk.system.metadata.MdAttributePrimitiveDTO value)
  {
    if(value == null)
    {
      setValue(SORTMDATTRIBUTE, "");
    }
    else
    {
      setValue(SORTMDATTRIBUTE, value.getId());
    }
  }
  
  public boolean isSortMdAttributeWritable()
  {
    return isWritable(SORTMDATTRIBUTE);
  }
  
  public boolean isSortMdAttributeReadable()
  {
    return isReadable(SORTMDATTRIBUTE);
  }
  
  public boolean isSortMdAttributeModified()
  {
    return isModified(SORTMDATTRIBUTE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getSortMdAttributeMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(SORTMDATTRIBUTE).getAttributeMdDTO();
  }
  
  public com.runwaysdk.system.metadata.MdRelationshipDTO getSuperMdRelationship()
  {
    if(getValue(SUPERMDRELATIONSHIP) == null || getValue(SUPERMDRELATIONSHIP).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.metadata.MdRelationshipDTO.get(getRequest(), getValue(SUPERMDRELATIONSHIP));
    }
  }
  
  public String getSuperMdRelationshipId()
  {
    return getValue(SUPERMDRELATIONSHIP);
  }
  
  public void setSuperMdRelationship(com.runwaysdk.system.metadata.MdRelationshipDTO value)
  {
    if(value == null)
    {
      setValue(SUPERMDRELATIONSHIP, "");
    }
    else
    {
      setValue(SUPERMDRELATIONSHIP, value.getId());
    }
  }
  
  public boolean isSuperMdRelationshipWritable()
  {
    return isWritable(SUPERMDRELATIONSHIP);
  }
  
  public boolean isSuperMdRelationshipReadable()
  {
    return isReadable(SUPERMDRELATIONSHIP);
  }
  
  public boolean isSuperMdRelationshipModified()
  {
    return isModified(SUPERMDRELATIONSHIP);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getSuperMdRelationshipMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(SUPERMDRELATIONSHIP).getAttributeMdDTO();
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.MdRelationshipDTO> getAllSubRelationship()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdRelationshipDTO>) getRequest().getChildren(this.getId(), com.runwaysdk.system.metadata.RelationshipInheritanceDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.MdRelationshipDTO> getAllSubRelationship(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdRelationshipDTO>) clientRequestIF.getChildren(id, com.runwaysdk.system.metadata.RelationshipInheritanceDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.RelationshipInheritanceDTO> getAllSubRelationshipRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.RelationshipInheritanceDTO>) getRequest().getChildRelationships(this.getId(), com.runwaysdk.system.metadata.RelationshipInheritanceDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.RelationshipInheritanceDTO> getAllSubRelationshipRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.RelationshipInheritanceDTO>) clientRequestIF.getChildRelationships(id, com.runwaysdk.system.metadata.RelationshipInheritanceDTO.CLASS);
  }
  
  public com.runwaysdk.system.metadata.RelationshipInheritanceDTO addSubRelationship(com.runwaysdk.system.metadata.MdRelationshipDTO child)
  {
    return (com.runwaysdk.system.metadata.RelationshipInheritanceDTO) getRequest().addChild(this.getId(), child.getId(), com.runwaysdk.system.metadata.RelationshipInheritanceDTO.CLASS);
  }
  
  public static com.runwaysdk.system.metadata.RelationshipInheritanceDTO addSubRelationship(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id, com.runwaysdk.system.metadata.MdRelationshipDTO child)
  {
    return (com.runwaysdk.system.metadata.RelationshipInheritanceDTO) clientRequestIF.addChild(id, child.getId(), com.runwaysdk.system.metadata.RelationshipInheritanceDTO.CLASS);
  }
  
  public void removeSubRelationship(com.runwaysdk.system.metadata.RelationshipInheritanceDTO relationship)
  {
    getRequest().deleteChild(relationship.getId());
  }
  
  public static void removeSubRelationship(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.metadata.RelationshipInheritanceDTO relationship)
  {
    clientRequestIF.deleteChild(relationship.getId());
  }
  
  public void removeAllSubRelationship()
  {
    getRequest().deleteChildren(this.getId(), com.runwaysdk.system.metadata.RelationshipInheritanceDTO.CLASS);
  }
  
  public static void removeAllSubRelationship(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    clientRequestIF.deleteChildren(id, com.runwaysdk.system.metadata.RelationshipInheritanceDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.MdRelationshipDTO> getAllInheritsFromRelationship()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdRelationshipDTO>) getRequest().getParents(this.getId(), com.runwaysdk.system.metadata.RelationshipInheritanceDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.MdRelationshipDTO> getAllInheritsFromRelationship(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdRelationshipDTO>) clientRequestIF.getParents(id, com.runwaysdk.system.metadata.RelationshipInheritanceDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.RelationshipInheritanceDTO> getAllInheritsFromRelationshipRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.RelationshipInheritanceDTO>) getRequest().getParentRelationships(this.getId(), com.runwaysdk.system.metadata.RelationshipInheritanceDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.RelationshipInheritanceDTO> getAllInheritsFromRelationshipRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.RelationshipInheritanceDTO>) clientRequestIF.getParentRelationships(id, com.runwaysdk.system.metadata.RelationshipInheritanceDTO.CLASS);
  }
  
  public com.runwaysdk.system.metadata.RelationshipInheritanceDTO addInheritsFromRelationship(com.runwaysdk.system.metadata.MdRelationshipDTO parent)
  {
    return (com.runwaysdk.system.metadata.RelationshipInheritanceDTO) getRequest().addParent(parent.getId(), this.getId(), com.runwaysdk.system.metadata.RelationshipInheritanceDTO.CLASS);
  }
  
  public static com.runwaysdk.system.metadata.RelationshipInheritanceDTO addInheritsFromRelationship(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id, com.runwaysdk.system.metadata.MdRelationshipDTO parent)
  {
    return (com.runwaysdk.system.metadata.RelationshipInheritanceDTO) clientRequestIF.addParent(parent.getId(), id, com.runwaysdk.system.metadata.RelationshipInheritanceDTO.CLASS);
  }
  
  public void removeInheritsFromRelationship(com.runwaysdk.system.metadata.RelationshipInheritanceDTO relationship)
  {
    getRequest().deleteParent(relationship.getId());
  }
  
  public static void removeInheritsFromRelationship(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.metadata.RelationshipInheritanceDTO relationship)
  {
    clientRequestIF.deleteParent(relationship.getId());
  }
  
  public void removeAllInheritsFromRelationship()
  {
    getRequest().deleteParents(this.getId(), com.runwaysdk.system.metadata.RelationshipInheritanceDTO.CLASS);
  }
  
  public static void removeAllInheritsFromRelationship(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    clientRequestIF.deleteParents(id, com.runwaysdk.system.metadata.RelationshipInheritanceDTO.CLASS);
  }
  
  public static com.runwaysdk.system.metadata.MdRelationshipDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String id)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(id);
    
    return (com.runwaysdk.system.metadata.MdRelationshipDTO) dto;
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
  
  public static com.runwaysdk.system.metadata.MdRelationshipQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.metadata.MdRelationshipQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.metadata.MdRelationshipDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdRelationshipDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdRelationshipDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdRelationshipDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdRelationshipDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdRelationshipDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdRelationshipDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
