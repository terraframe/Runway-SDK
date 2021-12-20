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

@com.runwaysdk.business.ClassSignature(hash = -664866546)
public abstract class MdMethodDTOBase extends com.runwaysdk.system.metadata.MetadataDTO
{
  public final static String CLASS = "com.runwaysdk.system.metadata.MdMethod";
  private static final long serialVersionUID = -664866546;
  
  protected MdMethodDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected MdMethodDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String DISPLAYLABEL = "displayLabel";
  public static java.lang.String ISSTATIC = "isStatic";
  public static java.lang.String MDTYPE = "mdType";
  public static java.lang.String METHODNAME = "methodName";
  public static java.lang.String RETURNTYPE = "returnType";
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
  
  public Boolean getIsStatic()
  {
    return com.runwaysdk.constants.MdAttributeBooleanUtil.getTypeSafeValue(getValue(ISSTATIC));
  }
  
  public void setIsStatic(Boolean value)
  {
    if(value == null)
    {
      setValue(ISSTATIC, "");
    }
    else
    {
      setValue(ISSTATIC, java.lang.Boolean.toString(value));
    }
  }
  
  public boolean isIsStaticWritable()
  {
    return isWritable(ISSTATIC);
  }
  
  public boolean isIsStaticReadable()
  {
    return isReadable(ISSTATIC);
  }
  
