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

@com.runwaysdk.business.ClassSignature(hash = 94931301)
public abstract class MdWebFieldDTOBase extends com.runwaysdk.system.metadata.MdFieldDTO
{
  public final static String CLASS = "com.runwaysdk.system.metadata.MdWebField";
  private static final long serialVersionUID = 94931301;
  
  protected MdWebFieldDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected MdWebFieldDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String DEFININGMDFORM = "definingMdForm";
  public com.runwaysdk.system.metadata.MdWebFormDTO getDefiningMdForm()
  {
    if(getValue(DEFININGMDFORM) == null || getValue(DEFININGMDFORM).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.metadata.MdWebFormDTO.get(getRequest(), getValue(DEFININGMDFORM));
    }
  }
  
  public String getDefiningMdFormId()
  {
    return getValue(DEFININGMDFORM);
  }
  
  public void setDefiningMdForm(com.runwaysdk.system.metadata.MdWebFormDTO value)
  {
    if(value == null)
    {
      setValue(DEFININGMDFORM, "");
    }
    else
    {
      setValue(DEFININGMDFORM, value.getId());
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
  public java.util.List<? extends com.runwaysdk.system.metadata.MdWebGroupDTO> getAllGroupFields()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdWebGroupDTO>) getRequest().getParents(this.getId(), com.runwaysdk.system.metadata.WebGroupFieldDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.MdWebGroupDTO> getAllGroupFields(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdWebGroupDTO>) clientRequestIF.getParents(id, com.runwaysdk.system.metadata.WebGroupFieldDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.WebGroupFieldDTO> getAllGroupFieldsRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.WebGroupFieldDTO>) getRequest().getParentRelationships(this.getId(), com.runwaysdk.system.metadata.WebGroupFieldDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.WebGroupFieldDTO> getAllGroupFieldsRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.WebGroupFieldDTO>) clientRequestIF.getParentRelationships(id, com.runwaysdk.system.metadata.WebGroupFieldDTO.CLASS);
  }
  
  public com.runwaysdk.system.metadata.WebGroupFieldDTO addGroupFields(com.runwaysdk.system.metadata.MdWebGroupDTO parent)
  {
    return (com.runwaysdk.system.metadata.WebGroupFieldDTO) getRequest().addParent(parent.getId(), this.getId(), com.runwaysdk.system.metadata.WebGroupFieldDTO.CLASS);
  }
  
  public static com.runwaysdk.system.metadata.WebGroupFieldDTO addGroupFields(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id, com.runwaysdk.system.metadata.MdWebGroupDTO parent)
  {
    return (com.runwaysdk.system.metadata.WebGroupFieldDTO) clientRequestIF.addParent(parent.getId(), id, com.runwaysdk.system.metadata.WebGroupFieldDTO.CLASS);
  }
  
  public void removeGroupFields(com.runwaysdk.system.metadata.WebGroupFieldDTO relationship)
  {
    getRequest().deleteParent(relationship.getId());
  }
  
  public static void removeGroupFields(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.metadata.WebGroupFieldDTO relationship)
  {
    clientRequestIF.deleteParent(relationship.getId());
  }
  
  public void removeAllGroupFields()
  {
    getRequest().deleteParents(this.getId(), com.runwaysdk.system.metadata.WebGroupFieldDTO.CLASS);
  }
  
  public static void removeAllGroupFields(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    clientRequestIF.deleteParents(id, com.runwaysdk.system.metadata.WebGroupFieldDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.MdWebFormDTO> getAllMdForm()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdWebFormDTO>) getRequest().getParents(this.getId(), com.runwaysdk.system.metadata.WebFormFieldDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.MdWebFormDTO> getAllMdForm(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdWebFormDTO>) clientRequestIF.getParents(id, com.runwaysdk.system.metadata.WebFormFieldDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.WebFormFieldDTO> getAllMdFormRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.WebFormFieldDTO>) getRequest().getParentRelationships(this.getId(), com.runwaysdk.system.metadata.WebFormFieldDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.WebFormFieldDTO> getAllMdFormRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.WebFormFieldDTO>) clientRequestIF.getParentRelationships(id, com.runwaysdk.system.metadata.WebFormFieldDTO.CLASS);
  }
  
  public com.runwaysdk.system.metadata.WebFormFieldDTO addMdForm(com.runwaysdk.system.metadata.MdWebFormDTO parent)
  {
    return (com.runwaysdk.system.metadata.WebFormFieldDTO) getRequest().addParent(parent.getId(), this.getId(), com.runwaysdk.system.metadata.WebFormFieldDTO.CLASS);
  }
  
  public static com.runwaysdk.system.metadata.WebFormFieldDTO addMdForm(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id, com.runwaysdk.system.metadata.MdWebFormDTO parent)
  {
    return (com.runwaysdk.system.metadata.WebFormFieldDTO) clientRequestIF.addParent(parent.getId(), id, com.runwaysdk.system.metadata.WebFormFieldDTO.CLASS);
  }
  
  public void removeMdForm(com.runwaysdk.system.metadata.WebFormFieldDTO relationship)
  {
    getRequest().deleteParent(relationship.getId());
  }
  
  public static void removeMdForm(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.metadata.WebFormFieldDTO relationship)
  {
    clientRequestIF.deleteParent(relationship.getId());
  }
  
  public void removeAllMdForm()
  {
    getRequest().deleteParents(this.getId(), com.runwaysdk.system.metadata.WebFormFieldDTO.CLASS);
  }
  
  public static void removeAllMdForm(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    clientRequestIF.deleteParents(id, com.runwaysdk.system.metadata.WebFormFieldDTO.CLASS);
  }
  
  public static com.runwaysdk.system.metadata.MdWebFieldDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String id)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(id);
    
    return (com.runwaysdk.system.metadata.MdWebFieldDTO) dto;
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
  
  public static com.runwaysdk.system.metadata.MdWebFieldQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.metadata.MdWebFieldQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.metadata.MdWebFieldDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdWebFieldDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdWebFieldDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdWebFieldDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdWebFieldDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdWebFieldDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdWebFieldDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
