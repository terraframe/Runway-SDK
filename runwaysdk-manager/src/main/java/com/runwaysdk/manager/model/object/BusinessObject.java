/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved. 
 * This file is part of Runway SDK(tm).
 * Runway SDK(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * Runway SDK(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.runwaysdk.manager.model.object;

import java.util.LinkedList;
import java.util.List;

import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.manager.model.IBusinessObject;

public class BusinessObject extends ElementObject implements IBusinessObject
{
  public BusinessObject(BusinessDAOIF businessDAO)
  {
    super(businessDAO);
  }

  @Override
  public EntityDAO instance()
  {
    if (!this.isNew() || this.isAppliedToDB())
    {
      return PersistanceFacade.get(this.getId()).getEntityDAO();
    }
    else
    {
      return PersistanceFacade.newInstance(this.getType());
    }
  }

  public static BusinessObject newInstance(String type)
  {
    BusinessDAO business = (BusinessDAO) PersistanceFacade.newInstance(type);

    return new BusinessObject(business);
  }

  @Override
  public List<RelationshipDAOIF> getChildren(String relationshipType)
  {
    if (!this.isNew())
    {
      return PersistanceFacade.getChildren(this.getId(), relationshipType);
    }

    return new LinkedList<RelationshipDAOIF>();
  }

  @Override
  public List<RelationshipDAOIF> getParents(String relationshipType)
  {
    if (!this.isNew())
    {
      return PersistanceFacade.getParents(this.getId(), relationshipType);
    }

    return new LinkedList<RelationshipDAOIF>();
  }
}
