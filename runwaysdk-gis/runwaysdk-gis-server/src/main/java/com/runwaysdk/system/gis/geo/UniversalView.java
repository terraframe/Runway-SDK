/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK GIS(tm).
 *
 * Runway SDK GIS(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway SDK GIS(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK GIS(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.system.gis.geo;

import java.util.LinkedList;
import java.util.List;

import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;

public class UniversalView extends UniversalViewBase
{
  private static final long serialVersionUID = 1078282592;
  
  public UniversalView()
  {
    super();
  }
  
  public String toString()
  {
    String template = "[%s] - Type [%s] with id [%s].";
    Object [] args = new Object[]{this.getClassDisplayLabel(), this.getDisplayLabel(), this.getUniversalId()};
    return String.format(template, args);
  };
  
  /**
   * Returns a UniversalView representing the Universal with the given id.
   * @param id
   * @return
   */
  public static UniversalView getUniversalView(String id)
  {
    return getUniversalViews(id)[0];
  }
  
  /**
   * Returns the UniversalViews representing the Universals with the given ids.
   * 
   * @param ids
   * @return
   */
  public static UniversalView[] getUniversalViews(String ... ids)
  {
    UniversalViewQuery q = new UniversalViewQuery(new QueryFactory(), ids);
    OIterator<? extends UniversalView> iter = q.getIterator();
    
    List<UniversalView> views = new LinkedList<UniversalView>();
    
    try
    {
      while(iter.hasNext())
      {
        views.add(iter.next());
      }
      
      return views.toArray(new UniversalView[]{});
    }
    finally
    {
      iter.close();
    }
  }
  
  @Override
  public boolean equals(Object obj)
  {
    boolean equals = super.equals(obj);
    
    if(!equals && obj instanceof UniversalView)
    {
      equals = ((UniversalView) obj).getUniversalId().equals(this.getUniversalId());
    }
    
    if(!equals && obj instanceof Universal)
    {
      equals = ((Universal) obj).getId().equals(this.getUniversalId());
    }
    
    return equals;
  }
}
