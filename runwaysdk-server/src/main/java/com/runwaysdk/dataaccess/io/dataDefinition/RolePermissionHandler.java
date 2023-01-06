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
package com.runwaysdk.dataaccess.io.dataDefinition;

import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import org.xml.sax.Attributes;

import com.runwaysdk.business.rbac.ActorDAO;
import com.runwaysdk.business.rbac.RoleDAO;
import com.runwaysdk.business.rbac.RoleDAOIF;
import com.runwaysdk.dataaccess.io.ImportManager;

public class RolePermissionHandler extends TagHandler implements TagHandlerIF, HandlerFactoryIF
{
  private static class SuperRoleHandler extends TagHandler implements TagHandlerIF, HandlerFactoryIF
  {
    public SuperRoleHandler(ImportManager manager)
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
    public void onStartElement(String qName, Attributes attributes, TagContext context)
    {
      List<ActorDAO> actors = (List<ActorDAO>) context.getObject("actors");

      String superRolename = attributes.getValue(XMLTags.ROLENAME_ATTRIBUTE);
      RoleDAOIF superRole = RoleDAO.findRole(superRolename);

      for (ActorDAO actor : actors)
      {
        if (superRole != null)
        {
          ( (RoleDAO) actor ).addAscendant(superRole);
        }
      }
    }
  }

  public RolePermissionHandler(ImportManager manager)
  {
    super(manager);

    this.addHandler(XMLTags.GRANT_TAG, new PermissionActionHandler(manager, PermissionAction.GRANT));
    this.addHandler(XMLTags.REVOKE_TAG, new PermissionActionHandler(manager, PermissionAction.REVOKE));
    this.addHandler(XMLTags.SUPER_ROLE_TAG, new SuperRoleHandler(manager));
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.io.dataDefinition.TagHandler#onStartElement(java.lang.String, org.xml.sax.Attributes, com.runwaysdk.dataaccess.io.dataDefinition.TagContext)
   */
  @Override
  public void onStartElement(String qName, Attributes attributes, TagContext context)
  {
    List<ActorDAO> actors = new LinkedList<ActorDAO>();

    String rolenames = attributes.getValue(XMLTags.ROLENAME_ATTRIBUTE);

    StringTokenizer toke = new StringTokenizer(rolenames, ",");

    while (toke.hasMoreTokens())
    {
      String rolename = toke.nextToken().trim();

      RoleDAOIF role = RoleDAO.findRole(rolename);

      if (role != null)
      {
        actors.add(role.getBusinessDAO());
      }
    }

    context.setObject("actors", actors);
  }
}
