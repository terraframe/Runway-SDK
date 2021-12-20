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

@com.runwaysdk.business.ClassSignature(hash = -21830849)
public abstract class MdEdgeDTOBase extends com.runwaysdk.system.metadata.MdGraphClassDTO
{
  public final static String CLASS = "com.runwaysdk.system.metadata.MdEdge";
  private static final long serialVersionUID = -21830849;
  
  protected MdEdgeDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected MdEdgeDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String CHILDMDVERTEX = "childMdVertex";
  public static java.lang.String PARENTMDVERTEX = "parentMdVertex";
  public com.runwaysdk.system.metadata.MdVertexDTO getChildMdVertex()
  {
    if(getValue(CHILDMDVERTEX) == null || getValue(CHILDMDVERTEX).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.metadata.MdVertexDTO.get(getRequest(), getValue(CHILDMDVERTEX));
    }
  }
  
  public String getChildMdVertexOid()
  {
    return getValue(CHILDMDVERTEX);
  }
  
  public void setChildMdVertex(com.runwaysdk.system.metadata.MdVertexDTO value)
  {
    if(value == null)
    {
      setValue(CHILDMDVERTEX, "");
    }
    else
    {
      setValue(CHILDMDVERTEX, value.getOid());
    }
  }
  
  public boolean isChildMdVertexWritable()
  {
    return isWritable(CHILDMDVERTEX);
  }
  
  public boolean isChildMdVertexReadable()
  {
    return isReadable(CHILDMDVERTEX);
  }
  
  public boolean isChildMdVertexModified()
  {
    return isModified(CHILDMDVERTEX);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getChildMdVertexMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(CHILDMDVERTEX).getAttributeMdDTO();
  }
  
  public com.runwaysdk.system.metadata.MdVertexDTO getParentMdVertex()
  {
    if(getValue(PARENTMDVERTEX) == null || getValue(PARENTMDVERTEX).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.metadata.MdVertexDTO.get(getRequest(), getValue(PARENTMDVERTEX));
    }
  }
  
  public String getParentMdVertexOid()
  {
    return getValue(PARENTMDVERTEX);
  }
  
  public void setParentMdVertex(com.runwaysdk.system.metadata.MdVertexDTO value)
  {
    if(value == null)
    {
      setValue(PARENTMDVERTEX, "");
    }
    else
    {
      setValue(PARENTMDVERTEX, value.getOid());
    }
  }
  
  public boolean isParentMdVertexWritable()
  {
    return isWritable(PARENTMDVERTEX);
  }
  
  public boolean isParentMdVertexReadable()
  {
    return isReadable(PARENTMDVERTEX);
  }
  
  public boolean isParentMdVertexModified()
  {
    return isModified(PARENTMDVERTEX);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getParentMdVertexMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(PARENTMDVERTEX).getAttributeMdDTO();
  }
  
  public static com.runwaysdk.system.metadata.MdEdgeDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String oid)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(oid);
    
    return (com.runwaysdk.system.metadata.MdEdgeDTO) dto;
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
  
  public static com.runwaysdk.system.metadata.MdEdgeQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.metadata.MdEdgeQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.metadata.MdEdgeDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdEdgeDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdEdgeDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdEdgeDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdEdgeDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdEdgeDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdEdgeDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
