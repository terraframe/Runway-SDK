/*******************************************************************************
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
 ******************************************************************************/
package com.runwaysdk.session;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

import com.runwaysdk.business.rbac.Operation;

public class PermissionMap implements Serializable
{
  /**
   * 
   */
  private static final long serialVersionUID = -4578512891458956504L;
  
  private ConcurrentHashMap<String, Set<Operation>> permissions;

  public PermissionMap()
  {
    this(new ConcurrentHashMap<String, Set<Operation>>());
  }

  public PermissionMap(ConcurrentHashMap<String, Set<Operation>> permissions)
  {
    this.permissions = permissions;
  }

  public Set<String> keySet()
  {
    return this.permissions.keySet();
  }

  public Set<Operation> get(String key)
  {
    return this.permissions.get(key);
  }

  public boolean containsPermission(String key, Operation operation)
  {
    Set<Operation> operations = this.get(key);

    if (operations != null)
    {
      return operations.contains(operation);
    }

    return false;
  }

  public void join(PermissionMap map, boolean enforcePrecedence)
  {
    Set<String> keys = map.keySet();

    for (String key : keys)
    {
      Set<Operation> operations = map.get(key);

      if (this.permissions.containsKey(key))
      {
        this.insert(key, operations, enforcePrecedence);
      }
      else
      {
        this.permissions.put(key, new TreeSet<Operation>(operations));
      }
    }
  }

  public void insert(String key, Set<Operation> operations)
  {
    this.insert(key, operations, false);
  }

  public void insert(String key, Set<Operation> operations, boolean enforcePrecedence)
  {
    // If permissions already exist for the mdTypeId then add on to the
    // exisiting permissions
    if (permissions.containsKey(key))
    {
      Set<Operation> current = permissions.get(key);

      for (Operation operation : operations)
      {
        if (!PermissionMap.negates(current, operation))
        {
          current.add(operation);
        }
        else if (operation.isDeny() && enforcePrecedence)
        {
          current.remove(operation.getNegation());
          current.add(operation);
        }
      }

      permissions.put(key, current);
    }
    else
    {
      this.permissions.put(key, new TreeSet<Operation>(operations));
    }
  }

  public static boolean negates(Set<Operation> operations, Operation operation2)
  {
    for (Operation operation1 : operations)
    {
      if (operation1.negates(operation2))
      {
        return true;
      }
    }

    return false;
  }

  public ConcurrentHashMap<String, Set<Operation>> getPermissions()
  {
    return permissions;
  }
}
