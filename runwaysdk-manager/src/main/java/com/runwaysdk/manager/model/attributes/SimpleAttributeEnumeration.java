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
package com.runwaysdk.manager.model.attributes;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.runwaysdk.dataaccess.EnumerationItemDAOIF;
import com.runwaysdk.manager.model.object.PersistanceFacade;

public class SimpleAttributeEnumeration extends SimpleAttribute
{
  private Set<String> items;

  public SimpleAttributeEnumeration()
  {
    super();

    this.items = new TreeSet<String>();
  }

  public SimpleAttributeEnumeration(Set<String> items)
  {
    this();

    this.items.addAll(items);
  }

  public void setItems(Set<String> items)
  {
    this.items.clear();

    this.items.addAll(items);
  }

  public Set<String> getItems()
  {
    return items;
  }

  public List<EnumerationItemDAOIF> dereference()
  {
    List<EnumerationItemDAOIF> list = new LinkedList<EnumerationItemDAOIF>();

    for (String oid : items)
    {
      EnumerationItemDAOIF item = (EnumerationItemDAOIF) PersistanceFacade.get(oid);

      list.add(item);
    }

    return list;
  }
}
