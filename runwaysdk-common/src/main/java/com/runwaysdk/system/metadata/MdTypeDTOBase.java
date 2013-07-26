/*******************************************************************************
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
 ******************************************************************************/
package com.runwaysdk.system.metadata;

@com.runwaysdk.business.ClassSignature(hash = -1125446900)
public abstract class MdTypeDTOBase extends com.runwaysdk.system.metadata.MetadataDTO
{
  public final static String CLASS = "com.runwaysdk.system.metadata.MdType";
  private static final long serialVersionUID = -1125446900;
  
  protected MdTypeDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected MdTypeDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String BASECLASS = "baseClass";
  public static java.lang.String BASESOURCE = "baseSource";
  public static java.lang.String DISPLAYLABEL = "displayLabel";
  public static java.lang.String DTOCLASS = "dtoClass";
  public static java.lang.String DTOSOURCE = "dtoSource";
  public static java.lang.String EXPORTED = "exported";
  public static java.lang.String JSBASE = "jsBase";
  public static java.lang.String JSSTUB = "jsStub";
  public static java.lang.String PACKAGENAME = "packageName";
  public static java.lang.String ROOTID = "rootId";
  public static java.lang.String TYPENAME = "typeName";
  public byte[] getBaseClass()
  {
    return super.getBlob(BASECLASS);
  }
  
  public boolean isBaseClassWritable()
  {
    return isWritable(BASECLASS);
  }
  
  public boolean isBaseClassReadable()
  {
    return isReadable(BASECLASS);
  }
  
