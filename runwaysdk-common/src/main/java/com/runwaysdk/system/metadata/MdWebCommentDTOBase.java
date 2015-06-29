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

@com.runwaysdk.business.ClassSignature(hash = -301149086)
public abstract class MdWebCommentDTOBase extends com.runwaysdk.system.metadata.MdWebFieldDTO
{
  public final static String CLASS = "com.runwaysdk.system.metadata.MdWebComment";
  private static final long serialVersionUID = -301149086;
  
  protected MdWebCommentDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected MdWebCommentDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String COMMENTTEXT = "commentText";
  public com.runwaysdk.system.metadata.MdWebCommentCommentTextDTO getCommentText()
  {
    return (com.runwaysdk.system.metadata.MdWebCommentCommentTextDTO) this.getAttributeStructDTO(COMMENTTEXT).getStructDTO();
  }
  
  public boolean isCommentTextWritable()
  {
    return isWritable(COMMENTTEXT);
  }
  
  public boolean isCommentTextReadable()
  {
    return isReadable(COMMENTTEXT);
  }
  
  public boolean isCommentTextModified()
  {
    return isModified(COMMENTTEXT);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeLocalTextMdDTO getCommentTextMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeLocalTextMdDTO) getAttributeDTO(COMMENTTEXT).getAttributeMdDTO();
  }
  
  public static com.runwaysdk.system.metadata.MdWebCommentDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String id)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(id);
    
    return (com.runwaysdk.system.metadata.MdWebCommentDTO) dto;
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
  
  public static com.runwaysdk.system.metadata.MdWebCommentQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.metadata.MdWebCommentQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.metadata.MdWebCommentDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdWebCommentDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdWebCommentDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdWebCommentDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdWebCommentDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdWebCommentDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdWebCommentDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
