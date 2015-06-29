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

@com.runwaysdk.business.ClassSignature(hash = -48532209)
public abstract class MdFacadeDTOBase extends com.runwaysdk.system.metadata.MdTypeDTO
{
  public final static String CLASS = "com.runwaysdk.system.metadata.MdFacade";
  private static final long serialVersionUID = -48532209;
  
  protected MdFacadeDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected MdFacadeDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String CLIENTCLASSES = "clientClasses";
  public static java.lang.String COMMONCLASSES = "commonClasses";
  public static java.lang.String SERVERCLASSES = "serverClasses";
  public static java.lang.String STUBCLASS = "stubClass";
  public static java.lang.String STUBSOURCE = "stubSource";
  public byte[] getClientClasses()
  {
    return super.getBlob(CLIENTCLASSES);
  }
  
  public boolean isClientClassesWritable()
  {
    return isWritable(CLIENTCLASSES);
  }
  
  public boolean isClientClassesReadable()
  {
    return isReadable(CLIENTCLASSES);
  }
  
  public boolean isClientClassesModified()
  {
    return isModified(CLIENTCLASSES);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeBlobMdDTO getClientClassesMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeBlobMdDTO) getAttributeDTO(CLIENTCLASSES).getAttributeMdDTO();
  }
  
  public byte[] getCommonClasses()
  {
    return super.getBlob(COMMONCLASSES);
  }
  
  public boolean isCommonClassesWritable()
  {
    return isWritable(COMMONCLASSES);
  }
  
  public boolean isCommonClassesReadable()
  {
    return isReadable(COMMONCLASSES);
  }
  
  public boolean isCommonClassesModified()
  {
    return isModified(COMMONCLASSES);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeBlobMdDTO getCommonClassesMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeBlobMdDTO) getAttributeDTO(COMMONCLASSES).getAttributeMdDTO();
  }
  
  public byte[] getServerClasses()
  {
    return super.getBlob(SERVERCLASSES);
  }
  
  public boolean isServerClassesWritable()
  {
    return isWritable(SERVERCLASSES);
  }
  
  public boolean isServerClassesReadable()
  {
    return isReadable(SERVERCLASSES);
  }
  
  public boolean isServerClassesModified()
  {
    return isModified(SERVERCLASSES);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeBlobMdDTO getServerClassesMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeBlobMdDTO) getAttributeDTO(SERVERCLASSES).getAttributeMdDTO();
  }
  
  public byte[] getStubClass()
  {
    return super.getBlob(STUBCLASS);
  }
  
  public boolean isStubClassWritable()
  {
    return isWritable(STUBCLASS);
  }
  
  public boolean isStubClassReadable()
  {
    return isReadable(STUBCLASS);
  }
  
  public boolean isStubClassModified()
  {
    return isModified(STUBCLASS);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeBlobMdDTO getStubClassMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeBlobMdDTO) getAttributeDTO(STUBCLASS).getAttributeMdDTO();
  }
  
  public String getStubSource()
  {
    return getValue(STUBSOURCE);
  }
  
  public void setStubSource(String value)
  {
    if(value == null)
    {
      setValue(STUBSOURCE, "");
    }
    else
    {
      setValue(STUBSOURCE, value);
    }
  }
  
  public boolean isStubSourceWritable()
  {
    return isWritable(STUBSOURCE);
  }
  
  public boolean isStubSourceReadable()
  {
    return isReadable(STUBSOURCE);
  }
  
  public boolean isStubSourceModified()
  {
    return isModified(STUBSOURCE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeClobMdDTO getStubSourceMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeClobMdDTO) getAttributeDTO(STUBSOURCE).getAttributeMdDTO();
  }
  
  public static com.runwaysdk.system.metadata.MdFacadeDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String id)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(id);
    
    return (com.runwaysdk.system.metadata.MdFacadeDTO) dto;
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
  
  public static com.runwaysdk.system.metadata.MdFacadeQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.metadata.MdFacadeQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.metadata.MdFacadeDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdFacadeDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdFacadeDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdFacadeDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdFacadeDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdFacadeDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdFacadeDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
