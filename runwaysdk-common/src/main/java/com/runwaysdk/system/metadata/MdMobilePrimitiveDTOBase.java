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

@com.runwaysdk.business.ClassSignature(hash = -790392146)
public abstract class MdMobilePrimitiveDTOBase extends com.runwaysdk.system.metadata.MdMobileAttributeDTO
{
  public final static String CLASS = "com.runwaysdk.system.metadata.MdMobilePrimitive";
  private static final long serialVersionUID = -790392146;
  
  protected MdMobilePrimitiveDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected MdMobilePrimitiveDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.MdMobileSingleTermGridDTO> getAllGrid()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdMobileSingleTermGridDTO>) getRequest().getParents(this.getOid(), com.runwaysdk.system.metadata.MobileGridFieldDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.MdMobileSingleTermGridDTO> getAllGrid(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdMobileSingleTermGridDTO>) clientRequestIF.getParents(oid, com.runwaysdk.system.metadata.MobileGridFieldDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.MobileGridFieldDTO> getAllGridRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MobileGridFieldDTO>) getRequest().getParentRelationships(this.getOid(), com.runwaysdk.system.metadata.MobileGridFieldDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.MobileGridFieldDTO> getAllGridRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MobileGridFieldDTO>) clientRequestIF.getParentRelationships(oid, com.runwaysdk.system.metadata.MobileGridFieldDTO.CLASS);
  }
  
  public com.runwaysdk.system.metadata.MobileGridFieldDTO addGrid(com.runwaysdk.system.metadata.MdMobileSingleTermGridDTO parent)
  {
    return (com.runwaysdk.system.metadata.MobileGridFieldDTO) getRequest().addParent(parent.getOid(), this.getOid(), com.runwaysdk.system.metadata.MobileGridFieldDTO.CLASS);
  }
  
  public static com.runwaysdk.system.metadata.MobileGridFieldDTO addGrid(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid, com.runwaysdk.system.metadata.MdMobileSingleTermGridDTO parent)
  {
    return (com.runwaysdk.system.metadata.MobileGridFieldDTO) clientRequestIF.addParent(parent.getOid(), oid, com.runwaysdk.system.metadata.MobileGridFieldDTO.CLASS);
  }
  
  public void removeGrid(com.runwaysdk.system.metadata.MobileGridFieldDTO relationship)
  {
    getRequest().deleteParent(relationship.getOid());
  }
  
  public static void removeGrid(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.metadata.MobileGridFieldDTO relationship)
  {
    clientRequestIF.deleteParent(relationship.getOid());
  }
  
  public void removeAllGrid()
  {
    getRequest().deleteParents(this.getOid(), com.runwaysdk.system.metadata.MobileGridFieldDTO.CLASS);
  }
  
  public static void removeAllGrid(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    clientRequestIF.deleteParents(oid, com.runwaysdk.system.metadata.MobileGridFieldDTO.CLASS);
  }
  
  public static com.runwaysdk.system.metadata.MdMobilePrimitiveDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String oid)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(oid);
    
    return (com.runwaysdk.system.metadata.MdMobilePrimitiveDTO) dto;
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
  
  public static com.runwaysdk.system.metadata.MdMobilePrimitiveQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.metadata.MdMobilePrimitiveQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.metadata.MdMobilePrimitiveDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdMobilePrimitiveDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdMobilePrimitiveDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdMobilePrimitiveDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdMobilePrimitiveDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdMobilePrimitiveDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdMobilePrimitiveDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
