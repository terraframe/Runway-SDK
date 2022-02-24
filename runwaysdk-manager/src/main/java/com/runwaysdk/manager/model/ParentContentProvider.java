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
package com.runwaysdk.manager.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAOIF;

public class ParentContentProvider implements ITreeContentProvider
{
  private String relationshipType;

  public ParentContentProvider(String relationshipType)
  {
    this.relationshipType = relationshipType;
  }

  @Override
  public Object[] getChildren(Object parentElement)
  {
    BusinessDAOIF business = (BusinessDAOIF) parentElement;

    List<RelationshipDAOIF> relationships = business.getParents(relationshipType);

    return this.toObjectArray(relationships);
  }

  @Override
  public Object getParent(Object element)
  {
    BusinessDAOIF child = (BusinessDAOIF) element;

    List<RelationshipDAOIF> parents = child.getChildren(relationshipType);

    if (parents.size() > 0)
    {
      return parents.get(0);
    }

    return parents;
  }

  @Override
  public boolean hasChildren(Object element)
  {
    BusinessDAOIF business = (BusinessDAOIF) element;

    List<RelationshipDAOIF> relationships = business.getParents(relationshipType);

    boolean hasChildren = relationships.size() > 0;

    return hasChildren;
  }

  @Override
  public Object[] getElements(Object input)
  {
    BusinessDAOIF business = (BusinessDAOIF) input;

    List<RelationshipDAOIF> relationships = business.getParents(relationshipType);

    return this.toObjectArray(relationships);
  }

  private Object[] toObjectArray(List<RelationshipDAOIF> relationships)
  {
    List<BusinessDAOIF> children = new ArrayList<BusinessDAOIF>();

    for (RelationshipDAOIF relationship : relationships)
    {
      children.add(relationship.getChild());
    }

    return children.toArray(new BusinessDAOIF[children.size()]);
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
