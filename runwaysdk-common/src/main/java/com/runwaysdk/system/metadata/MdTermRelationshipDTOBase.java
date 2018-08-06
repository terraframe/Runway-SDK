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

@com.runwaysdk.business.ClassSignature(hash = -920084916)
public abstract class MdTermRelationshipDTOBase extends com.runwaysdk.system.metadata.MdRelationshipDTO
{
  public final static String CLASS = "com.runwaysdk.system.metadata.MdTermRelationship";
  private static final long serialVersionUID = -920084916;
  
  protected MdTermRelationshipDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected MdTermRelationshipDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String ASSOCIATIONTYPE = "associationType";
  @SuppressWarnings("unchecked")
  public java.util.List<com.runwaysdk.system.metadata.AssociationTypeDTO> getAssociationType()
  {
    return (java.util.List<com.runwaysdk.system.metadata.AssociationTypeDTO>) com.runwaysdk.transport.conversion.ConversionFacade.convertEnumDTOsFromEnumNames(getRequest(), com.runwaysdk.system.metadata.AssociationTypeDTO.CLASS, getEnumNames(ASSOCIATIONTYPE));
  }
  
  public java.util.List<String> getAssociationTypeEnumNames()
  {
    return getEnumNames(ASSOCIATIONTYPE);
  }
  
  public void addAssociationType(com.runwaysdk.system.metadata.AssociationTypeDTO enumDTO)
  {
    addEnumItem(ASSOCIATIONTYPE, enumDTO.toString());
  }
  
  public void removeAssociationType(com.runwaysdk.system.metadata.AssociationTypeDTO enumDTO)
  {
    removeEnumItem(ASSOCIATIONTYPE, enumDTO.toString());
  }
  
  public void clearAssociationType()
  {
    clearEnum(ASSOCIATIONTYPE);
  }
  
  public boolean isAssociationTypeWritable()
  {
    return isWritable(ASSOCIATIONTYPE);
  }
  
  public boolean isAssociationTypeReadable()
  {
    return isReadable(ASSOCIATIONTYPE);
  }
  
  public boolean isAssociationTypeModified()
  {
    return isModified(ASSOCIATIONTYPE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeEnumerationMdDTO getAssociationTypeMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeEnumerationMdDTO) getAttributeDTO(ASSOCIATIONTYPE).getAttributeMdDTO();
  }
  
  public static com.runwaysdk.system.metadata.MdTermRelationshipDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String oid)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(oid);
    
    return (com.runwaysdk.system.metadata.MdTermRelationshipDTO) dto;
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
  
  public static com.runwaysdk.system.metadata.MdTermRelationshipQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.metadata.MdTermRelationshipQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.metadata.MdTermRelationshipDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdTermRelationshipDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdTermRelationshipDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdTermRelationshipDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdTermRelationshipDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdTermRelationshipDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdTermRelationshipDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