  public boolean isIsStaticModified()
  {
    return isModified(ISSTATIC);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeBooleanMdDTO getIsStaticMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeBooleanMdDTO) getAttributeDTO(ISSTATIC).getAttributeMdDTO();
  }
  
  public com.runwaysdk.system.metadata.MdTypeDTO getMdType()
  {
    if(getValue(MDTYPE) == null || getValue(MDTYPE).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.metadata.MdTypeDTO.get(getRequest(), getValue(MDTYPE));
    }
  }
  
  public String getMdTypeId()
  {
    return getValue(MDTYPE);
  }
  
  public void setMdType(com.runwaysdk.system.metadata.MdTypeDTO value)
  {
    if(value == null)
    {
      setValue(MDTYPE, "");
    }
    else
    {
      setValue(MDTYPE, value.getOid());
    }
  }
  
  public boolean isMdTypeWritable()
  {
    return isWritable(MDTYPE);
  }
  
  public boolean isMdTypeReadable()
  {
    return isReadable(MDTYPE);
  }
  
  public boolean isMdTypeModified()
  {
    return isModified(MDTYPE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getMdTypeMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(MDTYPE).getAttributeMdDTO();
  }
  
  public String getMethodName()
  {
    return getValue(METHODNAME);
  }
  
  public void setMethodName(String value)
  {
    if(value == null)
    {
      setValue(METHODNAME, "");
    }
    else
    {
      setValue(METHODNAME, value);
    }
  }
  
  public boolean isMethodNameWritable()
  {
    return isWritable(METHODNAME);
  }
  
  public boolean isMethodNameReadable()
  {
    return isReadable(METHODNAME);
  }
  
  public boolean isMethodNameModified()
  {
    return isModified(METHODNAME);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getMethodNameMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(METHODNAME).getAttributeMdDTO();
  }
  
  public String getReturnType()
  {
    return getValue(RETURNTYPE);
  }
  
  public void setReturnType(String value)
  {
    if(value == null)
    {
      setValue(RETURNTYPE, "");
    }
    else
    {
      setValue(RETURNTYPE, value);
    }
  }
  
  public boolean isReturnTypeWritable()
  {
    return isWritable(RETURNTYPE);
  }
  
  public boolean isReturnTypeReadable()
  {
    return isReadable(RETURNTYPE);
  }
  
  public boolean isReturnTypeModified()
  {
    return isModified(RETURNTYPE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getReturnTypeMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(RETURNTYPE).getAttributeMdDTO();
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.MethodActorDTO> getAllMethodActor()
  {
    return (java.util.List<? extends com.runwaysdk.system.MethodActorDTO>) getRequest().getChildren(this.getOid(), com.runwaysdk.system.metadata.MdMethodMethodActorDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.MethodActorDTO> getAllMethodActor(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.MethodActorDTO>) clientRequestIF.getChildren(oid, com.runwaysdk.system.metadata.MdMethodMethodActorDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.MdMethodMethodActorDTO> getAllMethodActorRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdMethodMethodActorDTO>) getRequest().getChildRelationships(this.getOid(), com.runwaysdk.system.metadata.MdMethodMethodActorDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.MdMethodMethodActorDTO> getAllMethodActorRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdMethodMethodActorDTO>) clientRequestIF.getChildRelationships(oid, com.runwaysdk.system.metadata.MdMethodMethodActorDTO.CLASS);
  }
  
  public com.runwaysdk.system.metadata.MdMethodMethodActorDTO addMethodActor(com.runwaysdk.system.MethodActorDTO child)
  {
    return (com.runwaysdk.system.metadata.MdMethodMethodActorDTO) getRequest().addChild(this.getOid(), child.getOid(), com.runwaysdk.system.metadata.MdMethodMethodActorDTO.CLASS);
  }
  
  public static com.runwaysdk.system.metadata.MdMethodMethodActorDTO addMethodActor(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid, com.runwaysdk.system.MethodActorDTO child)
  {
    return (com.runwaysdk.system.metadata.MdMethodMethodActorDTO) clientRequestIF.addChild(oid, child.getOid(), com.runwaysdk.system.metadata.MdMethodMethodActorDTO.CLASS);
  }
  
  public void removeMethodActor(com.runwaysdk.system.metadata.MdMethodMethodActorDTO relationship)
  {
    getRequest().deleteChild(relationship.getOid());
  }
  
  public static void removeMethodActor(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.metadata.MdMethodMethodActorDTO relationship)
  {
    clientRequestIF.deleteChild(relationship.getOid());
  }
  
  public void removeAllMethodActor()
  {
    getRequest().deleteChildren(this.getOid(), com.runwaysdk.system.metadata.MdMethodMethodActorDTO.CLASS);
  }
  
  public static void removeAllMethodActor(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    clientRequestIF.deleteChildren(oid, com.runwaysdk.system.metadata.MdMethodMethodActorDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.MdTypeDTO> getAllMdType()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdTypeDTO>) getRequest().getParents(this.getOid(), com.runwaysdk.system.metadata.TypeMethodDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.MdTypeDTO> getAllMdType(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdTypeDTO>) clientRequestIF.getParents(oid, com.runwaysdk.system.metadata.TypeMethodDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.TypeMethodDTO> getAllMdTypeRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.TypeMethodDTO>) getRequest().getParentRelationships(this.getOid(), com.runwaysdk.system.metadata.TypeMethodDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.TypeMethodDTO> getAllMdTypeRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.TypeMethodDTO>) clientRequestIF.getParentRelationships(oid, com.runwaysdk.system.metadata.TypeMethodDTO.CLASS);
  }
  
  public com.runwaysdk.system.metadata.TypeMethodDTO addMdType(com.runwaysdk.system.metadata.MdTypeDTO parent)
  {
    return (com.runwaysdk.system.metadata.TypeMethodDTO) getRequest().addParent(parent.getOid(), this.getOid(), com.runwaysdk.system.metadata.TypeMethodDTO.CLASS);
  }
  
  public static com.runwaysdk.system.metadata.TypeMethodDTO addMdType(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid, com.runwaysdk.system.metadata.MdTypeDTO parent)
  {
    return (com.runwaysdk.system.metadata.TypeMethodDTO) clientRequestIF.addParent(parent.getOid(), oid, com.runwaysdk.system.metadata.TypeMethodDTO.CLASS);
  }
  
  public void removeMdType(com.runwaysdk.system.metadata.TypeMethodDTO relationship)
  {
    getRequest().deleteParent(relationship.getOid());
  }
  
  public static void removeMdType(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.metadata.TypeMethodDTO relationship)
  {
    clientRequestIF.deleteParent(relationship.getOid());
  }
  
  public void removeAllMdType()
  {
    getRequest().deleteParents(this.getOid(), com.runwaysdk.system.metadata.TypeMethodDTO.CLASS);
  }
  
  public static void removeAllMdType(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    clientRequestIF.deleteParents(oid, com.runwaysdk.system.metadata.TypeMethodDTO.CLASS);
  }
  
  public static com.runwaysdk.system.metadata.MdMethodDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String oid)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(oid);
    
    return (com.runwaysdk.system.metadata.MdMethodDTO) dto;
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
  
  public static com.runwaysdk.system.metadata.MdMethodQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.metadata.MdMethodQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.metadata.MdMethodDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdMethodDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdMethodDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdMethodDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdMethodDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdMethodDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdMethodDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
