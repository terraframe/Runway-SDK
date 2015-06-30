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

@com.runwaysdk.business.ClassSignature(hash = -1274401877)
public abstract class MdViewDTOBase extends com.runwaysdk.system.metadata.MdSessionDTO
{
  public final static String CLASS = "com.runwaysdk.system.metadata.MdView";
  private static final long serialVersionUID = -1274401877;
  
  protected MdViewDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected MdViewDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String QUERYBASECLASS = "queryBaseClass";
  public static java.lang.String QUERYBASESOURCE = "queryBaseSource";
  public static java.lang.String QUERYDTOCLASS = "queryDTOclass";
  public static java.lang.String QUERYDTOSOURCE = "queryDTOsource";
  public static java.lang.String QUERYSTUBCLASS = "queryStubClass";
  public static java.lang.String QUERYSTUBSOURCE = "queryStubSource";
  public static java.lang.String SUPERMDVIEW = "superMdView";
  public byte[] getQueryBaseClass()
  {
    return super.getBlob(QUERYBASECLASS);
  }
  
  public boolean isQueryBaseClassWritable()
  {
    return isWritable(QUERYBASECLASS);
  }
  
  public boolean isQueryBaseClassReadable()
  {
    return isReadable(QUERYBASECLASS);
  }
  
