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

@com.runwaysdk.business.ClassSignature(hash = -1009931566)
public abstract class MdMobileHeaderDTOBase extends com.runwaysdk.system.metadata.MdMobileFieldDTO
{
  public final static String CLASS = "com.runwaysdk.system.metadata.MdMobileHeader";
  private static final long serialVersionUID = -1009931566;
  
  protected MdMobileHeaderDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected MdMobileHeaderDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String HEADERTEXT = "headerText";
  public com.runwaysdk.system.metadata.MdMobileHeaderHeaderTextDTO getHeaderText()
  {
    return (com.runwaysdk.system.metadata.MdMobileHeaderHeaderTextDTO) this.getAttributeStructDTO(HEADERTEXT).getStructDTO();
  }
  
  public boolean isHeaderTextWritable()
  {
    return isWritable(HEADERTEXT);
  }
  
  public boolean isHeaderTextReadable()
  {
    return isReadable(HEADERTEXT);
  }
  
  public boolean isHeaderTextModified()
  {
    return isModified(HEADERTEXT);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeLocalCharacterMdDTO getHeaderTextMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeLocalCharacterMdDTO) getAttributeDTO(HEADERTEXT).getAttributeMdDTO();
  }
  
  public static com.runwaysdk.system.metadata.MdMobileHeaderDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String oid)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(oid);
    
    return (com.runwaysdk.system.metadata.MdMobileHeaderDTO) dto;
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
  
  public static com.runwaysdk.system.metadata.MdMobileHeaderQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.metadata.MdMobileHeaderQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.metadata.MdMobileHeaderDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdMobileHeaderDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdMobileHeaderDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdMobileHeaderDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdMobileHeaderDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdMobileHeaderDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdMobileHeaderDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
