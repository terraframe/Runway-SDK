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

@com.runwaysdk.business.ClassSignature(hash = 1042359183)
public abstract class MdClassificationDTOBase extends com.runwaysdk.system.metadata.MetadataDTO
{
  public final static String CLASS = "com.runwaysdk.system.metadata.MdClassification";
  private static final long serialVersionUID = 1042359183;
  
  protected MdClassificationDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected MdClassificationDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public final static java.lang.String DISPLAYLABEL = "displayLabel";
  public final static java.lang.String GENERATESOURCE = "generateSource";
  public final static java.lang.String MDEDGE = "mdEdge";
  public final static java.lang.String MDVERTEX = "mdVertex";
  public final static java.lang.String PACKAGENAME = "packageName";
  public final static java.lang.String ROOT = "root";
  public final static java.lang.String TYPENAME = "typeName";
  public com.runwaysdk.system.metadata.MdClassificationDisplayLabelDTO getDisplayLabel()
  {
    return (com.runwaysdk.system.metadata.MdClassificationDisplayLabelDTO) this.getAttributeStructDTO(DISPLAYLABEL).getStructDTO();
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
  
  public Boolean getGenerateSource()
  {
    return com.runwaysdk.constants.MdAttributeBooleanUtil.getTypeSafeValue(getValue(GENERATESOURCE));
  }
  
  public void setGenerateSource(Boolean value)
  {
    if(value == null)
    {
      setValue(GENERATESOURCE, "");
    }
    else
    {
      setValue(GENERATESOURCE, java.lang.Boolean.toString(value));
    }
  }
  
  public boolean isGenerateSourceWritable()
  {
    return isWritable(GENERATESOURCE);
  }
  
  public boolean isGenerateSourceReadable()
  {
    return isReadable(GENERATESOURCE);
  }
  
  public boolean isGenerateSourceModified()
  {
    return isModified(GENERATESOURCE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeBooleanMdDTO getGenerateSourceMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeBooleanMdDTO) getAttributeDTO(GENERATESOURCE).getAttributeMdDTO();
  }
  
  public com.runwaysdk.system.metadata.MdEdgeDTO getMdEdge()
  {
    if(getValue(MDEDGE) == null || getValue(MDEDGE).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.metadata.MdEdgeDTO.get(getRequest(), getValue(MDEDGE));
    }
  }
  
  public String getMdEdgeOid()
  {
    return getValue(MDEDGE);
  }
  
  public void setMdEdge(com.runwaysdk.system.metadata.MdEdgeDTO value)
  {
    if(value == null)
    {
      setValue(MDEDGE, "");
    }
    else
    {
      setValue(MDEDGE, value.getOid());
    }
  }
  
  public boolean isMdEdgeWritable()
  {
    return isWritable(MDEDGE);
  }
  
  public boolean isMdEdgeReadable()
  {
    return isReadable(MDEDGE);
  }
  
  public boolean isMdEdgeModified()
  {
    return isModified(MDEDGE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getMdEdgeMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(MDEDGE).getAttributeMdDTO();
  }
  
  public com.runwaysdk.system.metadata.MdVertexDTO getMdVertex()
  {
    if(getValue(MDVERTEX) == null || getValue(MDVERTEX).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.metadata.MdVertexDTO.get(getRequest(), getValue(MDVERTEX));
    }
  }
  
  public String getMdVertexOid()
  {
    return getValue(MDVERTEX);
  }
  
  public void setMdVertex(com.runwaysdk.system.metadata.MdVertexDTO value)
  {
    if(value == null)
    {
      setValue(MDVERTEX, "");
    }
    else
    {
      setValue(MDVERTEX, value.getOid());
    }
  }
  
  public boolean isMdVertexWritable()
  {
    return isWritable(MDVERTEX);
  }
  
  public boolean isMdVertexReadable()
  {
    return isReadable(MDVERTEX);
  }
  
  public boolean isMdVertexModified()
  {
    return isModified(MDVERTEX);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getMdVertexMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(MDVERTEX).getAttributeMdDTO();
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
  
  public static com.runwaysdk.system.metadata.MdClassificationDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String oid)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(oid);
    
    return (com.runwaysdk.system.metadata.MdClassificationDTO) dto;
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
  
  public static com.runwaysdk.system.metadata.MdClassificationQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.metadata.MdClassificationQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.metadata.MdClassificationDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdClassificationDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdClassificationDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdClassificationDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdClassificationDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdClassificationDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdClassificationDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
