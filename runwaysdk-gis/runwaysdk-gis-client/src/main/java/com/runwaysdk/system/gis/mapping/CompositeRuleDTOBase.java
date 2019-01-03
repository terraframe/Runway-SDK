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

@com.runwaysdk.business.ClassSignature(hash = 757391526)
public abstract class CompositeRuleDTOBase extends com.runwaysdk.system.gis.mapping.ThematicRuleDTO
{
  public final static String CLASS = "com.runwaysdk.system.gis.mapping.CompositeRule";
  private static final long serialVersionUID = 757391526;
  
  protected CompositeRuleDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected CompositeRuleDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.gis.mapping.PrimitiveRuleDTO> getAllPrimitiveRule()
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.mapping.PrimitiveRuleDTO>) getRequest().getChildren(this.getOid(), com.runwaysdk.system.gis.mapping.HasPrimitiveRuleDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.gis.mapping.PrimitiveRuleDTO> getAllPrimitiveRule(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.mapping.PrimitiveRuleDTO>) clientRequestIF.getChildren(oid, com.runwaysdk.system.gis.mapping.HasPrimitiveRuleDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.gis.mapping.HasPrimitiveRuleDTO> getAllPrimitiveRuleRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.mapping.HasPrimitiveRuleDTO>) getRequest().getChildRelationships(this.getOid(), com.runwaysdk.system.gis.mapping.HasPrimitiveRuleDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.gis.mapping.HasPrimitiveRuleDTO> getAllPrimitiveRuleRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.gis.mapping.HasPrimitiveRuleDTO>) clientRequestIF.getChildRelationships(oid, com.runwaysdk.system.gis.mapping.HasPrimitiveRuleDTO.CLASS);
  }
  
  public com.runwaysdk.system.gis.mapping.HasPrimitiveRuleDTO addPrimitiveRule(com.runwaysdk.system.gis.mapping.PrimitiveRuleDTO child)
  {
    return (com.runwaysdk.system.gis.mapping.HasPrimitiveRuleDTO) getRequest().addChild(this.getOid(), child.getOid(), com.runwaysdk.system.gis.mapping.HasPrimitiveRuleDTO.CLASS);
  }
  
  public static com.runwaysdk.system.gis.mapping.HasPrimitiveRuleDTO addPrimitiveRule(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid, com.runwaysdk.system.gis.mapping.PrimitiveRuleDTO child)
  {
    return (com.runwaysdk.system.gis.mapping.HasPrimitiveRuleDTO) clientRequestIF.addChild(oid, child.getOid(), com.runwaysdk.system.gis.mapping.HasPrimitiveRuleDTO.CLASS);
  }
  
  public void removePrimitiveRule(com.runwaysdk.system.gis.mapping.HasPrimitiveRuleDTO relationship)
  {
    getRequest().deleteChild(relationship.getOid());
  }
  
  public static void removePrimitiveRule(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.gis.mapping.HasPrimitiveRuleDTO relationship)
  {
    clientRequestIF.deleteChild(relationship.getOid());
  }
  
  public void removeAllPrimitiveRule()
  {
    getRequest().deleteChildren(this.getOid(), com.runwaysdk.system.gis.mapping.HasPrimitiveRuleDTO.CLASS);
  }
  
  public static void removeAllPrimitiveRule(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    clientRequestIF.deleteChildren(oid, com.runwaysdk.system.gis.mapping.HasPrimitiveRuleDTO.CLASS);
  }
  
  public static com.runwaysdk.system.gis.mapping.CompositeRuleDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String oid)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(oid);
    
    return (com.runwaysdk.system.gis.mapping.CompositeRuleDTO) dto;
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
  
  public static com.runwaysdk.system.gis.mapping.CompositeRuleQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.gis.mapping.CompositeRuleQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.gis.mapping.CompositeRuleDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.gis.mapping.CompositeRuleDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.gis.mapping.CompositeRuleDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.gis.mapping.CompositeRuleDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.gis.mapping.CompositeRuleDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.gis.mapping.CompositeRuleDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.gis.mapping.CompositeRuleDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
