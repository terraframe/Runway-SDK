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
package com.runwaysdk.business;

import java.util.List;

import com.runwaysdk.constants.RelationshipQueryDTOInfo;

/**
 * A class to query MdRelationship instances.
 */
public class RelationshipQueryDTO extends ElementQueryDTO
{
  private String             parentMdBusiness;

  private String             childMdBusiness;

  public static final String CLASS            = RelationshipQueryDTOInfo.CLASS;

  /**
   * 
   */
  private static final long  serialVersionUID = -2524140885774255532L;

  /**
   * Constructor to set the query type.
   * 
   * @param type
   */
  protected RelationshipQueryDTO(String type)
  {
    super(type);
    parentMdBusiness = null;
    childMdBusiness = null;
  }

  /**
   * Copies properties from the given componentQueryDTO into this one.
   * 
   * @param componentQueryDTO
   */
  public void copyProperties(RelationshipQueryDTO componentQueryDTO)
  {
    super.copyProperties(componentQueryDTO);
    this.parentMdBusiness = componentQueryDTO.parentMdBusiness;
    this.childMdBusiness = componentQueryDTO.childMdBusiness;
  }

  public void setParentMdBusiness(String parentMdBusiness)
  {
    this.parentMdBusiness = parentMdBusiness;
  }

  public void setChildMdBusiness(String childMdBusiness)
  {
    this.childMdBusiness = childMdBusiness;
  }

  public String getParentMdBusiness()
  {
    return parentMdBusiness;
  }

  public String getChildMdBusiness()
  {
    return childMdBusiness;
  }

  @SuppressWarnings("unchecked")
  public List<? extends RelationshipDTO> getResultSet()
  {
    return (List<? extends RelationshipDTO>) super.getResultSet();
  }
}
