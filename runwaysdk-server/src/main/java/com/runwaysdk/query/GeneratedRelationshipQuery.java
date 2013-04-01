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
package com.runwaysdk.query;

import com.runwaysdk.business.RelationshipQuery;

public abstract class GeneratedRelationshipQuery extends GeneratedEntityQuery
{
  /**
   *
   */
  protected GeneratedRelationshipQuery ()
  {
    super();
  }

  /**
   * Returns RelationshipQuery that all generated query methods delegate to.
   * @return RelationshipQuery that all generated query methods delegate to.
   */
  protected RelationshipQuery getRelationshipQuery()
  {
    return (RelationshipQuery)this.getComponentQuery();
  }

  /**
   * Sets the RelationshipQuery that all generated query methods delegate to.
   */
  protected void setRelationshipQuery(RelationshipQuery relationshipQuery)
  {
    this.setComponentQuery(relationshipQuery);
  }

  /**
   * Returns an attribute character statement object where the name of the
   * attribute is the Relationship.PARENT_ID.
   * @return Attribute character statement object.
   */
  public AttributeCharacter parentId()
  {
    return this.getRelationshipQuery().parentId();
  }

  /**
   * Returns an attribute character statement object where the name of the
   * attribute is the Relationship.PARENT_ID.
   * @param userDefinedAlias
   * @return Attribute character statement object.
   */
  public AttributeCharacter parentId(String userDefinedAlias)
  {
    return this.getRelationshipQuery().parentId(userDefinedAlias, null);
  }

  /**
   * Returns an attribute character statement object where the name of the
   * attribute is the Relationship.PARENT_ID.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return Attribute character statement object.
   */
  public AttributeCharacter parentId(String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return this.getRelationshipQuery().parentId(userDefinedAlias, userDefinedDisplayLabel);
  }

  /**
   * Returns an attribute character statement object where the name of the
   * attribute is the Relationship.CHILD_ID.
   * @return Attribute character statement object.
   */
  public AttributeCharacter childId()
  {
    return this.getRelationshipQuery().childId();
  }

  /**
   * Returns an attribute character statement object where the name of the
   * attribute is the Relationship.CHILD_ID.
   * @param userDefinedAlias
   * @return Attribute character statement object.
   */
  public AttributeCharacter childId(String userDefinedAlias)
  {
    return this.getRelationshipQuery().childId(userDefinedAlias, null);
  }


  /**
   * Returns an attribute character statement object where the name of the
   * attribute is the Relationship.CHILD_ID.
   * @param userDefinedAlias
   * @param userDefinedDisplayLabel
   * @return Attribute character statement object.
   */
  public AttributeCharacter childId(String userDefinedAlias, String userDefinedDisplayLabel)
  {
    return this.getRelationshipQuery().childId(userDefinedAlias, userDefinedDisplayLabel);
  }

}
