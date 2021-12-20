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
package com.runwaysdk.manager.view;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

import com.runwaysdk.dataaccess.EntityDAOIF;

public class EntitySorter extends ViewerSorter
{
  private String attributeName;
  
  private boolean isAscending;
  
  public EntitySorter(String attributeName, boolean isAscending)
  {
    this.attributeName = attributeName;
    this.isAscending = isAscending;
  }
  
  @Override
  public int compare(Viewer viewer, Object e1, Object e2)
  {
    if(e1 instanceof EntityDAOIF && e2 instanceof EntityDAOIF)
    {
      EntityDAOIF entity1 = (EntityDAOIF) e1;
      EntityDAOIF entity2 = (EntityDAOIF) e2;

      String value1 = entity1.getValue(attributeName);
      String value2 = entity2.getValue(attributeName);

      int compareTo = value1.compareTo(value2);
      
      if(!this.isAscending)
      {
        return compareTo * -1;
      }
      
      return compareTo;
    }
    
    return super.compare(viewer, e1, e2);
  }
  
  public boolean isAscending()
  {
    return isAscending;
  }
  
  public String getAttributeName()
  {
    return attributeName;
  }
}
