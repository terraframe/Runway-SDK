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
package com.runwaysdk.manager.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.manager.model.object.BusinessObject;

public class RelationshipContentProvider implements ITreeContentProvider
{
  private String                relationshipType;

  private IRelationshipStrategy command;

  public RelationshipContentProvider(String relationshipType, IRelationshipStrategy command)
  {
    this.relationshipType = relationshipType;
    this.command = command;
  }

  @Override
  public Object[] getChildren(Object parentElement)
  {
    BusinessObject business = (BusinessObject) parentElement;

    List<RelationshipDAOIF> relationships = command.getRelationships(business, relationshipType);

    return this.toObjectArray(relationships);
  }

  @Override
  public Object getParent(Object element)
  {
    BusinessObject child = (BusinessObject) element;

    List<RelationshipDAOIF> parents = command.getInverse(child, relationshipType);

    if (parents.size() > 0)
    {
      return parents.get(0);
    }

    return parents;
  }

  @Override
  public boolean hasChildren(Object element)
  {
    return true;
  }

  @Override
  public Object[] getElements(Object input)
  {
    BusinessObject business = (BusinessObject) input;

    List<RelationshipDAOIF> relationships = command.getRelationships(business, relationshipType);

    return this.toObjectArray(relationships);
  }

  private Object[] toObjectArray(List<RelationshipDAOIF> relationships)
  {
    List<BusinessObject> children = new ArrayList<BusinessObject>();

    for (RelationshipDAOIF relationship : relationships)
    {
      children.add(command.getEndPoint(relationship));
    }

    return children.toArray(new BusinessObject[children.size()]);
  }

  @Override
  public void dispose()
  {
    // TODO Auto-generated method stub

  }

  @Override
  public void inputChanged(Viewer view, Object oldInput, Object newInput)
  {
    // TODO Auto-generated method stub

  }

}
