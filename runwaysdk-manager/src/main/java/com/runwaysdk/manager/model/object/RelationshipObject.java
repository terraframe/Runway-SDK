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
package com.runwaysdk.manager.model.object;

import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.RelationshipDAO;
import com.runwaysdk.dataaccess.RelationshipDAOIF;

public class RelationshipObject extends ElementObject
{
  /**
   * oid of the parent BusinessDAO in the relationship. <br/>
   * <b>invariant </b> parenRef != null <br/>
   * <b>invariant </b> !parenRef.trim().equals("") <br/>
   */
  private String parentOid;

  /**
   * oid of the child BusinessDAO in the relationship. <br/>
   * <b>invariant </b> childOid != null <br/>
   * <b>invariant </b> !childOid().equals("") <br/>
   */
  private String childOid;

  public RelationshipObject(RelationshipDAOIF relationshipDAO)
  {
    super(relationshipDAO);

    this.parentOid = relationshipDAO.getParentOid();
    this.childOid = relationshipDAO.getChildOid();
  }

  public String getParentOid()
  {
    return parentOid;
  }

  public String getChildOid()
  {
    return childOid;
  }

  @Override
  public EntityDAO instance()
  {
    if (this.isNew() && !this.isAppliedToDB())
    {
      return PersistanceFacade.newInstance(parentOid, childOid, this.getType());
    }

    return PersistanceFacade.get(this.getOid()).getEntityDAO();
  }

  public static RelationshipObject newInstance(String parentOid, String childOid, String type)
  {
    RelationshipDAO business = PersistanceFacade.newInstance(parentOid, childOid, type);

    return new RelationshipObject(business);
  }

}
