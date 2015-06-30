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

@com.runwaysdk.business.ClassSignature(hash = 829651447)
public abstract class MdWebAttributeDTOBase extends com.runwaysdk.system.metadata.MdWebFieldDTO
{
  public final static String CLASS = "com.runwaysdk.system.metadata.MdWebAttribute";
  private static final long serialVersionUID = 829651447;
  
  protected MdWebAttributeDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected MdWebAttributeDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String DEFININGMDATTRIBUTE = "definingMdAttribute";
  public static java.lang.String SHOWONSEARCH = "showOnSearch";
  public static java.lang.String SHOWONVIEWALL = "showOnViewAll";
  public com.runwaysdk.system.metadata.MdAttributeDTO getDefiningMdAttribute()
  {
    if(getValue(DEFININGMDATTRIBUTE) == null || getValue(DEFININGMDATTRIBUTE).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.metadata.MdAttributeDTO.get(getRequest(), getValue(DEFININGMDATTRIBUTE));
    }
  }
  
  public String getDefiningMdAttributeId()
  {
    return getValue(DEFININGMDATTRIBUTE);
  }
  
  public void setDefiningMdAttribute(com.runwaysdk.system.metadata.MdAttributeDTO value)
  {
    if(value == null)
    {
      setValue(DEFININGMDATTRIBUTE, "");
    }
    else
    {
      setValue(DEFININGMDATTRIBUTE, value.getId());
    }
  }
  
  public boolean isDefiningMdAttributeWritable()
  {
    return isWritable(DEFININGMDATTRIBUTE);
  }
  
  public boolean isDefiningMdAttributeReadable()
  {
    return isReadable(DEFININGMDATTRIBUTE);
  }
  
  public boolean isDefiningMdAttributeModified()
  {
    return isModified(DEFININGMDATTRIBUTE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getDefiningMdAttributeMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(DEFININGMDATTRIBUTE).getAttributeMdDTO();
  }
  
  public Boolean getShowOnSearch()
  {
    return com.runwaysdk.constants.MdAttributeBooleanUtil.getTypeSafeValue(getValue(SHOWONSEARCH));
  }
  
  public void setShowOnSearch(Boolean value)
  {
    if(value == null)
    {
      setValue(SHOWONSEARCH, "");
    }
    else
    {
      setValue(SHOWONSEARCH, java.lang.Boolean.toString(value));
    }
  }
  
  public boolean isShowOnSearchWritable()
  {
    return isWritable(SHOWONSEARCH);
  }
  
  public boolean isShowOnSearchReadable()
  {
    return isReadable(SHOWONSEARCH);
  }
  
  public boolean isShowOnSearchModified()
  {
    return isModified(SHOWONSEARCH);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeBooleanMdDTO getShowOnSearchMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeBooleanMdDTO) getAttributeDTO(SHOWONSEARCH).getAttributeMdDTO();
  }
  
  public Boolean getShowOnViewAll()
  {
    return com.runwaysdk.constants.MdAttributeBooleanUtil.getTypeSafeValue(getValue(SHOWONVIEWALL));
  }
  
  public void setShowOnViewAll(Boolean value)
  {
    if(value == null)
    {
      setValue(SHOWONVIEWALL, "");
    }
    else
    {
      setValue(SHOWONVIEWALL, java.lang.Boolean.toString(value));
    }
  }
  
  public boolean isShowOnViewAllWritable()
  {
    return isWritable(SHOWONVIEWALL);
  }
  
  public boolean isShowOnViewAllReadable()
  {
    return isReadable(SHOWONVIEWALL);
  }
  
  public boolean isShowOnViewAllModified()
  {
    return isModified(SHOWONVIEWALL);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeBooleanMdDTO getShowOnViewAllMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeBooleanMdDTO) getAttributeDTO(SHOWONVIEWALL).getAttributeMdDTO();
  }
  
  public static com.runwaysdk.system.metadata.MdWebAttributeDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String id)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(id);
    
    return (com.runwaysdk.system.metadata.MdWebAttributeDTO) dto;
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
  
  public static com.runwaysdk.system.metadata.MdWebAttributeQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.metadata.MdWebAttributeQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.metadata.MdWebAttributeDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdWebAttributeDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdWebAttributeDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdWebAttributeDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdWebAttributeDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdWebAttributeDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdWebAttributeDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
