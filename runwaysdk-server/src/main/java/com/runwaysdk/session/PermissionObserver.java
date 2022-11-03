/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.session;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.runwaysdk.business.rbac.ActorDAOIF;
import com.runwaysdk.business.rbac.RoleDAO;
import com.runwaysdk.business.rbac.RoleDAOIF;
import com.runwaysdk.business.rbac.SingleActorDAOIF;

/**
 * PermissionManager is a Singleton Class that acts as a bridge between
 * MethodActors, which are used to set and remove permissions for MdMethods, and
 * Methods, which are used for checking permission access.
 * 
 * @author Justin Smethie
 */
public class PermissionObserver
{
  /**
   * Singleton PermissionManager
   */
  private static PermissionObserver manager = null;

  /**
   * Synchronized List of registered listeners
   */
  private List<PermissionEntity>    listeners;

  /**
   * Creates a PermissionManager with no listeners
   */
  private PermissionObserver()
  {
    listeners = Collections.synchronizedList(new ArrayList<PermissionEntity>());
  }

  /**
   * Returns the list of listeners currently registered
   * 
   * @return
   */
  private List<PermissionEntity> getListeners()
  {
    return listeners;
  }

  /**
   * Returns the singleton manager if one exists. If not then it creates a
   * manager and then returns it.
   * 
   * @return
   */
  private synchronized static PermissionObserver getManager()
  {
    if (manager == null)
    {
      manager = new PermissionObserver();
    }

    return manager;
  }

  /**
   * Notifys all listeners which reference the Actor which modified that a
   * change in Permissions has occured.
   * 
   * @param actor
   *          The Actor that was modified
   */
  public static synchronized void notify(ActorDAOIF actor)
  {
    if (isGlobalActor(actor))
    {
      updateGlobalPermissions(actor);
    }
    else if (actor instanceof RoleDAOIF)
    {
      // If the actor updated is a role then notify all members
      // of that role that a change in permissions has occurred

      RoleDAOIF role = (RoleDAOIF) actor;

      for (SingleActorDAOIF singleActorIF : role.authorizedActors())
      {
        notify(singleActorIF);
      }
    }
    else
    {
      notify((SingleActorDAOIF) actor);
    }
  }

  /**
   * Notifys all listeners which reference the singleActor which modified that a
   * change in Permissions has occured.
   * 
   * @param singleActorIF
   *          The singleActor that was modified
   */
  private static void notify(SingleActorDAOIF singleActorIF)
  {
    String oid = singleActorIF.getOid();
    PermissionObserver manager = getManager();

    for (PermissionEntity entity : manager.getListeners())
    {
      String entityId = entity.getUser().getOid();

      if (oid.equals(entityId))
      {
        entity.notify(PermissionEvent.PERMISSION_CHANGE);
      }
    }
  }

  /**
   * If a change has been made to the owner or public role then the Session
   * permissions needs to be reloaded
   */
  private static synchronized void updateGlobalPermissions(ActorDAOIF actor)
  {
    if (RoleDAO.OWNER_ID.equals(actor.getOid()) || RoleDAO.PUBLIC_ROLE_ID.equals(actor.getOid()))
    {
      PermissionCache.reload();
    }
  }

  /**
   * Determines if an actor is a global actor
   * 
   * @param actor
   * @return
   */
  private static boolean isGlobalActor(ActorDAOIF actor)
  {
    return RoleDAO.PUBLIC_ROLE_ID.equals(actor.getOid()) || RoleDAO.OWNER_ID.equals(actor.getOid());
  }

  /**
   * Registers a new listeners
   * 
   * @param entity
   */
  public static synchronized void register(PermissionEntity entity)
  {
    PermissionObserver manager = getManager();
    List<PermissionEntity> listeners = manager.getListeners();

    if (!listeners.contains(entity))
    {
      listeners.add(entity);
    }
  }

  /**
   * Unregisters an existing listener
   * 
   * @param entity
   */
  public static synchronized void unregister(PermissionEntity entity)
  {
    PermissionObserver manager = getManager();
    List<PermissionEntity> listeners = manager.getListeners();

    listeners.remove(entity);
  }

  /**
   * Returns the size of the listeners array. This method exists for testing
   * purposes only.
   * 
   * @return
   */
  static synchronized int size()
  {
    PermissionObserver manager = getManager();

    return manager.getListeners().size();
  }
}
