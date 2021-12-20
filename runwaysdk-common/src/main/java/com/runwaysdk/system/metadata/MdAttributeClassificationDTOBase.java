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

@com.runwaysdk.business.ClassSignature(hash = 396253908)
public abstract class MdAttributeClassificationDTOBase extends com.runwaysdk.system.metadata.MdAttributeConcreteDTO
{
  public final static String CLASS = "com.runwaysdk.system.metadata.MdAttributeClassification";
  private static final long serialVersionUID = 396253908;
  
  protected MdAttributeClassificationDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected MdAttributeClassificationDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String REFERENCEMDCLASSIFICATION = "referenceMdClassification";
  public static java.lang.String ROOT = "root";
  public com.runwaysdk.system.metadata.MdClassificationDTO getReferenceMdClassification()
  {
    if(getValue(REFERENCEMDCLASSIFICATION) == null || getValue(REFERENCEMDCLASSIFICATION).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.metadata.MdClassificationDTO.get(getRequest(), getValue(REFERENCEMDCLASSIFICATION));
    }
  }
  
  public String getReferenceMdClassificationOid()
  {
    return getValue(REFERENCEMDCLASSIFICATION);
  }
  
  public void setReferenceMdClassification(com.runwaysdk.system.metadata.MdClassificationDTO value)
  {
    if(value == null)
    {
      setValue(REFERENCEMDCLASSIFICATION, "");
    }
    else
    {
      setValue(REFERENCEMDCLASSIFICATION, value.getOid());
    }
  }
  
  public boolean isReferenceMdClassificationWritable()
  {
    return isWritable(REFERENCEMDCLASSIFICATION);
  }
  
  public boolean isReferenceMdClassificationReadable()
  {
    return isReadable(REFERENCEMDCLASSIFICATION);
  }
  
  public boolean isReferenceMdClassificationModified()
  {
    return isModified(REFERENCEMDCLASSIFICATION);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getReferenceMdClassificationMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(REFERENCEMDCLASSIFICATION).getAttributeMdDTO();
  }
  
  public static com.runwaysdk.system.metadata.MdAttributeClassificationDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String oid)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(oid);
    
    return (com.runwaysdk.system.metadata.MdAttributeClassificationDTO) dto;
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
  
  public static com.runwaysdk.system.metadata.MdAttributeClassificationQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.metadata.MdAttributeClassificationQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.metadata.MdAttributeClassificationDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdAttributeClassificationDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdAttributeClassificationDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdAttributeClassificationDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdAttributeClassificationDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdAttributeClassificationDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdAttributeClassificationDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
