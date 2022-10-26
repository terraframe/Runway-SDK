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

@com.runwaysdk.business.ClassSignature(hash = 845739507)
public abstract class MdParameterDTOBase extends com.runwaysdk.system.metadata.MetadataDTO
{
  public final static String CLASS = "com.runwaysdk.system.metadata.MdParameter";
  private static final long serialVersionUID = 845739507;
  
  protected MdParameterDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected MdParameterDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public final static java.lang.String DISPLAYLABEL = "displayLabel";
  public final static java.lang.String METADATA = "metadata";
  public final static java.lang.String PARAMETERNAME = "parameterName";
  public final static java.lang.String PARAMETERORDER = "parameterOrder";
  public final static java.lang.String PARAMETERTYPE = "parameterType";
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
  
  public com.runwaysdk.system.metadata.MetadataDTO getMetadata()
  {
    if(getValue(METADATA) == null || getValue(METADATA).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.metadata.MetadataDTO.get(getRequest(), getValue(METADATA));
    }
  }
  
  public String getMetadataId()
  {
    return getValue(METADATA);
  }
  
  public void setMetadata(com.runwaysdk.system.metadata.MetadataDTO value)
  {
    if(value == null)
    {
      setValue(METADATA, "");
    }
    else
    {
      setValue(METADATA, value.getOid());
    }
  }
  
  public boolean isMetadataWritable()
  {
    return isWritable(METADATA);
  }
  
  public boolean isMetadataReadable()
  {
    return isReadable(METADATA);
  }
  
  public boolean isMetadataModified()
  {
    return isModified(METADATA);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getMetadataMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(METADATA).getAttributeMdDTO();
  }
  
  public String getParameterName()
  {
    return getValue(PARAMETERNAME);
  }
  
  public void setParameterName(String value)
  {
    if(value == null)
    {
      setValue(PARAMETERNAME, "");
    }
    else
    {
      setValue(PARAMETERNAME, value);
    }
  }
  
  public boolean isParameterNameWritable()
  {
    return isWritable(PARAMETERNAME);
  }
  
  public boolean isParameterNameReadable()
  {
    return isReadable(PARAMETERNAME);
  }
  
  public boolean isParameterNameModified()
  {
    return isModified(PARAMETERNAME);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getParameterNameMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(PARAMETERNAME).getAttributeMdDTO();
  }
  
  public Integer getParameterOrder()
  {
    return com.runwaysdk.constants.MdAttributeIntegerUtil.getTypeSafeValue(getValue(PARAMETERORDER));
  }
  
  public void setParameterOrder(Integer value)
  {
    if(value == null)
    {
      setValue(PARAMETERORDER, "");
    }
    else
    {
      setValue(PARAMETERORDER, java.lang.Integer.toString(value));
    }
  }
  
  public boolean isParameterOrderWritable()
  {
    return isWritable(PARAMETERORDER);
  }
  
  public boolean isParameterOrderReadable()
  {
    return isReadable(PARAMETERORDER);
  }
  
  public boolean isParameterOrderModified()
  {
    return isModified(PARAMETERORDER);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeNumberMdDTO getParameterOrderMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeNumberMdDTO) getAttributeDTO(PARAMETERORDER).getAttributeMdDTO();
  }
  
  public String getParameterType()
  {
    return getValue(PARAMETERTYPE);
  }
  
  public void setParameterType(String value)
  {
    if(value == null)
    {
      setValue(PARAMETERTYPE, "");
    }
    else
    {
      setValue(PARAMETERTYPE, value);
    }
  }
  
  public boolean isParameterTypeWritable()
  {
    return isWritable(PARAMETERTYPE);
  }
  
  public boolean isParameterTypeReadable()
  {
    return isReadable(PARAMETERTYPE);
  }
  
  public boolean isParameterTypeModified()
  {
    return isModified(PARAMETERTYPE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getParameterTypeMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(PARAMETERTYPE).getAttributeMdDTO();
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.MetadataDTO> getAllMetadata()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MetadataDTO>) getRequest().getParents(this.getOid(), com.runwaysdk.system.MetadataParameterDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.MetadataDTO> getAllMetadata(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MetadataDTO>) clientRequestIF.getParents(oid, com.runwaysdk.system.MetadataParameterDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.MetadataParameterDTO> getAllMetadataRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.MetadataParameterDTO>) getRequest().getParentRelationships(this.getOid(), com.runwaysdk.system.MetadataParameterDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.MetadataParameterDTO> getAllMetadataRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.MetadataParameterDTO>) clientRequestIF.getParentRelationships(oid, com.runwaysdk.system.MetadataParameterDTO.CLASS);
  }
  
  public com.runwaysdk.system.MetadataParameterDTO addMetadata(com.runwaysdk.system.metadata.MetadataDTO parent)
  {
    return (com.runwaysdk.system.MetadataParameterDTO) getRequest().addParent(parent.getOid(), this.getOid(), com.runwaysdk.system.MetadataParameterDTO.CLASS);
  }
  
  public static com.runwaysdk.system.MetadataParameterDTO addMetadata(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid, com.runwaysdk.system.metadata.MetadataDTO parent)
  {
    return (com.runwaysdk.system.MetadataParameterDTO) clientRequestIF.addParent(parent.getOid(), oid, com.runwaysdk.system.MetadataParameterDTO.CLASS);
  }
  
  public void removeMetadata(com.runwaysdk.system.MetadataParameterDTO relationship)
  {
    getRequest().deleteParent(relationship.getOid());
  }
  
  public static void removeMetadata(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.MetadataParameterDTO relationship)
  {
    clientRequestIF.deleteParent(relationship.getOid());
  }
  
  public void removeAllMetadata()
  {
    getRequest().deleteParents(this.getOid(), com.runwaysdk.system.MetadataParameterDTO.CLASS);
  }
  
  public static void removeAllMetadata(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    clientRequestIF.deleteParents(oid, com.runwaysdk.system.MetadataParameterDTO.CLASS);
  }
  
  public static com.runwaysdk.system.metadata.MdParameterDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String oid)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(oid);
    
    return (com.runwaysdk.system.metadata.MdParameterDTO) dto;
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
  
  public static com.runwaysdk.system.metadata.MdParameterQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.metadata.MdParameterQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.metadata.MdParameterDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdParameterDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdParameterDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdParameterDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdParameterDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdParameterDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdParameterDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
