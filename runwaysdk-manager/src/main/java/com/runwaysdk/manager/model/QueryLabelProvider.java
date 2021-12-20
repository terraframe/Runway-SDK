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

import java.util.HashMap;
import java.util.List;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

import com.runwaysdk.ComponentIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;

public class QueryLabelProvider implements ITableLabelProvider
{
  private HashMap<Integer, MdAttributeDAOIF> map;

  public QueryLabelProvider(List<MdAttributeDAOIF> attributes)
  {
    this.map = new HashMap<Integer, MdAttributeDAOIF>();

    int i = 0;

    for (MdAttributeDAOIF mdAttribute : attributes)
    {
      map.put(i++, mdAttribute);
    }
  }

  @Override
  public Image getColumnImage(Object element, int arg1)
  {
    return null;
  }

  @Override
  public String getColumnText(Object element, int index)
  {
    ComponentIF component = (ComponentIF) element;
    
    MdAttributeDAOIF mdAttribute = map.get(new Integer(index));
    
    ComponentLabelVisitor visitor = new ComponentLabelVisitor(component);
    
    mdAttribute.accept(visitor);

    return visitor.getLabel();
  }

  @Override
  public void addListener(ILabelProviderListener listener)
  {
  }

  @Override
  public void dispose()
  {
  }

  @Override
  public boolean isLabelProperty(Object element, String index)
  {
    return false;
  }

  @Override
  public void removeListener(ILabelProviderListener listener)
  {
  }
}
