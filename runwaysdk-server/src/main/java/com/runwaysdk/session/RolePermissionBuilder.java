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

import java.util.Queue;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

import com.runwaysdk.business.rbac.RoleDAOIF;

public class RolePermissionBuilder extends AbstractPermissionBuilder implements PermissionBuilderIF
{
  public RolePermissionBuilder(RoleDAOIF role)
  {
    super(role);
  }

  public RolePermissionBuilder(RoleDAOIF actor, PermissionManager manager)
  {
    super(actor, manager);
  }

  @Override
  public RoleDAOIF getActor()
  {
    return (RoleDAOIF) super.getActor();
  }

  public PermissionMap build()
  {
    Queue<RoleDAOIF> queue = new LinkedBlockingQueue<RoleDAOIF>();
    queue.add(this.getActor());

    while (!queue.isEmpty())
    {
      RoleDAOIF role = queue.poll();

      Set<RoleDAOIF> roles = role.getSuperRoles();

      for (RoleDAOIF superRole : roles)
      {
        queue.offer(superRole);
      }

      PermissionMap superRoleMap = new PermissionBuilder(role, this.getManager()).build();

      this.map.join(superRoleMap, false);
    }
    
    return this.getMap();
  }
}