  public boolean isBaseClassModified()
  {
    return isModified(BASECLASS);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeBlobMdDTO getBaseClassMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeBlobMdDTO) getAttributeDTO(BASECLASS).getAttributeMdDTO();
  }
  
  public String getBaseSource()
  {
    return getValue(BASESOURCE);
  }
  
  public boolean isBaseSourceWritable()
  {
    return isWritable(BASESOURCE);
  }
  
  public boolean isBaseSourceReadable()
  {
    return isReadable(BASESOURCE);
  }
  
  public boolean isBaseSourceModified()
  {
    return isModified(BASESOURCE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeClobMdDTO getBaseSourceMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeClobMdDTO) getAttributeDTO(BASESOURCE).getAttributeMdDTO();
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
  
  public byte[] getDtoClass()
  {
    return super.getBlob(DTOCLASS);
  }
  
  public boolean isDtoClassWritable()
  {
    return isWritable(DTOCLASS);
  }
  
  public boolean isDtoClassReadable()
  {
    return isReadable(DTOCLASS);
  }
  
  public boolean isDtoClassModified()
  {
    return isModified(DTOCLASS);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeBlobMdDTO getDtoClassMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeBlobMdDTO) getAttributeDTO(DTOCLASS).getAttributeMdDTO();
  }
  
  public String getDtoSource()
  {
    return getValue(DTOSOURCE);
  }
  
  public boolean isDtoSourceWritable()
  {
    return isWritable(DTOSOURCE);
  }
  
  public boolean isDtoSourceReadable()
  {
    return isReadable(DTOSOURCE);
  }
  
  public boolean isDtoSourceModified()
  {
    return isModified(DTOSOURCE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeClobMdDTO getDtoSourceMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeClobMdDTO) getAttributeDTO(DTOSOURCE).getAttributeMdDTO();
  }
  
  public Boolean getExported()
  {
    return com.runwaysdk.constants.MdAttributeBooleanUtil.getTypeSafeValue(getValue(EXPORTED));
  }
  
  public void setExported(Boolean value)
  {
    if(value == null)
    {
      setValue(EXPORTED, "");
    }
    else
    {
      setValue(EXPORTED, java.lang.Boolean.toString(value));
    }
  }
  
  public boolean isExportedWritable()
  {
    return isWritable(EXPORTED);
  }
  
  public boolean isExportedReadable()
  {
    return isReadable(EXPORTED);
  }
  
  public boolean isExportedModified()
  {
    return isModified(EXPORTED);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeBooleanMdDTO getExportedMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeBooleanMdDTO) getAttributeDTO(EXPORTED).getAttributeMdDTO();
  }
  
  public String getJsBase()
  {
    return getValue(JSBASE);
  }
  
  public void setJsBase(String value)
  {
    if(value == null)
    {
      setValue(JSBASE, "");
    }
    else
    {
      setValue(JSBASE, value);
    }
  }
  
  public boolean isJsBaseWritable()
  {
    return isWritable(JSBASE);
  }
  
  public boolean isJsBaseReadable()
  {
    return isReadable(JSBASE);
  }
  
  public boolean isJsBaseModified()
  {
    return isModified(JSBASE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeClobMdDTO getJsBaseMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeClobMdDTO) getAttributeDTO(JSBASE).getAttributeMdDTO();
  }
  
  public String getJsStub()
  {
    return getValue(JSSTUB);
  }
  
  public void setJsStub(String value)
  {
    if(value == null)
    {
      setValue(JSSTUB, "");
    }
    else
    {
      setValue(JSSTUB, value);
    }
  }
  
  public boolean isJsStubWritable()
  {
    return isWritable(JSSTUB);
  }
  
  public boolean isJsStubReadable()
  {
    return isReadable(JSSTUB);
  }
  
  public boolean isJsStubModified()
  {
    return isModified(JSSTUB);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeClobMdDTO getJsStubMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeClobMdDTO) getAttributeDTO(JSSTUB).getAttributeMdDTO();
  }
  
  public String getPackageName()
  {
    return getValue(PACKAGENAME);
  }
  
  public void setPackageName(String value)
  {
    if(value == null)
    {
      setValue(PACKAGENAME, "");
    }
    else
    {
      setValue(PACKAGENAME, value);
    }
  }
  
  public boolean isPackageNameWritable()
  {
    return isWritable(PACKAGENAME);
  }
  
  public boolean isPackageNameReadable()
  {
    return isReadable(PACKAGENAME);
  }
  
  public boolean isPackageNameModified()
  {
    return isModified(PACKAGENAME);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getPackageNameMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(PACKAGENAME).getAttributeMdDTO();
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getRootIdMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(ROOTID).getAttributeMdDTO();
  }
  
  public String getTypeName()
  {
    return getValue(TYPENAME);
  }
  
  public void setTypeName(String value)
  {
    if(value == null)
    {
      setValue(TYPENAME, "");
    }
    else
    {
      setValue(TYPENAME, value);
    }
  }
  
  public boolean isTypeNameWritable()
  {
    return isWritable(TYPENAME);
  }
  
  public boolean isTypeNameReadable()
  {
    return isReadable(TYPENAME);
  }
  
  public boolean isTypeNameModified()
  {
    return isModified(TYPENAME);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getTypeNameMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(TYPENAME).getAttributeMdDTO();
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.MdMethodDTO> getAllMdMethod()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdMethodDTO>) getRequest().getChildren(this.getId(), com.runwaysdk.system.metadata.TypeMethodDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.MdMethodDTO> getAllMdMethod(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdMethodDTO>) clientRequestIF.getChildren(id, com.runwaysdk.system.metadata.TypeMethodDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.TypeMethodDTO> getAllMdMethodRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.TypeMethodDTO>) getRequest().getChildRelationships(this.getId(), com.runwaysdk.system.metadata.TypeMethodDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.TypeMethodDTO> getAllMdMethodRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.TypeMethodDTO>) clientRequestIF.getChildRelationships(id, com.runwaysdk.system.metadata.TypeMethodDTO.CLASS);
  }
  
  public com.runwaysdk.system.metadata.TypeMethodDTO addMdMethod(com.runwaysdk.system.metadata.MdMethodDTO child)
  {
    return (com.runwaysdk.system.metadata.TypeMethodDTO) getRequest().addChild(this.getId(), child.getId(), com.runwaysdk.system.metadata.TypeMethodDTO.CLASS);
  }
  
  public static com.runwaysdk.system.metadata.TypeMethodDTO addMdMethod(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id, com.runwaysdk.system.metadata.MdMethodDTO child)
  {
    return (com.runwaysdk.system.metadata.TypeMethodDTO) clientRequestIF.addChild(id, child.getId(), com.runwaysdk.system.metadata.TypeMethodDTO.CLASS);
  }
  
  public void removeMdMethod(com.runwaysdk.system.metadata.TypeMethodDTO relationship)
  {
    getRequest().deleteChild(relationship.getId());
  }
  
  public static void removeMdMethod(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.metadata.TypeMethodDTO relationship)
  {
    clientRequestIF.deleteChild(relationship.getId());
  }
  
  public void removeAllMdMethod()
  {
    getRequest().deleteChildren(this.getId(), com.runwaysdk.system.metadata.TypeMethodDTO.CLASS);
  }
  
  public static void removeAllMdMethod(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    clientRequestIF.deleteChildren(id, com.runwaysdk.system.metadata.TypeMethodDTO.CLASS);
  }
  
  public static com.runwaysdk.system.metadata.MdTypeDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String id)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(id);
    
    return (com.runwaysdk.system.metadata.MdTypeDTO) dto;
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
  
  public static com.runwaysdk.system.metadata.MdTypeQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.metadata.MdTypeQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.metadata.MdTypeDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdTypeDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdTypeDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdTypeDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdTypeDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdTypeDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdTypeDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}