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
package com.runwaysdk.dataaccess.schemamanager.model;

import java.util.HashMap;
import java.util.Map;

import org.xml.sax.Attributes;

import com.runwaysdk.dataaccess.schemamanager.xml.UnKeyedElement;

public class PermissionActionElement extends UnKeyedElement
{  
  private Map<String, PermissionElement> permissions;
  
  public PermissionActionElement(Attributes map, String tag, SchemaElementIF parent)
  {
    super(map, tag, parent);
    
    this.permissions = new HashMap<String, PermissionElement>();
  }
  
  /**
   * Performs a recursive search on this {@link PermissionElement} for any
   * descendant which has the same tag and attributes of the given
   * {@link PermissionElement}
   * 
   * @param element
   * @return
   */
  public PermissionElement getDescendant(String key)
  {
    return permissions.get(key);
  }
  
  public void addDescendent(PermissionElement element)
  {
    permissions.put(element.getKey(), element);
  }
  
  public void removeDescendent(String key)
  {
    permissions.remove(key);
  }
  
  @Override
  public boolean addChild(SchemaElementIF child)
  {
    boolean success = super.addChild(child);

    if (child instanceof PermissionElement && success)
    {
      this.addDescendent((PermissionElement) child);
    }
    
    return success;
  }
  
  public void removeDescendant(PermissionElement element)
  {
    PermissionElement descendant = this.getDescendant(element.getKey());

    // Do not remove a descendant which has existing children because
    // these children are revoked permissions which have not been granted
    if (descendant != null && !descendant.hasChildren())
    {
      // THEN: remove the revoke permission element
      descendant.remove();
      
      element.setExport(false);
    }
  }
}
