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
package com.runwaysdk.business;

import java.util.Map;

import com.runwaysdk.CommonExceptionProcessor;
import com.runwaysdk.constants.BusinessDTOInfo;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.constants.ExceptionConstants;
import com.runwaysdk.transport.attributes.AttributeDTO;


/**
 * The BusinessDTO class provies a value object that represents a
 * Business. It offers basic accessors and mutators but no actual
 * Business behavior.
 */
public class BusinessDTO extends ElementDTO
{
  public final static String CLASS = BusinessDTOInfo.CLASS;

  /**
   * Auto-generated serial oid.
   */
  private static final long serialVersionUID = -4669065823599432023L;

  /**
   * Generic business object.
   *
   * @param sessionId
   * @param clientRequest
   */
  protected BusinessDTO(ClientRequestIF clientRequest, String type)
  {
    super(clientRequest, type);
  }

  /**
   * Constructor which specifies the session and ClientRequestIF. Only subclasses should call
   * this method. When this constructor is called, a new instance of the proper type
   * is constructed via a clientRequest call. The default values are copied from there.
   *
   * @param clientRequest
   */
  protected BusinessDTO(ClientRequestIF clientRequest)
  {
    super(clientRequest);

    String _type = this.getDeclaredType();

    // only get default values if a subclass of BusinessDTO is requested.
    if(!_type.equals(BusinessDTO.class.getName()) && clientRequest != null)
    {
      BusinessDTO dto = clientRequest.newGenericBusiness(_type);
      // Not bothering to clone the map, since the source dto is garbage collected anyway.
      this.copyProperties(dto, dto.attributeMap);
    }
  }

  /**
   * @param businessDTO
   */
  protected BusinessDTO(BusinessDTO businessDTO, ClientRequestIF clientRequest)
  {
    super(clientRequest);

    if(!this.getDeclaredType().equals(businessDTO.getType()))
    {
      String msg = "Cannot instaniate [" + this.getDeclaredType() +
                   "] with a generic DTO of [" + businessDTO.getType() + "]";

      CommonExceptionProcessor.processException(
          ExceptionConstants.ProgrammingErrorException.getExceptionClass(), msg);
    }

    this.copyProperties(businessDTO, businessDTO.attributeMap);
  }

  /**
   * Constructor used when the object is instantiated on the front-end or back-end.
   * If the clientRequest is null, then it is instantiated on the front-end, otherwise on the back-end.
   */
  protected BusinessDTO(ClientRequestIF clientRequest, String type, Map<String, AttributeDTO> attributeMap)
  {
    super(clientRequest, type, attributeMap);
  }

  @Override
  protected String getDeclaredType()
  {
    return BusinessDTO.class.getName();
  }

  /**********************************************************************************/
  /**                      Generated accessor methods                              **/
  /**********************************************************************************/
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.MetadataDTO> getAllParentMetadata()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MetadataDTO>) getRequest().getParents(this.getOid(), com.runwaysdk.system.metadata.MetadataRelationshipDTO.CLASS);
  }

  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.MetadataDTO> getAllParentMetadata(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MetadataDTO>) clientRequestIF.getParents(oid, com.runwaysdk.system.metadata.MetadataRelationshipDTO.CLASS);
  }

  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.MetadataRelationshipDTO> getAllParentMetadataRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MetadataRelationshipDTO>) getRequest().getParentRelationships(this.getOid(), com.runwaysdk.system.metadata.MetadataRelationshipDTO.CLASS);
  }

  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.MetadataRelationshipDTO> getAllParentMetadataRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MetadataRelationshipDTO>) clientRequestIF.getParentRelationships(oid, com.runwaysdk.system.metadata.MetadataRelationshipDTO.CLASS);
  }

  public com.runwaysdk.system.metadata.MetadataRelationshipDTO addParentMetadata(com.runwaysdk.system.metadata.MetadataDTO parent)
  {
    return (com.runwaysdk.system.metadata.MetadataRelationshipDTO) getRequest().addParent(parent.getOid(), this.getOid(), com.runwaysdk.system.metadata.MetadataRelationshipDTO.CLASS);
  }

  public static com.runwaysdk.system.metadata.MetadataRelationshipDTO addParentMetadata(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid, com.runwaysdk.system.metadata.MetadataDTO parent)
  {
    return (com.runwaysdk.system.metadata.MetadataRelationshipDTO) clientRequestIF.addParent(parent.getOid(), oid, com.runwaysdk.system.metadata.MetadataRelationshipDTO.CLASS);
  }

  public void removeParentMetadata(com.runwaysdk.system.metadata.MetadataRelationshipDTO relationship)
  {
    getRequest().deleteParent(relationship.getOid());
  }

  public static void removeParentMetadata(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.metadata.MetadataRelationshipDTO relationship)
  {
    clientRequestIF.deleteParent(relationship.getOid());
  }

  public void removeAllParentMetadata()
  {
    getRequest().deleteParents(this.getOid(), com.runwaysdk.system.metadata.MetadataRelationshipDTO.CLASS);
  }

  public static void removeAllParentMetadata(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    clientRequestIF.deleteParents(oid, com.runwaysdk.system.metadata.MetadataRelationshipDTO.CLASS);
  }
}
