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

@com.runwaysdk.business.ClassSignature(hash = 920062879)
public abstract class MdMobileFieldDTOBase extends com.runwaysdk.system.metadata.MdFieldDTO
{
  public final static String CLASS = "com.runwaysdk.system.metadata.MdMobileField";
  private static final long serialVersionUID = 920062879;
  
  protected MdMobileFieldDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected MdMobileFieldDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public final static java.lang.String DEFININGMDFORM = "definingMdForm";
  public com.runwaysdk.system.metadata.MdMobileFormDTO getDefiningMdForm()
  {
    if(getValue(DEFININGMDFORM) == null || getValue(DEFININGMDFORM).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.metadata.MdMobileFormDTO.get(getRequest(), getValue(DEFININGMDFORM));
    }
  }
  
  public String getDefiningMdFormId()
  {
    return getValue(DEFININGMDFORM);
  }
  
  public void setDefiningMdForm(com.runwaysdk.system.metadata.MdMobileFormDTO value)
  {
    if(value == null)
    {
      setValue(DEFININGMDFORM, "");
    }
    else
    {
      setValue(DEFININGMDFORM, value.getOid());
    }
  }
  
  public boolean isDefiningMdFormWritable()
  {
    return isWritable(DEFININGMDFORM);
  }
  
  public boolean isDefiningMdFormReadable()
  {
    return isReadable(DEFININGMDFORM);
  }
  
  public boolean isDefiningMdFormModified()
  {
    return isModified(DEFININGMDFORM);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getDefiningMdFormMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(DEFININGMDFORM).getAttributeMdDTO();
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.MdMobileGroupDTO> getAllGroupFields()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdMobileGroupDTO>) getRequest().getParents(this.getOid(), com.runwaysdk.system.metadata.MobileGroupFieldDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.MdMobileGroupDTO> getAllGroupFields(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdMobileGroupDTO>) clientRequestIF.getParents(oid, com.runwaysdk.system.metadata.MobileGroupFieldDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.MobileGroupFieldDTO> getAllGroupFieldsRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MobileGroupFieldDTO>) getRequest().getParentRelationships(this.getOid(), com.runwaysdk.system.metadata.MobileGroupFieldDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.MobileGroupFieldDTO> getAllGroupFieldsRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MobileGroupFieldDTO>) clientRequestIF.getParentRelationships(oid, com.runwaysdk.system.metadata.MobileGroupFieldDTO.CLASS);
  }
  
  public com.runwaysdk.system.metadata.MobileGroupFieldDTO addGroupFields(com.runwaysdk.system.metadata.MdMobileGroupDTO parent)
  {
    return (com.runwaysdk.system.metadata.MobileGroupFieldDTO) getRequest().addParent(parent.getOid(), this.getOid(), com.runwaysdk.system.metadata.MobileGroupFieldDTO.CLASS);
  }
  
  public static com.runwaysdk.system.metadata.MobileGroupFieldDTO addGroupFields(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid, com.runwaysdk.system.metadata.MdMobileGroupDTO parent)
  {
    return (com.runwaysdk.system.metadata.MobileGroupFieldDTO) clientRequestIF.addParent(parent.getOid(), oid, com.runwaysdk.system.metadata.MobileGroupFieldDTO.CLASS);
  }
  
  public void removeGroupFields(com.runwaysdk.system.metadata.MobileGroupFieldDTO relationship)
  {
    getRequest().deleteParent(relationship.getOid());
  }
  
  public static void removeGroupFields(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.metadata.MobileGroupFieldDTO relationship)
  {
    clientRequestIF.deleteParent(relationship.getOid());
  }
  
  public void removeAllGroupFields()
  {
    getRequest().deleteParents(this.getOid(), com.runwaysdk.system.metadata.MobileGroupFieldDTO.CLASS);
  }
  
  public static void removeAllGroupFields(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    clientRequestIF.deleteParents(oid, com.runwaysdk.system.metadata.MobileGroupFieldDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.MdMobileFormDTO> getAllMdForm()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdMobileFormDTO>) getRequest().getParents(this.getOid(), com.runwaysdk.system.metadata.MobileFormFieldDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.MdMobileFormDTO> getAllMdForm(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdMobileFormDTO>) clientRequestIF.getParents(oid, com.runwaysdk.system.metadata.MobileFormFieldDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.MobileFormFieldDTO> getAllMdFormRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MobileFormFieldDTO>) getRequest().getParentRelationships(this.getOid(), com.runwaysdk.system.metadata.MobileFormFieldDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.MobileFormFieldDTO> getAllMdFormRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MobileFormFieldDTO>) clientRequestIF.getParentRelationships(oid, com.runwaysdk.system.metadata.MobileFormFieldDTO.CLASS);
  }
  
  public com.runwaysdk.system.metadata.MobileFormFieldDTO addMdForm(com.runwaysdk.system.metadata.MdMobileFormDTO parent)
  {
    return (com.runwaysdk.system.metadata.MobileFormFieldDTO) getRequest().addParent(parent.getOid(), this.getOid(), com.runwaysdk.system.metadata.MobileFormFieldDTO.CLASS);
  }
  
  public static com.runwaysdk.system.metadata.MobileFormFieldDTO addMdForm(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid, com.runwaysdk.system.metadata.MdMobileFormDTO parent)
  {
    return (com.runwaysdk.system.metadata.MobileFormFieldDTO) clientRequestIF.addParent(parent.getOid(), oid, com.runwaysdk.system.metadata.MobileFormFieldDTO.CLASS);
  }
  
  public void removeMdForm(com.runwaysdk.system.metadata.MobileFormFieldDTO relationship)
  {
    getRequest().deleteParent(relationship.getOid());
  }
  
  public static void removeMdForm(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.metadata.MobileFormFieldDTO relationship)
  {
    clientRequestIF.deleteParent(relationship.getOid());
  }
  
  public void removeAllMdForm()
  {
    getRequest().deleteParents(this.getOid(), com.runwaysdk.system.metadata.MobileFormFieldDTO.CLASS);
  }
  
  public static void removeAllMdForm(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    clientRequestIF.deleteParents(oid, com.runwaysdk.system.metadata.MobileFormFieldDTO.CLASS);
  }
  
  public static com.runwaysdk.system.metadata.MdMobileFieldDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String oid)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(oid);
    
    return (com.runwaysdk.system.metadata.MdMobileFieldDTO) dto;
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
  
  public static com.runwaysdk.system.metadata.MdMobileFieldQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.metadata.MdMobileFieldQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.metadata.MdMobileFieldDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdMobileFieldDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdMobileFieldDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdMobileFieldDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdMobileFieldDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdMobileFieldDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdMobileFieldDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
