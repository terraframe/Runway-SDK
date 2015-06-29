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
package com.runwaysdk.transport.conversion.business;

import java.util.Locale;
import java.util.Map;

import com.runwaysdk.business.ComponentDTOFacade;
import com.runwaysdk.business.Relationship;
import com.runwaysdk.business.RelationshipDTO;
import com.runwaysdk.dataaccess.MdRelationshipDAOIF;
import com.runwaysdk.session.Session;
import com.runwaysdk.transport.attributes.AttributeDTO;
import com.runwaysdk.transport.metadata.RelationshipTypeMd;
import com.runwaysdk.transport.metadata.TypeMd;

/**
 * Converts a Relationship into a RelationshipDTO
 */
public class RelationshipToRelationshipDTO extends ElementToElementDTO
{

  /**
   *
   * @param sessionId
   * @param relationship
   */
  public RelationshipToRelationshipDTO(String sessionId, Relationship relationship, boolean convertMetaData)
  {
    super(sessionId, relationship, convertMetaData);
  }


  /**
   *
   */
  protected Relationship getComponentIF()
  {
    return (Relationship)super.getComponentIF();
  }

  /**
   * Instantiates RelationshipDTO (not type safe)
   * @param sessionId
   * @param type
   * @param attributeMap
   * @param parentId
   * @param childId
   * @param newInstance
   * @param readable
   * @param writable
   * @param modified
   * @return RelationshipDTO (not type safe)
   */
  protected RelationshipDTO factoryMethod(Map<String, AttributeDTO> attributeMap,
      boolean newInstance, boolean readable, boolean writable, boolean modified)
  {
    return ComponentDTOFacade.buildRelationshipDTO(
        null,  this.getComponentIF().getType(), attributeMap, this.getComponentIF().getParentId(), this.getComponentIF().getChildId(),
        newInstance, readable, writable, modified, this.getComponentIF().toString(), this.getComponentIF().checkUserLock());
  }
  /**
   * Returns the populated RelationshipDTO
   */
  public RelationshipDTO populate()
  {
    RelationshipDTO relationshipDTO = (RelationshipDTO) super.populate();

    return relationshipDTO;
  }

  @Override
  public MdRelationshipDAOIF getMdTypeIF()
  {
    return (MdRelationshipDAOIF) super.getMdTypeIF();
  }

  /**
   * Returns a new TypeMd object.
   *
   * @return
   */
  @Override
  protected TypeMd createTypeMd()
  {
    if (this.convertMetaData())
    {
      String parentMdBusiness = getMdTypeIF().getParentMdBusiness().definesType();
      String childMdBusiness = getMdTypeIF().getChildMdBusiness().definesType();

      Locale locale = Session.getCurrentLocale();

      return new RelationshipTypeMd(this.getMdTypeIF().getDisplayLabel(locale), this.getMdTypeIF().getDescription(locale), this.getMdTypeIF().getId(), parentMdBusiness, childMdBusiness);
    }
    else
    {
      return new RelationshipTypeMd();
    }
  }

}
