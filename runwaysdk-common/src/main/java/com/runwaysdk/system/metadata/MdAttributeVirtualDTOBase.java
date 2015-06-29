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

@com.runwaysdk.business.ClassSignature(hash = 1992912852)
public abstract class MdAttributeVirtualDTOBase extends com.runwaysdk.system.metadata.MdAttributeDTO
{
  public final static String CLASS = "com.runwaysdk.system.metadata.MdAttributeVirtual";
  private static final long serialVersionUID = 1992912852;
  
  protected MdAttributeVirtualDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected MdAttributeVirtualDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String ATTRIBUTENAME = "attributeName";
  public static java.lang.String DEFININGMDVIEW = "definingMdView";
  public static java.lang.String DISPLAYLABEL = "displayLabel";
  public static java.lang.String MDATTRIBUTECONCRETE = "mdAttributeConcrete";
  public static java.lang.String REQUIRED = "required";
  public String getAttributeName()
  {
    return getValue(ATTRIBUTENAME);
  }
  
  public void setAttributeName(String value)
  {
    if(value == null)
    {
      setValue(ATTRIBUTENAME, "");
    }
    else
    {
      setValue(ATTRIBUTENAME, value);
    }
  }
  
  public boolean isAttributeNameWritable()
  {
    return isWritable(ATTRIBUTENAME);
  }
  
  public boolean isAttributeNameReadable()
  {
    return isReadable(ATTRIBUTENAME);
  }
  
  public boolean isAttributeNameModified()
  {
    return isModified(ATTRIBUTENAME);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getAttributeNameMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(ATTRIBUTENAME).getAttributeMdDTO();
  }
  
  public com.runwaysdk.system.metadata.MdViewDTO getDefiningMdView()
  {
    if(getValue(DEFININGMDVIEW) == null || getValue(DEFININGMDVIEW).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.metadata.MdViewDTO.get(getRequest(), getValue(DEFININGMDVIEW));
    }
  }
  
  public String getDefiningMdViewId()
  {
    return getValue(DEFININGMDVIEW);
  }
  
  public void setDefiningMdView(com.runwaysdk.system.metadata.MdViewDTO value)
  {
    if(value == null)
    {
      setValue(DEFININGMDVIEW, "");
    }
    else
    {
      setValue(DEFININGMDVIEW, value.getId());
    }
  }
  
  public boolean isDefiningMdViewWritable()
  {
    return isWritable(DEFININGMDVIEW);
  }
  
  public boolean isDefiningMdViewReadable()
  {
    return isReadable(DEFININGMDVIEW);
  }
  
  public boolean isDefiningMdViewModified()
  {
    return isModified(DEFININGMDVIEW);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getDefiningMdViewMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(DEFININGMDVIEW).getAttributeMdDTO();
  }
  
  public com.runwaysdk.system.metadata.MetadataDisplayLabelDTO getDisplayLabel()
  {
    return (com.runwaysdk.system.metadata.MetadataDisplayLabelDTO) this.getAttributeStructDTO(DISPLAYLABEL).getStructDTO();
  }
  
  public boolean isDisplayLabelWritable()
  {
    return isWritable(DISPLAYLABEL);
  }
  
  public boolean isDisplayLabelReadable()
  {
    return isReadable(DISPLAYLABEL);
  }
  
