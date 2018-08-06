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
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.constants.ExceptionConstants;
import com.runwaysdk.constants.RelationshipDTOInfo;
import com.runwaysdk.transport.attributes.AttributeDTO;
import com.runwaysdk.transport.metadata.RelationshipTypeMd;


public class RelationshipDTO extends ElementDTO
{
  /**
   * Auto-generated serial oid.
   */
  private static final long serialVersionUID = -1045931690869189494L;

  public final static String CLASS = RelationshipDTOInfo.CLASS;

  /**
   * The oid of the child in this relationship.
   */
  private String            childOid;

  /**
   * The oid of the parent in this relationship.
   */
  private String            parentOid;

  /**
   * Generic business object.
   *
   * @param sessionId
   * @param clientRequest
   */
  protected RelationshipDTO(ClientRequestIF clientRequest, String type, String parentOid, String childOid)
  {
    super(clientRequest, type);
  }

  /**
   * Constructor used when the object is instantiated on the front-end.
   */
  protected RelationshipDTO(ClientRequestIF clientRequest, String parentOid, String childOid)
  {
    super(clientRequest);

    String _type = this.getDeclaredType();

    if(!_type.equals(RelationshipDTO.class.getName()) && clientRequest != null)
    {
      RelationshipDTO dto = (RelationshipDTO) clientRequest.newGenericMutable(_type);
      // Not bothering to clone the map, since the source dto is gargage collected anyway.
      this.copyProperties(dto, dto.attributeMap);
    }

    this.parentOid = parentOid;
    this.childOid  = childOid;
  }


  protected RelationshipDTO(RelationshipDTO relationshipDTO, ClientRequestIF clientRequest)
  {
    super(clientRequest);

    if(!this.getDeclaredType().equals(relationshipDTO.getType()))
    {
      String msg = "Cannot instaniate [" + this.getDeclaredType() +
                   "] with a generic DTO of [" + relationshipDTO.getType() + "]";

      CommonExceptionProcessor.processException(
          ExceptionConstants.ProgrammingErrorException.getExceptionClass(), msg);
    }

    this.parentOid = relationshipDTO.parentOid;
    this.childOid = relationshipDTO.childOid;

    this.copyProperties(relationshipDTO, relationshipDTO.attributeMap);
  }

  /**
   * Constructor used when the object is instantiated on the front-end or back-end.
   * If the clientRequest is null, then it is instantiated on the front-end, otherwise on the back-end.
   */
  protected RelationshipDTO(ClientRequestIF clientRequest, String type, Map<String, AttributeDTO> attributeMap, String parentOid, String childOid)
  {
    super(clientRequest, type, attributeMap);

    this.parentOid = parentOid;
    this.childOid  = childOid;
  }

  /**
   * Returns the value of the child reference.
   *
   * @return The reference of the child.
   */
  public String getChildOid()
  {
    return childOid;
  }


  /**
   * Returns the value of the parent reference.
   *
   * @return The reference of the parent.
   */
  public String getParentOid()
  {
    return parentOid;
  }

  /**
   * Sets the parent oid (for internal use only).
   *
   * @param parentOid
   */
  void setParentOid(String parentOid)
  {
    this.parentOid = parentOid;
  }

  /**
   * Sets the child oid (for internal use only).
   * @param childOid
   */
  void setChildOid(String childOid)
  {
    this.childOid = childOid;
  }

  /**
   * Copies over properties from the given componentDTO.
   * @param componentDTOIF
   */
  public void copyProperties(ComponentDTOIF componentDTOIF)
  {
    super.copyProperties(componentDTOIF);

    if (componentDTOIF instanceof RelationshipDTO)
    {
      RelationshipDTO relationshipDTO = (RelationshipDTO)componentDTOIF;
      this.parentOid = relationshipDTO.parentOid;
      this.childOid = relationshipDTO.childOid;
    }
  }

  @Override
  protected String getDeclaredType()
  {
    return RelationshipDTO.class.getName();
  }

  /**
   * Overrides super.getTypeMd() to return a RelationshipMd
   * object instead of a TypeMd.
   */
  @Override
  public RelationshipTypeMd getMd()
  {
    if (!isTypeMdSet())
    {
      return new RelationshipTypeMd();
    }
    else
    {
      return (RelationshipTypeMd) super.getMd();
    }
  }


  /**********************************************************************************/
  /**                      Generated accessor methods                              **/
  /**********************************************************************************/

}
