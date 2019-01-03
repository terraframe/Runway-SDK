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

@com.runwaysdk.business.ClassSignature(hash = -895046968)
public abstract class MdMobileSingleTermGridDTOBase extends com.runwaysdk.system.metadata.MdMobileAttributeDTO
{
  public final static String CLASS = "com.runwaysdk.system.metadata.MdMobileSingleTermGrid";
  private static final long serialVersionUID = -895046968;
  
  protected MdMobileSingleTermGridDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected MdMobileSingleTermGridDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.MdMobilePrimitiveDTO> getAllMdFields()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdMobilePrimitiveDTO>) getRequest().getChildren(this.getOid(), com.runwaysdk.system.metadata.MobileGridFieldDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.MdMobilePrimitiveDTO> getAllMdFields(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdMobilePrimitiveDTO>) clientRequestIF.getChildren(oid, com.runwaysdk.system.metadata.MobileGridFieldDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.MobileGridFieldDTO> getAllMdFieldsRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MobileGridFieldDTO>) getRequest().getChildRelationships(this.getOid(), com.runwaysdk.system.metadata.MobileGridFieldDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.MobileGridFieldDTO> getAllMdFieldsRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MobileGridFieldDTO>) clientRequestIF.getChildRelationships(oid, com.runwaysdk.system.metadata.MobileGridFieldDTO.CLASS);
  }
  
  public com.runwaysdk.system.metadata.MobileGridFieldDTO addMdFields(com.runwaysdk.system.metadata.MdMobilePrimitiveDTO child)
  {
    return (com.runwaysdk.system.metadata.MobileGridFieldDTO) getRequest().addChild(this.getOid(), child.getOid(), com.runwaysdk.system.metadata.MobileGridFieldDTO.CLASS);
  }
  
  public static com.runwaysdk.system.metadata.MobileGridFieldDTO addMdFields(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid, com.runwaysdk.system.metadata.MdMobilePrimitiveDTO child)
  {
    return (com.runwaysdk.system.metadata.MobileGridFieldDTO) clientRequestIF.addChild(oid, child.getOid(), com.runwaysdk.system.metadata.MobileGridFieldDTO.CLASS);
  }
  
  public void removeMdFields(com.runwaysdk.system.metadata.MobileGridFieldDTO relationship)
  {
    getRequest().deleteChild(relationship.getOid());
  }
  
  public static void removeMdFields(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.metadata.MobileGridFieldDTO relationship)
  {
    clientRequestIF.deleteChild(relationship.getOid());
  }
  
  public void removeAllMdFields()
  {
    getRequest().deleteChildren(this.getOid(), com.runwaysdk.system.metadata.MobileGridFieldDTO.CLASS);
  }
  
  public static void removeAllMdFields(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    clientRequestIF.deleteChildren(oid, com.runwaysdk.system.metadata.MobileGridFieldDTO.CLASS);
  }
  
  public static com.runwaysdk.system.metadata.MdMobileSingleTermGridDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String oid)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(oid);
    
    return (com.runwaysdk.system.metadata.MdMobileSingleTermGridDTO) dto;
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
  
  public static com.runwaysdk.system.metadata.MdMobileSingleTermGridQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.metadata.MdMobileSingleTermGridQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.metadata.MdMobileSingleTermGridDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdMobileSingleTermGridDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdMobileSingleTermGridDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdMobileSingleTermGridDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdMobileSingleTermGridDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdMobileSingleTermGridDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdMobileSingleTermGridDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
