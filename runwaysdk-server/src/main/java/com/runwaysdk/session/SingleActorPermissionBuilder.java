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

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.runwaysdk.business.rbac.RoleDAOIF;
import com.runwaysdk.business.rbac.SingleActorDAOIF;
import com.runwaysdk.constants.RelationshipTypes;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.query.RelationshipDAOQuery;

public class SingleActorPermissionBuilder extends AbstractPermissionBuilder implements PermissionBuilderIF
{
  public SingleActorPermissionBuilder(SingleActorDAOIF actor)
  {
    super(actor);
  }

  public SingleActorPermissionBuilder(SingleActorDAOIF actor, PermissionManager manager)
  {
    super(actor, manager);
  }

  @Override
  public SingleActorDAOIF getActor()
  {
    return (SingleActorDAOIF) super.getActor();
  }

  /**
   * Appends the permission set of a given user onto the existing permissions
   * 
   * @param user
   *          The user to append the permissions of
   */
  @Override
  public PermissionMap build()
  {
    SingleActorDAOIF actor = this.getActor();

    this.initializeRolePermissions(actor);

    // Get the permissions for the individual user
    PermissionBuilder builder = new PermissionBuilder(actor, this.getManager());
    PermissionMap actorMap = builder.build();
    
    this.map.join(actorMap, false);

    Set<RoleDAOIF> set = actor.assignedRoles();

    PermissionMap rolePermissions = new PermissionMap();

    // Get the permissions for all the roles the user is part of
    for (RoleDAOIF role : set)
    {
      RolePermissionBuilder roleBuilder = new RolePermissionBuilder(role, this.getManager());
      PermissionMap roleMap = roleBuilder.build();

      rolePermissions.join(roleMap, true);
    }

    this.map.join(rolePermissions, false);
    
    return this.getMap();
  }

  private void initializeRolePermissions(SingleActorDAOIF actor)
  {
    PermissionManager manager = this.getManager();

    String[] ids = this.getIdsToQuery(actor);

    QueryFactory factory = new QueryFactory();

    RelationshipDAOQuery query = factory.relationshipDAOQuery(RelationshipTypes.TYPE_PERMISSION.getType());

    query.WHERE(query.parentId().IN(ids));

    OIterator<RelationshipDAOIF> it = query.getIterator();

    try
    {
      while (it.hasNext())
      {
        RelationshipDAOIF relationship = it.next();

        manager.put(relationship);
      }
    }
    finally
    {
      it.close();
    }
  }

  private String[] getIdsToQuery(SingleActorDAOIF actor)
  {
    List<String> ids = new LinkedList<String>();
    Set<RoleDAOIF> roles = actor.authorizedRoles();

    ids.add(actor.getId());

    for (RoleDAOIF role : roles)
    {
      ids.add(role.getId());
    }

    return ids.toArray(new String[ids.size()]);
  }
}
