/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
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
package com.runwaysdk.system.gis.mapping;

@com.runwaysdk.business.ClassSignature(hash = -6916393)
public abstract class PrimitiveRuleDTOBase extends com.runwaysdk.system.gis.mapping.ThematicRuleDTO
{
  public final static String CLASS = "com.runwaysdk.system.gis.mapping.PrimitiveRule";
  private static final long serialVersionUID = -6916393;
  
  protected PrimitiveRuleDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected PrimitiveRuleDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String ATTRIBUTEVALUE = "attributeValue";
  public String getAttributeValue()
  {
    return getValue(ATTRIBUTEVALUE);
  }
  
  public void setAttributeValue(String value)
  {
    if(value == null)
    {
      setValue(ATTRIBUTEVALUE, "");
    }
    else
    {
      setValue(ATTRIBUTEVALUE, value);
    }
  }
  
  public boolean isAttributeValueWritable()
  {
    return isWritable(ATTRIBUTEVALUE);
  }
  
  public boolean isAttributeValueReadable()
  {
    return isReadable(ATTRIBUTEVALUE);
  }
  
  public boolean isAttributeValueModified()
  {
    return isModified(ATTRIBUTEVALUE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getAttributeValueMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(ATTRIBUTEVALUE).getAttributeMdDTO();
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.gis.mapping.CompositeRuleDTO> getAllCompositeRule()
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.mapping.CompositeRuleDTO>) getRequest().getParents(this.getOid(), com.runwaysdk.system.gis.mapping.HasPrimitiveRuleDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.gis.mapping.CompositeRuleDTO> getAllCompositeRule(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.mapping.CompositeRuleDTO>) clientRequestIF.getParents(oid, com.runwaysdk.system.gis.mapping.HasPrimitiveRuleDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.gis.mapping.HasPrimitiveRuleDTO> getAllCompositeRuleRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.mapping.HasPrimitiveRuleDTO>) getRequest().getParentRelationships(this.getOid(), com.runwaysdk.system.gis.mapping.HasPrimitiveRuleDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.gis.mapping.HasPrimitiveRuleDTO> getAllCompositeRuleRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.mapping.HasPrimitiveRuleDTO>) clientRequestIF.getParentRelationships(oid, com.runwaysdk.system.gis.mapping.HasPrimitiveRuleDTO.CLASS);
  }
  
  public com.runwaysdk.system.gis.mapping.HasPrimitiveRuleDTO addCompositeRule(com.runwaysdk.system.gis.mapping.CompositeRuleDTO parent)
  {
    return (com.runwaysdk.system.gis.mapping.HasPrimitiveRuleDTO) getRequest().addParent(parent.getOid(), this.getOid(), com.runwaysdk.system.gis.mapping.HasPrimitiveRuleDTO.CLASS);
  }
  
  public static com.runwaysdk.system.gis.mapping.HasPrimitiveRuleDTO addCompositeRule(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid, com.runwaysdk.system.gis.mapping.CompositeRuleDTO parent)
  {
    return (com.runwaysdk.system.gis.mapping.HasPrimitiveRuleDTO) clientRequestIF.addParent(parent.getOid(), oid, com.runwaysdk.system.gis.mapping.HasPrimitiveRuleDTO.CLASS);
  }
  
  public void removeCompositeRule(com.runwaysdk.system.gis.mapping.HasPrimitiveRuleDTO relationship)
  {
    getRequest().deleteParent(relationship.getOid());
  }
  
  public static void removeCompositeRule(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.gis.mapping.HasPrimitiveRuleDTO relationship)
  {
    clientRequestIF.deleteParent(relationship.getOid());
  }
  
  public void removeAllCompositeRule()
  {
    getRequest().deleteParents(this.getOid(), com.runwaysdk.system.gis.mapping.HasPrimitiveRuleDTO.CLASS);
  }
  
  public static void removeAllCompositeRule(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    clientRequestIF.deleteParents(oid, com.runwaysdk.system.gis.mapping.HasPrimitiveRuleDTO.CLASS);
  }
  
  public static com.runwaysdk.system.gis.mapping.PrimitiveRuleDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String oid)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(oid);
    
    return (com.runwaysdk.system.gis.mapping.PrimitiveRuleDTO) dto;
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
  
  public static com.runwaysdk.system.gis.mapping.PrimitiveRuleQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.gis.mapping.PrimitiveRuleQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.gis.mapping.PrimitiveRuleDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.gis.mapping.PrimitiveRuleDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.gis.mapping.PrimitiveRuleDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.gis.mapping.PrimitiveRuleDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.gis.mapping.PrimitiveRuleDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.gis.mapping.PrimitiveRuleDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.gis.mapping.PrimitiveRuleDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
