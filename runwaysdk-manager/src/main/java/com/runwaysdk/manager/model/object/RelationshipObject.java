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
package com.runwaysdk.manager.model.object;

import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.RelationshipDAO;
import com.runwaysdk.dataaccess.RelationshipDAOIF;

public class RelationshipObject extends ElementObject
{
  /**
   * id of the parent BusinessDAO in the relationship. <br/>
   * <b>invariant </b> parenRef != null <br/>
   * <b>invariant </b> !parenRef.trim().equals("") <br/>
   */
  private String parentId;

  /**
   * id of the child BusinessDAO in the relationship. <br/>
   * <b>invariant </b> childId != null <br/>
   * <b>invariant </b> !childId().equals("") <br/>
   */
  private String childId;

  public RelationshipObject(RelationshipDAOIF relationshipDAO)
  {
    super(relationshipDAO);

    this.parentId = relationshipDAO.getParentId();
    this.childId = relationshipDAO.getChildId();
  }

  public String getParentId()
  {
    return parentId;
  }

  public String getChildId()
  {
    return childId;
  }

  @Override
  public EntityDAO instance()
  {
    if (this.isNew() && !this.isAppliedToDB())
    {
      return PersistanceFacade.newInstance(parentId, childId, this.getType());
    }

    return PersistanceFacade.get(this.getId()).getEntityDAO();
  }

  public static RelationshipObject newInstance(String parentId, String childId, String type)
  {
    RelationshipDAO business = PersistanceFacade.newInstance(parentId, childId, type);

    return new RelationshipObject(business);
  }

}
