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

@com.runwaysdk.business.ClassSignature(hash = 1612781979)
public abstract class MdBusinessDTOBase extends com.runwaysdk.system.metadata.MdElementDTO
{
  public final static String CLASS = "com.runwaysdk.system.metadata.MdBusiness";
  private static final long serialVersionUID = 1612781979;
  
  protected MdBusinessDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected MdBusinessDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String CACHEALGORITHM = "cacheAlgorithm";
  public static java.lang.String SUPERMDBUSINESS = "superMdBusiness";
  @SuppressWarnings("unchecked")
  public java.util.List<com.runwaysdk.system.metadata.ClassCacheDTO> getCacheAlgorithm()
  {
    return (java.util.List<com.runwaysdk.system.metadata.ClassCacheDTO>) com.runwaysdk.transport.conversion.ConversionFacade.convertEnumDTOsFromEnumNames(getRequest(), com.runwaysdk.system.metadata.ClassCacheDTO.CLASS, getEnumNames(CACHEALGORITHM));
  }
  
  public java.util.List<String> getCacheAlgorithmEnumNames()
  {
    return getEnumNames(CACHEALGORITHM);
  }
  
  public void addCacheAlgorithm(com.runwaysdk.system.metadata.ClassCacheDTO enumDTO)
  {
    addEnumItem(CACHEALGORITHM, enumDTO.toString());
  }
  
  public void removeCacheAlgorithm(com.runwaysdk.system.metadata.ClassCacheDTO enumDTO)
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
  
  public com.runwaysdk.system.metadata.MdBusinessDTO getSuperMdBusiness()
  {
    if(getValue(SUPERMDBUSINESS) == null || getValue(SUPERMDBUSINESS).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.metadata.MdBusinessDTO.get(getRequest(), getValue(SUPERMDBUSINESS));
    }
  }
  
  public String getSuperMdBusinessId()
  {
    return getValue(SUPERMDBUSINESS);
  }
  
  public void setSuperMdBusiness(com.runwaysdk.system.metadata.MdBusinessDTO value)
  {
    if(value == null)
    {
      setValue(SUPERMDBUSINESS, "");
    }
    else
    {
      setValue(SUPERMDBUSINESS, value.getId());
    }
  }
  
  public boolean isSuperMdBusinessWritable()
  {
    return isWritable(SUPERMDBUSINESS);
  }
  
  public boolean isSuperMdBusinessReadable()
  {
    return isReadable(SUPERMDBUSINESS);
  }
  
  public boolean isSuperMdBusinessModified()
  {
    return isModified(SUPERMDBUSINESS);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getSuperMdBusinessMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(SUPERMDBUSINESS).getAttributeMdDTO();
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.MdStateMachineDTO> getAllDefiningMdBusiness()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdStateMachineDTO>) getRequest().getChildren(this.getId(), com.runwaysdk.system.DefinesStateMachineDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.MdStateMachineDTO> getAllDefiningMdBusiness(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdStateMachineDTO>) clientRequestIF.getChildren(id, com.runwaysdk.system.DefinesStateMachineDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.DefinesStateMachineDTO> getAllDefiningMdBusinessRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.DefinesStateMachineDTO>) getRequest().getChildRelationships(this.getId(), com.runwaysdk.system.DefinesStateMachineDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.DefinesStateMachineDTO> getAllDefiningMdBusinessRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.DefinesStateMachineDTO>) clientRequestIF.getChildRelationships(id, com.runwaysdk.system.DefinesStateMachineDTO.CLASS);
  }
  
  public com.runwaysdk.system.DefinesStateMachineDTO addDefiningMdBusiness(com.runwaysdk.system.metadata.MdStateMachineDTO child)
  {
    return (com.runwaysdk.system.DefinesStateMachineDTO) getRequest().addChild(this.getId(), child.getId(), com.runwaysdk.system.DefinesStateMachineDTO.CLASS);
  }
  
  public static com.runwaysdk.system.DefinesStateMachineDTO addDefiningMdBusiness(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id, com.runwaysdk.system.metadata.MdStateMachineDTO child)
  {
    return (com.runwaysdk.system.DefinesStateMachineDTO) clientRequestIF.addChild(id, child.getId(), com.runwaysdk.system.DefinesStateMachineDTO.CLASS);
  }
  
