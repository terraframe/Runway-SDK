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
package com.runwaysdk.manager.model;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.runwaysdk.dataaccess.EntityDAOIF;
import com.runwaysdk.manager.model.object.PersistanceFacade;
import com.runwaysdk.query.EntityQuery;

public class QueryContentProvider implements IStructuredContentProvider
{
  private int pageNumber;

  private int pageSize;

  public QueryContentProvider(int pageSize)
  {
    this.pageNumber = 1;
    this.pageSize = pageSize;
  }

  /**
   * Gets the food items for the list
   * 
   * @param input
   *          the data model
   * @return Object[]
   */
  public Object[] getElements(Object input)
  {
    List<EntityDAOIF> results = null;

    if (pageSize != -1)
    {
      results = PersistanceFacade.getResults((EntityQuery) input, pageSize, pageNumber);
    }
    else
    {
      results = PersistanceFacade.getResults((EntityQuery) input);
    }

    return results.toArray(new EntityDAOIF[results.size()]);
  }

  /**
   * Disposes any created resources
   */
  public void dispose()
  {
    // Do nothing
  }

  /**
   * Called when the input changes
   * 
   * @param viewer
   *          the viewer
   * @param oldInput
   *          the old input
   * @param newInput
   *          the new input
   */
  public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
  {
    this.pageNumber = 1;
  }

  public void setPageNumber(int pageNumber)
  {
    this.pageNumber = pageNumber;
  }
}