  public boolean isQueryBaseClassModified()
  {
    return isModified(QUERYBASECLASS);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeBlobMdDTO getQueryBaseClassMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeBlobMdDTO) getAttributeDTO(QUERYBASECLASS).getAttributeMdDTO();
  }
  
  public String getQueryBaseSource()
  {
    return getValue(QUERYBASESOURCE);
  }
  
  public boolean isQueryBaseSourceWritable()
  {
    return isWritable(QUERYBASESOURCE);
  }
  
  public boolean isQueryBaseSourceReadable()
  {
    return isReadable(QUERYBASESOURCE);
  }
  
  public boolean isQueryBaseSourceModified()
  {
    return isModified(QUERYBASESOURCE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeClobMdDTO getQueryBaseSourceMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeClobMdDTO) getAttributeDTO(QUERYBASESOURCE).getAttributeMdDTO();
  }
  
  public byte[] getQueryDTOclass()
  {
    return super.getBlob(QUERYDTOCLASS);
  }
  
  public boolean isQueryDTOclassWritable()
  {
    return isWritable(QUERYDTOCLASS);
  }
  
  public boolean isQueryDTOclassReadable()
  {
    return isReadable(QUERYDTOCLASS);
  }
  
  public boolean isQueryDTOclassModified()
  {
    return isModified(QUERYDTOCLASS);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeBlobMdDTO getQueryDTOclassMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeBlobMdDTO) getAttributeDTO(QUERYDTOCLASS).getAttributeMdDTO();
  }
  
  public String getQueryDTOsource()
  {
    return getValue(QUERYDTOSOURCE);
  }
  
  public boolean isQueryDTOsourceWritable()
  {
    return isWritable(QUERYDTOSOURCE);
  }
  
  public boolean isQueryDTOsourceReadable()
  {
    return isReadable(QUERYDTOSOURCE);
  }
  
  public boolean isQueryDTOsourceModified()
  {
    return isModified(QUERYDTOSOURCE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeClobMdDTO getQueryDTOsourceMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeClobMdDTO) getAttributeDTO(QUERYDTOSOURCE).getAttributeMdDTO();
  }
  
  public byte[] getQueryStubClass()
  {
    return super.getBlob(QUERYSTUBCLASS);
  }
  
  public boolean isQueryStubClassWritable()
  {
    return isWritable(QUERYSTUBCLASS);
  }
  
  public boolean isQueryStubClassReadable()
  {
    return isReadable(QUERYSTUBCLASS);
  }
  
  public boolean isQueryStubClassModified()
  {
    return isModified(QUERYSTUBCLASS);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeBlobMdDTO getQueryStubClassMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeBlobMdDTO) getAttributeDTO(QUERYSTUBCLASS).getAttributeMdDTO();
  }
  
  public String getQueryStubSource()
  {
    return getValue(QUERYSTUBSOURCE);
  }
  
  public void setQueryStubSource(String value)
  {
    if(value == null)
    {
      setValue(QUERYSTUBSOURCE, "");
    }
    else
    {
      setValue(QUERYSTUBSOURCE, value);
    }
  }
  
  public boolean isQueryStubSourceWritable()
  {
    return isWritable(QUERYSTUBSOURCE);
  }
  
  public boolean isQueryStubSourceReadable()
  {
    return isReadable(QUERYSTUBSOURCE);
  }
  
  public boolean isQueryStubSourceModified()
  {
    return isModified(QUERYSTUBSOURCE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeClobMdDTO getQueryStubSourceMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeClobMdDTO) getAttributeDTO(QUERYSTUBSOURCE).getAttributeMdDTO();
  }
  
  public com.runwaysdk.system.metadata.MdViewDTO getSuperMdView()
  {
    if(getValue(SUPERMDVIEW) == null || getValue(SUPERMDVIEW).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.metadata.MdViewDTO.get(getRequest(), getValue(SUPERMDVIEW));
    }
  }
  
  public String getSuperMdViewId()
  {
    return getValue(SUPERMDVIEW);
  }
  
  public void setSuperMdView(com.runwaysdk.system.metadata.MdViewDTO value)
  {
    if(value == null)
    {
      setValue(SUPERMDVIEW, "");
    }
    else
    {
      setValue(SUPERMDVIEW, value.getId());
    }
  }
  
  public boolean isSuperMdViewWritable()
  {
    return isWritable(SUPERMDVIEW);
  }
  
  public boolean isSuperMdViewReadable()
  {
    return isReadable(SUPERMDVIEW);
  }
  
  public boolean isSuperMdViewModified()
  {
    return isModified(SUPERMDVIEW);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getSuperMdViewMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(SUPERMDVIEW).getAttributeMdDTO();
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.MdViewDTO> getAllChildViews()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdViewDTO>) getRequest().getChildren(this.getId(), com.runwaysdk.system.metadata.ViewInheritanceDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.MdViewDTO> getAllChildViews(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdViewDTO>) clientRequestIF.getChildren(id, com.runwaysdk.system.metadata.ViewInheritanceDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.ViewInheritanceDTO> getAllChildViewsRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.ViewInheritanceDTO>) getRequest().getChildRelationships(this.getId(), com.runwaysdk.system.metadata.ViewInheritanceDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.ViewInheritanceDTO> getAllChildViewsRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.ViewInheritanceDTO>) clientRequestIF.getChildRelationships(id, com.runwaysdk.system.metadata.ViewInheritanceDTO.CLASS);
  }
  
  public com.runwaysdk.system.metadata.ViewInheritanceDTO addChildViews(com.runwaysdk.system.metadata.MdViewDTO child)
  {
    return (com.runwaysdk.system.metadata.ViewInheritanceDTO) getRequest().addChild(this.getId(), child.getId(), com.runwaysdk.system.metadata.ViewInheritanceDTO.CLASS);
  }
  
  public static com.runwaysdk.system.metadata.ViewInheritanceDTO addChildViews(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id, com.runwaysdk.system.metadata.MdViewDTO child)
  {
    return (com.runwaysdk.system.metadata.ViewInheritanceDTO) clientRequestIF.addChild(id, child.getId(), com.runwaysdk.system.metadata.ViewInheritanceDTO.CLASS);
  }
  
  public void removeChildViews(com.runwaysdk.system.metadata.ViewInheritanceDTO relationship)
  {
    getRequest().deleteChild(relationship.getId());
  }
  
  public static void removeChildViews(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.metadata.ViewInheritanceDTO relationship)
  {
    clientRequestIF.deleteChild(relationship.getId());
  }
  
  public void removeAllChildViews()
  {
    getRequest().deleteChildren(this.getId(), com.runwaysdk.system.metadata.ViewInheritanceDTO.CLASS);
  }
  
  public static void removeAllChildViews(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    clientRequestIF.deleteChildren(id, com.runwaysdk.system.metadata.ViewInheritanceDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.MdAttributeVirtualDTO> getAllVirtualAttribute()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdAttributeVirtualDTO>) getRequest().getChildren(this.getId(), com.runwaysdk.system.metadata.ClassAttributeVirtualDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.MdAttributeVirtualDTO> getAllVirtualAttribute(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdAttributeVirtualDTO>) clientRequestIF.getChildren(id, com.runwaysdk.system.metadata.ClassAttributeVirtualDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.ClassAttributeVirtualDTO> getAllVirtualAttributeRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.ClassAttributeVirtualDTO>) getRequest().getChildRelationships(this.getId(), com.runwaysdk.system.metadata.ClassAttributeVirtualDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.ClassAttributeVirtualDTO> getAllVirtualAttributeRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.ClassAttributeVirtualDTO>) clientRequestIF.getChildRelationships(id, com.runwaysdk.system.metadata.ClassAttributeVirtualDTO.CLASS);
  }
  
  public com.runwaysdk.system.metadata.ClassAttributeVirtualDTO addVirtualAttribute(com.runwaysdk.system.metadata.MdAttributeVirtualDTO child)
  {
    return (com.runwaysdk.system.metadata.ClassAttributeVirtualDTO) getRequest().addChild(this.getId(), child.getId(), com.runwaysdk.system.metadata.ClassAttributeVirtualDTO.CLASS);
  }
  
  public static com.runwaysdk.system.metadata.ClassAttributeVirtualDTO addVirtualAttribute(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id, com.runwaysdk.system.metadata.MdAttributeVirtualDTO child)
  {
    return (com.runwaysdk.system.metadata.ClassAttributeVirtualDTO) clientRequestIF.addChild(id, child.getId(), com.runwaysdk.system.metadata.ClassAttributeVirtualDTO.CLASS);
  }
  
  public void removeVirtualAttribute(com.runwaysdk.system.metadata.ClassAttributeVirtualDTO relationship)
  {
    getRequest().deleteChild(relationship.getId());
  }
  
  public static void removeVirtualAttribute(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.metadata.ClassAttributeVirtualDTO relationship)
  {
    clientRequestIF.deleteChild(relationship.getId());
  }
  
  public void removeAllVirtualAttribute()
  {
    getRequest().deleteChildren(this.getId(), com.runwaysdk.system.metadata.ClassAttributeVirtualDTO.CLASS);
  }
  
  public static void removeAllVirtualAttribute(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    clientRequestIF.deleteChildren(id, com.runwaysdk.system.metadata.ClassAttributeVirtualDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.MdViewDTO> getAllParentView()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdViewDTO>) getRequest().getParents(this.getId(), com.runwaysdk.system.metadata.ViewInheritanceDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.MdViewDTO> getAllParentView(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdViewDTO>) clientRequestIF.getParents(id, com.runwaysdk.system.metadata.ViewInheritanceDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.ViewInheritanceDTO> getAllParentViewRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.ViewInheritanceDTO>) getRequest().getParentRelationships(this.getId(), com.runwaysdk.system.metadata.ViewInheritanceDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.ViewInheritanceDTO> getAllParentViewRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.ViewInheritanceDTO>) clientRequestIF.getParentRelationships(id, com.runwaysdk.system.metadata.ViewInheritanceDTO.CLASS);
  }
  
  public com.runwaysdk.system.metadata.ViewInheritanceDTO addParentView(com.runwaysdk.system.metadata.MdViewDTO parent)
  {
    return (com.runwaysdk.system.metadata.ViewInheritanceDTO) getRequest().addParent(parent.getId(), this.getId(), com.runwaysdk.system.metadata.ViewInheritanceDTO.CLASS);
  }
  
  public static com.runwaysdk.system.metadata.ViewInheritanceDTO addParentView(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id, com.runwaysdk.system.metadata.MdViewDTO parent)
  {
    return (com.runwaysdk.system.metadata.ViewInheritanceDTO) clientRequestIF.addParent(parent.getId(), id, com.runwaysdk.system.metadata.ViewInheritanceDTO.CLASS);
  }
  
  public void removeParentView(com.runwaysdk.system.metadata.ViewInheritanceDTO relationship)
  {
    getRequest().deleteParent(relationship.getId());
  }
  
  public static void removeParentView(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.metadata.ViewInheritanceDTO relationship)
  {
    clientRequestIF.deleteParent(relationship.getId());
  }
  
  public void removeAllParentView()
  {
    getRequest().deleteParents(this.getId(), com.runwaysdk.system.metadata.ViewInheritanceDTO.CLASS);
  }
  
  public static void removeAllParentView(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    clientRequestIF.deleteParents(id, com.runwaysdk.system.metadata.ViewInheritanceDTO.CLASS);
  }
  
  public static com.runwaysdk.system.metadata.MdViewDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String id)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(id);
    
    return (com.runwaysdk.system.metadata.MdViewDTO) dto;
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
  
  public static com.runwaysdk.system.metadata.MdViewQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.metadata.MdViewQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.metadata.MdViewDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdViewDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdViewDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdViewDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdViewDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdViewDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdViewDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
