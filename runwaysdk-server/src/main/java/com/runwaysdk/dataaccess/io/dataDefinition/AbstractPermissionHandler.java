/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved.
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
 ******************************************************************************/
package com.runwaysdk.dataaccess.io.dataDefinition;

import java.util.List;

import org.xml.sax.XMLReader;

import com.runwaysdk.business.rbac.ActorDAO;
import com.runwaysdk.business.rbac.Operation;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdRelationshipDAOIF;
import com.runwaysdk.dataaccess.MetadataDAOIF;
import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.io.XMLHandler;

public abstract class AbstractPermissionHandler extends XMLHandler
{
  /**
   * The BusinessDAO created by the metadata
   */
  protected List<ActorDAO>   actors;

  /**
   * Enumeration of the desired permission action
   */
  protected PermissionAction action;

  public AbstractPermissionHandler(XMLReader reader, XMLHandler previousHandler, ImportManager manager, List<ActorDAO> actor, PermissionAction action)
  {
    super(reader, previousHandler, manager);

    this.actors = actor;
    this.action = action;
  }

  protected void setPermission(Operation operation, String id)
  {
    for (ActorDAO actor : actors)
    {
      if (action.equals(PermissionAction.GRANT))
      {
        actor.grantPermission(operation, id);
      }
      else if (action.equals(PermissionAction.REVOKE))
      {
        actor.revokePermission(operation, id);
      }
    }
  }

  protected void setPermission(String operationName, MdClassDAOIF mdClass, MetadataDAOIF metadata)
  {
    if (operationName.equals(XMLTags.ALL))
    {
      this.setPermission(Operation.CREATE, metadata.getId());
      this.setPermission(Operation.DELETE, metadata.getId());
      this.setPermission(Operation.READ, metadata.getId());
      this.setPermission(Operation.READ_ALL, metadata.getId());
      this.setPermission(Operation.WRITE, metadata.getId());
      this.setPermission(Operation.WRITE_ALL, metadata.getId());

      if (mdClass instanceof MdRelationshipDAOIF)
      {
        this.setPermission(Operation.ADD_CHILD, metadata.getId());
        this.setPermission(Operation.ADD_PARENT, metadata.getId());
        
        this.setPermission(Operation.DELETE_CHILD, metadata.getId());
        this.setPermission(Operation.DELETE_PARENT, metadata.getId());
        
        this.setPermission(Operation.READ_CHILD, metadata.getId());
        this.setPermission(Operation.READ_PARENT, metadata.getId());
        
        this.setPermission(Operation.WRITE_CHILD, metadata.getId());
        this.setPermission(Operation.WRITE_PARENT, metadata.getId());
      }
    }
    else if (operationName.equals(XMLTags.READ_ALL_ATTRIBUTES))
    {
      this.setPermission(Operation.READ_ALL, metadata.getId());
    }
    else if (operationName.equals(XMLTags.WRITE_ALL_ATTRIBUTES))
    {
      this.setPermission(Operation.WRITE_ALL, metadata.getId());
    }
    else
    {
      Operation operation = Operation.valueOf(operationName);

      this.setPermission(operation, metadata.getId());
    }
  }

}