  public boolean isDisplayLabelModified()
  {
    return isModified(DISPLAYLABEL);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeLocalCharacterMdDTO getDisplayLabelMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeLocalCharacterMdDTO) getAttributeDTO(DISPLAYLABEL).getAttributeMdDTO();
  }
  
  public com.runwaysdk.system.metadata.MdAttributeConcreteDTO getMdAttributeConcrete()
  {
    if(getValue(MDATTRIBUTECONCRETE) == null || getValue(MDATTRIBUTECONCRETE).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.metadata.MdAttributeConcreteDTO.get(getRequest(), getValue(MDATTRIBUTECONCRETE));
    }
  }
  
  public String getMdAttributeConcreteId()
  {
    return getValue(MDATTRIBUTECONCRETE);
  }
  
  public void setMdAttributeConcrete(com.runwaysdk.system.metadata.MdAttributeConcreteDTO value)
  {
    if(value == null)
    {
      setValue(MDATTRIBUTECONCRETE, "");
    }
    else
    {
      setValue(MDATTRIBUTECONCRETE, value.getId());
    }
  }
  
  public boolean isMdAttributeConcreteWritable()
  {
    return isWritable(MDATTRIBUTECONCRETE);
  }
  
  public boolean isMdAttributeConcreteReadable()
  {
    return isReadable(MDATTRIBUTECONCRETE);
  }
  
  public boolean isMdAttributeConcreteModified()
  {
    return isModified(MDATTRIBUTECONCRETE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getMdAttributeConcreteMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(MDATTRIBUTECONCRETE).getAttributeMdDTO();
  }
  
  public Boolean getRequired()
  {
    return com.runwaysdk.constants.MdAttributeBooleanUtil.getTypeSafeValue(getValue(REQUIRED));
  }
  
  public void setRequired(Boolean value)
  {
    if(value == null)
    {
      setValue(REQUIRED, "");
    }
    else
    {
      setValue(REQUIRED, java.lang.Boolean.toString(value));
    }
  }
  
  public boolean isRequiredWritable()
  {
    return isWritable(REQUIRED);
  }
  
  public boolean isRequiredReadable()
  {
    return isReadable(REQUIRED);
  }
  
  public boolean isRequiredModified()
  {
    return isModified(REQUIRED);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeBooleanMdDTO getRequiredMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeBooleanMdDTO) getAttributeDTO(REQUIRED).getAttributeMdDTO();
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.MdAttributeConcreteDTO> getAllConcreteAttribute()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdAttributeConcreteDTO>) getRequest().getParents(this.getId(), com.runwaysdk.system.metadata.VirtualizeAttributeDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.MdAttributeConcreteDTO> getAllConcreteAttribute(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdAttributeConcreteDTO>) clientRequestIF.getParents(id, com.runwaysdk.system.metadata.VirtualizeAttributeDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.VirtualizeAttributeDTO> getAllConcreteAttributeRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.VirtualizeAttributeDTO>) getRequest().getParentRelationships(this.getId(), com.runwaysdk.system.metadata.VirtualizeAttributeDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.VirtualizeAttributeDTO> getAllConcreteAttributeRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.VirtualizeAttributeDTO>) clientRequestIF.getParentRelationships(id, com.runwaysdk.system.metadata.VirtualizeAttributeDTO.CLASS);
  }
  
  public com.runwaysdk.system.metadata.VirtualizeAttributeDTO addConcreteAttribute(com.runwaysdk.system.metadata.MdAttributeConcreteDTO parent)
  {
    return (com.runwaysdk.system.metadata.VirtualizeAttributeDTO) getRequest().addParent(parent.getId(), this.getId(), com.runwaysdk.system.metadata.VirtualizeAttributeDTO.CLASS);
  }
  
  public static com.runwaysdk.system.metadata.VirtualizeAttributeDTO addConcreteAttribute(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id, com.runwaysdk.system.metadata.MdAttributeConcreteDTO parent)
  {
    return (com.runwaysdk.system.metadata.VirtualizeAttributeDTO) clientRequestIF.addParent(parent.getId(), id, com.runwaysdk.system.metadata.VirtualizeAttributeDTO.CLASS);
  }
  
  public void removeConcreteAttribute(com.runwaysdk.system.metadata.VirtualizeAttributeDTO relationship)
  {
    getRequest().deleteParent(relationship.getId());
  }
  
  public static void removeConcreteAttribute(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.metadata.VirtualizeAttributeDTO relationship)
  {
    clientRequestIF.deleteParent(relationship.getId());
  }
  
  public void removeAllConcreteAttribute()
  {
    getRequest().deleteParents(this.getId(), com.runwaysdk.system.metadata.VirtualizeAttributeDTO.CLASS);
  }
  
  public static void removeAllConcreteAttribute(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    clientRequestIF.deleteParents(id, com.runwaysdk.system.metadata.VirtualizeAttributeDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.MdViewDTO> getAllDefiningView()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdViewDTO>) getRequest().getParents(this.getId(), com.runwaysdk.system.metadata.ClassAttributeVirtualDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.MdViewDTO> getAllDefiningView(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdViewDTO>) clientRequestIF.getParents(id, com.runwaysdk.system.metadata.ClassAttributeVirtualDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.ClassAttributeVirtualDTO> getAllDefiningViewRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.ClassAttributeVirtualDTO>) getRequest().getParentRelationships(this.getId(), com.runwaysdk.system.metadata.ClassAttributeVirtualDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.ClassAttributeVirtualDTO> getAllDefiningViewRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.ClassAttributeVirtualDTO>) clientRequestIF.getParentRelationships(id, com.runwaysdk.system.metadata.ClassAttributeVirtualDTO.CLASS);
  }
  
  public com.runwaysdk.system.metadata.ClassAttributeVirtualDTO addDefiningView(com.runwaysdk.system.metadata.MdViewDTO parent)
  {
    return (com.runwaysdk.system.metadata.ClassAttributeVirtualDTO) getRequest().addParent(parent.getId(), this.getId(), com.runwaysdk.system.metadata.ClassAttributeVirtualDTO.CLASS);
  }
  
  public static com.runwaysdk.system.metadata.ClassAttributeVirtualDTO addDefiningView(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id, com.runwaysdk.system.metadata.MdViewDTO parent)
  {
    return (com.runwaysdk.system.metadata.ClassAttributeVirtualDTO) clientRequestIF.addParent(parent.getId(), id, com.runwaysdk.system.metadata.ClassAttributeVirtualDTO.CLASS);
  }
  
  public void removeDefiningView(com.runwaysdk.system.metadata.ClassAttributeVirtualDTO relationship)
  {
    getRequest().deleteParent(relationship.getId());
  }
  
  public static void removeDefiningView(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.metadata.ClassAttributeVirtualDTO relationship)
  {
    clientRequestIF.deleteParent(relationship.getId());
  }
  
  public void removeAllDefiningView()
  {
    getRequest().deleteParents(this.getId(), com.runwaysdk.system.metadata.ClassAttributeVirtualDTO.CLASS);
  }
  
  public static void removeAllDefiningView(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    clientRequestIF.deleteParents(id, com.runwaysdk.system.metadata.ClassAttributeVirtualDTO.CLASS);
  }
  
  public static com.runwaysdk.system.metadata.MdAttributeVirtualDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String id)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(id);
    
    return (com.runwaysdk.system.metadata.MdAttributeVirtualDTO) dto;
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
  
  public static com.runwaysdk.system.metadata.MdAttributeVirtualQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.metadata.MdAttributeVirtualQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.metadata.MdAttributeVirtualDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdAttributeVirtualDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdAttributeVirtualDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdAttributeVirtualDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdAttributeVirtualDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdAttributeVirtualDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdAttributeVirtualDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
