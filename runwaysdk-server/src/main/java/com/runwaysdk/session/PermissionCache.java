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
package com.runwaysdk.session;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import com.runwaysdk.business.rbac.ActorDAOIF;
import com.runwaysdk.business.rbac.Operation;
import com.runwaysdk.business.rbac.RoleDAO;
import com.runwaysdk.business.rbac.RoleDAOIF;
import com.runwaysdk.business.rbac.UserDAO;

public class PermissionCache
{
  /**
   * Synch lock that guards the ownerTypePermissions and the
   * publicTypePermissions collections when they are being repopulated.
   */
  private static final ReentrantLock                         cacheLock = new ReentrantLock();

  private static PermissionCache                             cache;

  /**
   * A hash map of the owner type permissions
   */
  private volatile ConcurrentHashMap<String, Set<Operation>> ownerPermissions;

  /**
   * A hash map of the public type permissions
   */
  
  private volatile ConcurrentHashMap<String, Set<Operation>> publicPermissions;

  private PermissionCache()
  {
    this.publicPermissions = this.getPermissions(UserDAO.getPublicUser());
    this.ownerPermissions = this.getPermissions(RoleDAO.findRole(RoleDAOIF.OWNER_ROLE));
  }

  private final ConcurrentHashMap<String, Set<Operation>> getPermissions(ActorDAOIF actor)
  {
    return actor.getOperations().getPermissions();
  }
 
  private ConcurrentHashMap<String, Set<Operation>> publicPermissions()
  {
    return this.publicPermissions;
  }
  
  private ConcurrentHashMap<String, Set<Operation>> ownerPermissions()
  {
    return this.ownerPermissions;
  }
  
  public static ConcurrentHashMap<String, Set<Operation>> getPublicPermissions()
  {
    return instance().publicPermissions();
  }

  public static ConcurrentHashMap<String, Set<Operation>> getOwnerPermissions()
  {
    return instance().ownerPermissions();
  }
  
  public static PermissionCache instance()
  {
    cacheLock.lock();

    try
    {
      if (cache == null)
      {
        cache = new PermissionCache();
      }

    }
    finally
    {
      cacheLock.unlock();
    }

    return cache;
  }
  
  /**
   * If a change has been made to the owner or public role then the Session permissions
   * needs to be reloaded
   */
  public static void reload()
  {
    cacheLock.lock();

    try
    {
      cache = null;
    }
    finally
    {
      cacheLock.unlock();
    }
  }

}
