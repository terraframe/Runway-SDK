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
package com.runwaysdk.business;

import java.util.LinkedList;
import java.util.List;
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
   * Auto-generated serial id.
   */
  private static final long serialVersionUID = -4669065823599432023L;

  /**
   * State.
   */
  private String            state;

  /**
   * A list of all transitions from the current state (if applicable).
   */
  private List<String> transitions;

  /**
   * Generic business object.
   *
   * @param sessionId
   * @param clientRequest
   */
  protected BusinessDTO(ClientRequestIF clientRequest, String type)
  {
    super(clientRequest, type);

    state = "";
    transitions = new LinkedList<String>();
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

    transitions = new LinkedList<String>();

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

    transitions = new LinkedList<String>();

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

    state = "";
    transitions = new LinkedList<String>();
  }

  /**
   * Returns the state of this DTO.
   *
   * @return
   */
  public String getState()
  {
    return state;
  }

  /**
   * Sets the state of this DTO
   */
  public void setState(String state)
  {
    this.state = state;
  }

  /**
   * Returns a list of available transitions.
   *
   * @return
   */
  public List<String> getTransitions()
  {
    return transitions;
  }

  public void setTransitions(List<String> transitions)
  {
    this.transitions = transitions;
  }

  /**
   * Copies over properties from the given componentDTO.
   * @param componentDTOIF
   */
  public void copyProperties(ComponentDTOIF componentDTOIF)
  {
    super.copyProperties(componentDTOIF);

    if (componentDTOIF instanceof BusinessDTO)
    {
      // state
      BusinessDTO businessDTO = (BusinessDTO)componentDTOIF;
      this.setState(businessDTO.getState());

      // enum values
      this.setTransitions(businessDTO.getTransitions());
    }
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
    return (java.util.List<? extends com.runwaysdk.system.metadata.MetadataDTO>) getRequest().getParents(this.getId(), com.runwaysdk.system.metadata.MetadataRelationshipDTO.CLASS);
  }

  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.MetadataDTO> getAllParentMetadata(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MetadataDTO>) clientRequestIF.getParents(id, com.runwaysdk.system.metadata.MetadataRelationshipDTO.CLASS);
  }

  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.MetadataRelationshipDTO> getAllParentMetadataRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MetadataRelationshipDTO>) getRequest().getParentRelationships(this.getId(), com.runwaysdk.system.metadata.MetadataRelationshipDTO.CLASS);
  }

  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.MetadataRelationshipDTO> getAllParentMetadataRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MetadataRelationshipDTO>) clientRequestIF.getParentRelationships(id, com.runwaysdk.system.metadata.MetadataRelationshipDTO.CLASS);
  }

  public com.runwaysdk.system.metadata.MetadataRelationshipDTO addParentMetadata(com.runwaysdk.system.metadata.MetadataDTO parent)
  {
    return (com.runwaysdk.system.metadata.MetadataRelationshipDTO) getRequest().addParent(parent.getId(), this.getId(), com.runwaysdk.system.metadata.MetadataRelationshipDTO.CLASS);
  }

  public static com.runwaysdk.system.metadata.MetadataRelationshipDTO addParentMetadata(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id, com.runwaysdk.system.metadata.MetadataDTO parent)
  {
    return (com.runwaysdk.system.metadata.MetadataRelationshipDTO) clientRequestIF.addParent(parent.getId(), id, com.runwaysdk.system.metadata.MetadataRelationshipDTO.CLASS);
  }

  public void removeParentMetadata(com.runwaysdk.system.metadata.MetadataRelationshipDTO relationship)
  {
    getRequest().deleteParent(relationship.getId());
  }

  public static void removeParentMetadata(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.metadata.MetadataRelationshipDTO relationship)
  {
    clientRequestIF.deleteParent(relationship.getId());
  }

  public void removeAllParentMetadata()
  {
    getRequest().deleteParents(this.getId(), com.runwaysdk.system.metadata.MetadataRelationshipDTO.CLASS);
  }

  public static void removeAllParentMetadata(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    clientRequestIF.deleteParents(id, com.runwaysdk.system.metadata.MetadataRelationshipDTO.CLASS);
  }
}
