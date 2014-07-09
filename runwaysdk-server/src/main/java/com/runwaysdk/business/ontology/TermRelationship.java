/**
 * 
 */
package com.runwaysdk.business.ontology;

import com.runwaysdk.business.Relationship;

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
public class TermRelationship extends Relationship
{
  private static final long serialVersionUID = -5381444576044996448L;
  
  /**
   * @param parentId
   * @param childId
   * @param type
   */
  public TermRelationship(String parentId, String childId, String type)
  {
    super(parentId, childId, type);
  }
  
  /**
   * @param parentId
   * @param childId
   */
  public TermRelationship(String parentId, String childId)
  {
    super(parentId, childId);
  }
  
  @Override
  public String buildKey()
  {
    return this.getParentId() + this.getChildId();
  }
}
