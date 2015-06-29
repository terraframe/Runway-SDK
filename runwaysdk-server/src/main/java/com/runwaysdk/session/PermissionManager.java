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
package com.runwaysdk.session;

import java.util.HashMap;
import java.util.Set;

import com.runwaysdk.business.rbac.ActorDAOIF;
import com.runwaysdk.dataaccess.MetadataDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAOIF;

public class PermissionManager
{
  /**
   * Hashmap containing all of the permissions for a given actor. Key is the
   * role id;
   */
  private HashMap<String, HashMap<String, RelationshipDAOIF>> permissions;

  public PermissionManager()
  {
    this(new HashMap<String, HashMap<String, RelationshipDAOIF>>());
  }

  public PermissionManager(HashMap<String, HashMap<String, RelationshipDAOIF>> permissions)
  {
    this.permissions = permissions;
  }

  public HashMap<String, RelationshipDAOIF> getPermissions(ActorDAOIF actor)
  {
    String key = actor.getId();

    if (!permissions.containsKey(key))
    {
      Set<RelationshipDAOIF> _permissions = actor.getPermissions();
      
      HashMap<String, RelationshipDAOIF> map = new HashMap<String, RelationshipDAOIF>();
      
      for(RelationshipDAOIF relationship : _permissions)
      {
        map.put(relationship.getChildId(), relationship);
      }

      permissions.put(key, map);
    }

    return permissions.get(key);
  }
  
  public RelationshipDAOIF getPermissions(ActorDAOIF actor, MetadataDAOIF metadata)
  {
    HashMap<String, RelationshipDAOIF> map = this.getPermissions(actor);
    
    if(map != null)
    {
      return map.get(metadata.getId());
    }
    
    return null;
  }

  public void put(RelationshipDAOIF relationship)
  {
    String key = relationship.getParentId();

    if (permissions.containsKey(key))
    {
      HashMap<String, RelationshipDAOIF> map = permissions.get(key);
      
      map.put(relationship.getChildId(), relationship);
    }
    else
    {
      HashMap<String, RelationshipDAOIF> map = new HashMap<String, RelationshipDAOIF>();

      map.put(relationship.getChildId(), relationship);
      
      permissions.put(key, map);
    }
  }
}
