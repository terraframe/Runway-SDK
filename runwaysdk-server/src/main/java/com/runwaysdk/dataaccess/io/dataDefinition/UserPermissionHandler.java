/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.dataaccess.io.dataDefinition;

import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import org.xml.sax.Attributes;

import com.runwaysdk.business.rbac.ActorDAO;
import com.runwaysdk.business.rbac.RoleDAO;
import com.runwaysdk.business.rbac.RoleDAOIF;
import com.runwaysdk.business.rbac.UserDAO;
import com.runwaysdk.business.rbac.UserDAOIF;
import com.runwaysdk.dataaccess.io.ImportManager;

public class UserPermissionHandler extends TagHandler implements TagHandlerIF, HandlerFactoryIF
{
  private static class AssignedRoleHandler extends TagHandler implements TagHandlerIF, HandlerFactoryIF
  {
    public AssignedRoleHandler(ImportManager manager)
    {
      super(manager);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.runwaysdk.dataaccess.io.dataDefinition.TagHandler#onStartElement(java.lang.String, org.xml.sax.Attributes, com.runwaysdk.dataaccess.io.dataDefinition.TagContext)
     */
    @SuppressWarnings("unchecked")
    @Override
    public void onStartElement(String localName, Attributes attributes, TagContext context)
    {
      List<ActorDAO> actors = (List<ActorDAO>) context.getObject("actors");

      String roleName = attributes.getValue(XMLTags.ROLENAME_ATTRIBUTE);
      RoleDAOIF roleIF = RoleDAO.findRole(roleName);

      for (ActorDAO actor : actors)
      {
        if (roleIF != null)
        {
          RoleDAO role = roleIF.getBusinessDAO();
          role.assignMember((UserDAO) actor);
        }
      }
    }
  }

  public UserPermissionHandler(ImportManager manager)
  {
    super(manager);

    this.addHandler(XMLTags.GRANT_TAG, new PermissionActionHandler(manager, PermissionAction.GRANT));
    this.addHandler(XMLTags.REVOKE_TAG, new PermissionActionHandler(manager, PermissionAction.REVOKE));
    this.addHandler(XMLTags.ASSIGNED_ROLE_TAG, new AssignedRoleHandler(manager));
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.io.dataDefinition.TagHandler#onStartElement(java.lang.String, org.xml.sax.Attributes, com.runwaysdk.dataaccess.io.dataDefinition.TagContext)
   */
  @Override
  public void onStartElement(String localName, Attributes attributes, TagContext context)
  {
    List<ActorDAO> actors = new LinkedList<ActorDAO>();
    String usernames = attributes.getValue(XMLTags.USERNAME_ATTRIBUTE);

    StringTokenizer toke = new StringTokenizer(usernames, ",");

    while (toke.hasMoreTokens())
    {
      String username = toke.nextToken().trim();

      UserDAOIF user = UserDAO.findUser(username);

      if (user != null)
      {
        actors.add(user.getBusinessDAO());
      }
    }

    context.setObject("actors", actors);
  }
}