  public void removeDefiningMdBusiness(com.runwaysdk.system.DefinesStateMachineDTO relationship)
  {
    getRequest().deleteChild(relationship.getId());
  }
  
  public static void removeDefiningMdBusiness(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.DefinesStateMachineDTO relationship)
  {
    clientRequestIF.deleteChild(relationship.getId());
  }
  
  public void removeAllDefiningMdBusiness()
  {
    getRequest().deleteChildren(this.getId(), com.runwaysdk.system.DefinesStateMachineDTO.CLASS);
  }
  
  public static void removeAllDefiningMdBusiness(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    clientRequestIF.deleteChildren(id, com.runwaysdk.system.DefinesStateMachineDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.MdEnumerationDTO> getAllMdEnumeration()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdEnumerationDTO>) getRequest().getChildren(this.getId(), com.runwaysdk.system.metadata.EnumerationAttributeDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.MdEnumerationDTO> getAllMdEnumeration(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdEnumerationDTO>) clientRequestIF.getChildren(id, com.runwaysdk.system.metadata.EnumerationAttributeDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.EnumerationAttributeDTO> getAllMdEnumerationRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.EnumerationAttributeDTO>) getRequest().getChildRelationships(this.getId(), com.runwaysdk.system.metadata.EnumerationAttributeDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.EnumerationAttributeDTO> getAllMdEnumerationRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.EnumerationAttributeDTO>) clientRequestIF.getChildRelationships(id, com.runwaysdk.system.metadata.EnumerationAttributeDTO.CLASS);
  }
  
  public com.runwaysdk.system.metadata.EnumerationAttributeDTO addMdEnumeration(com.runwaysdk.system.metadata.MdEnumerationDTO child)
  {
    return (com.runwaysdk.system.metadata.EnumerationAttributeDTO) getRequest().addChild(this.getId(), child.getId(), com.runwaysdk.system.metadata.EnumerationAttributeDTO.CLASS);
  }
  
  public static com.runwaysdk.system.metadata.EnumerationAttributeDTO addMdEnumeration(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id, com.runwaysdk.system.metadata.MdEnumerationDTO child)
  {
    return (com.runwaysdk.system.metadata.EnumerationAttributeDTO) clientRequestIF.addChild(id, child.getId(), com.runwaysdk.system.metadata.EnumerationAttributeDTO.CLASS);
  }
  
  public void removeMdEnumeration(com.runwaysdk.system.metadata.EnumerationAttributeDTO relationship)
  {
    getRequest().deleteChild(relationship.getId());
  }
  
  public static void removeMdEnumeration(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.metadata.EnumerationAttributeDTO relationship)
  {
    clientRequestIF.deleteChild(relationship.getId());
  }
  
  public void removeAllMdEnumeration()
  {
    getRequest().deleteChildren(this.getId(), com.runwaysdk.system.metadata.EnumerationAttributeDTO.CLASS);
  }
  
  public static void removeAllMdEnumeration(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    clientRequestIF.deleteChildren(id, com.runwaysdk.system.metadata.EnumerationAttributeDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.MdBusinessDTO> getAllSubClass()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdBusinessDTO>) getRequest().getChildren(this.getId(), com.runwaysdk.system.metadata.BusinessInheritanceDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.MdBusinessDTO> getAllSubClass(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdBusinessDTO>) clientRequestIF.getChildren(id, com.runwaysdk.system.metadata.BusinessInheritanceDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.BusinessInheritanceDTO> getAllSubClassRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.BusinessInheritanceDTO>) getRequest().getChildRelationships(this.getId(), com.runwaysdk.system.metadata.BusinessInheritanceDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.BusinessInheritanceDTO> getAllSubClassRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.BusinessInheritanceDTO>) clientRequestIF.getChildRelationships(id, com.runwaysdk.system.metadata.BusinessInheritanceDTO.CLASS);
  }
  
  public com.runwaysdk.system.metadata.BusinessInheritanceDTO addSubClass(com.runwaysdk.system.metadata.MdBusinessDTO child)
  {
    return (com.runwaysdk.system.metadata.BusinessInheritanceDTO) getRequest().addChild(this.getId(), child.getId(), com.runwaysdk.system.metadata.BusinessInheritanceDTO.CLASS);
  }
  
  public static com.runwaysdk.system.metadata.BusinessInheritanceDTO addSubClass(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id, com.runwaysdk.system.metadata.MdBusinessDTO child)
  {
    return (com.runwaysdk.system.metadata.BusinessInheritanceDTO) clientRequestIF.addChild(id, child.getId(), com.runwaysdk.system.metadata.BusinessInheritanceDTO.CLASS);
  }
  
  public void removeSubClass(com.runwaysdk.system.metadata.BusinessInheritanceDTO relationship)
  {
    getRequest().deleteChild(relationship.getId());
  }
  
  public static void removeSubClass(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.metadata.BusinessInheritanceDTO relationship)
  {
    clientRequestIF.deleteChild(relationship.getId());
  }
  
  public void removeAllSubClass()
  {
    getRequest().deleteChildren(this.getId(), com.runwaysdk.system.metadata.BusinessInheritanceDTO.CLASS);
  }
  
  public static void removeAllSubClass(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    clientRequestIF.deleteChildren(id, com.runwaysdk.system.metadata.BusinessInheritanceDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.MdBusinessDTO> getAllInheritsFromClass()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdBusinessDTO>) getRequest().getParents(this.getId(), com.runwaysdk.system.metadata.BusinessInheritanceDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.MdBusinessDTO> getAllInheritsFromClass(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdBusinessDTO>) clientRequestIF.getParents(id, com.runwaysdk.system.metadata.BusinessInheritanceDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.BusinessInheritanceDTO> getAllInheritsFromClassRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.BusinessInheritanceDTO>) getRequest().getParentRelationships(this.getId(), com.runwaysdk.system.metadata.BusinessInheritanceDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.BusinessInheritanceDTO> getAllInheritsFromClassRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.BusinessInheritanceDTO>) clientRequestIF.getParentRelationships(id, com.runwaysdk.system.metadata.BusinessInheritanceDTO.CLASS);
  }
  
  public com.runwaysdk.system.metadata.BusinessInheritanceDTO addInheritsFromClass(com.runwaysdk.system.metadata.MdBusinessDTO parent)
  {
    return (com.runwaysdk.system.metadata.BusinessInheritanceDTO) getRequest().addParent(parent.getId(), this.getId(), com.runwaysdk.system.metadata.BusinessInheritanceDTO.CLASS);
  }
  
  public static com.runwaysdk.system.metadata.BusinessInheritanceDTO addInheritsFromClass(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id, com.runwaysdk.system.metadata.MdBusinessDTO parent)
  {
    return (com.runwaysdk.system.metadata.BusinessInheritanceDTO) clientRequestIF.addParent(parent.getId(), id, com.runwaysdk.system.metadata.BusinessInheritanceDTO.CLASS);
  }
  
  public void removeInheritsFromClass(com.runwaysdk.system.metadata.BusinessInheritanceDTO relationship)
  {
    getRequest().deleteParent(relationship.getId());
  }
  
  public static void removeInheritsFromClass(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.metadata.BusinessInheritanceDTO relationship)
  {
    clientRequestIF.deleteParent(relationship.getId());
  }
  
  public void removeAllInheritsFromClass()
  {
    getRequest().deleteParents(this.getId(), com.runwaysdk.system.metadata.BusinessInheritanceDTO.CLASS);
  }
  
  public static void removeAllInheritsFromClass(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    clientRequestIF.deleteParents(id, com.runwaysdk.system.metadata.BusinessInheritanceDTO.CLASS);
  }
  
  public static com.runwaysdk.system.metadata.MdBusinessDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String id)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(id);
    
    return (com.runwaysdk.system.metadata.MdBusinessDTO) dto;
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
  
  public static com.runwaysdk.system.metadata.MdBusinessQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.metadata.MdBusinessQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.metadata.MdBusinessDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdBusinessDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdBusinessDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdBusinessDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdBusinessDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdBusinessDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdBusinessDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
