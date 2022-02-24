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

@com.runwaysdk.business.ClassSignature(hash = -737203208)
public abstract class MdElementDTOBase extends com.runwaysdk.system.metadata.MdEntityDTO
{
  public final static String CLASS = "com.runwaysdk.system.metadata.MdElement";
  private static final long serialVersionUID = -737203208;
  
  protected MdElementDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected MdElementDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String EXTENDABLE = "extendable";
  public static java.lang.String ISABSTRACT = "isAbstract";
  public Boolean getExtendable()
  {
    return com.runwaysdk.constants.MdAttributeBooleanUtil.getTypeSafeValue(getValue(EXTENDABLE));
  }
  
  public void setExtendable(Boolean value)
  {
    if(value == null)
    {
      setValue(EXTENDABLE, "");
    }
    else
    {
      setValue(EXTENDABLE, java.lang.Boolean.toString(value));
    }
  }
  
  public boolean isExtendableWritable()
  {
    return isWritable(EXTENDABLE);
  }
  
  public boolean isExtendableReadable()
  {
    return isReadable(EXTENDABLE);
  }
  
  public boolean isExtendableModified()
  {
    return isModified(EXTENDABLE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeBooleanMdDTO getExtendableMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeBooleanMdDTO) getAttributeDTO(EXTENDABLE).getAttributeMdDTO();
  }
  
  public Boolean getIsAbstract()
  {
    return com.runwaysdk.constants.MdAttributeBooleanUtil.getTypeSafeValue(getValue(ISABSTRACT));
  }
  
  public void setIsAbstract(Boolean value)
  {
    if(value == null)
    {
      setValue(ISABSTRACT, "");
    }
    else
    {
      setValue(ISABSTRACT, java.lang.Boolean.toString(value));
    }
  }
  
  public boolean isIsAbstractWritable()
  {
    return isWritable(ISABSTRACT);
  }
  
  public boolean isIsAbstractReadable()
  {
    return isReadable(ISABSTRACT);
  }
  
  public boolean isIsAbstractModified()
  {
    return isModified(ISABSTRACT);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeBooleanMdDTO getIsAbstractMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeBooleanMdDTO) getAttributeDTO(ISABSTRACT).getAttributeMdDTO();
  }
  
  public static com.runwaysdk.system.metadata.MdElementDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String oid)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(oid);
    
    return (com.runwaysdk.system.metadata.MdElementDTO) dto;
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
  
  public static com.runwaysdk.system.metadata.MdElementQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.metadata.MdElementQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.metadata.MdElementDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdElementDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdElementDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdElementDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdElementDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdElementDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdElementDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
