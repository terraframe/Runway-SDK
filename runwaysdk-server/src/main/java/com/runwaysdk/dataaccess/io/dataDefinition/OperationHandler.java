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
/**
 * 
 */
package com.runwaysdk.dataaccess.io.dataDefinition;

import java.util.List;

import org.xml.sax.Attributes;

import com.runwaysdk.business.rbac.ActorDAO;
import com.runwaysdk.business.rbac.Operation;
import com.runwaysdk.constants.MdTypeInfo;
import com.runwaysdk.constants.MetadataInfo;
import com.runwaysdk.dataaccess.MdRelationshipDAOIF;
import com.runwaysdk.dataaccess.MdTypeDAOIF;
import com.runwaysdk.dataaccess.MetadataDAOIF;
import com.runwaysdk.dataaccess.io.ImportManager;

public class OperationHandler extends TagHandler implements TagHandlerIF, HandlerFactoryIF
{
  /**
     * 
     */
  public OperationHandler(ImportManager manager)
  {
    super(manager);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.io.dataDefinition.TagHandler#onStartElement(java.lang.String, org.xml.sax.Attributes, com.runwaysdk.dataaccess.io.dataDefinition.TagContext)
   */
  @Override
  public void onStartElement(String localName, Attributes attributes, TagContext context)
  {
    String operationName = attributes.getValue(XMLTags.NAME_ATTRIBUTE);

    this.setPermission(operationName, context);
  }

  @SuppressWarnings("unchecked")
  protected List<ActorDAO> getActors(TagContext context)
  {
    return (List<ActorDAO>) context.getObject("actors");
  }

  protected MetadataDAOIF getMetadata(TagContext context)
  {
    return (MetadataDAOIF) context.getObject(MetadataInfo.CLASS);
  }

  protected MdTypeDAOIF getMdType(TagContext context)
  {
    return (MdTypeDAOIF) context.getObject(MdTypeInfo.CLASS);
  }

  protected PermissionAction getAction(TagContext context)
  {
    return (PermissionAction) context.getObject("action");
  }

  protected void setPermission(Operation operation, String oid, TagContext context)
  {
    List<ActorDAO> actors = this.getActors(context);
    PermissionAction action = this.getAction(context);

    for (ActorDAO actor : actors)
    {
      if (action.equals(PermissionAction.GRANT))
      {
        actor.grantPermission(operation, oid);
      }
      else if (action.equals(PermissionAction.REVOKE))
      {
        actor.revokePermission(operation, oid);
      }
    }
  }

  protected void setPermission(String operationName, TagContext context)
  {
    MetadataDAOIF metadata = this.getMetadata(context);
    MdTypeDAOIF mdClass = this.getMdType(context);

    if (operationName.equals(XMLTags.ALL))
    {
      this.setPermission(Operation.CREATE, metadata.getOid(), context);
      this.setPermission(Operation.DELETE, metadata.getOid(), context);
      this.setPermission(Operation.READ, metadata.getOid(), context);
      this.setPermission(Operation.READ_ALL, metadata.getOid(), context);
      this.setPermission(Operation.WRITE, metadata.getOid(), context);
      this.setPermission(Operation.WRITE_ALL, metadata.getOid(), context);

      if (mdClass instanceof MdRelationshipDAOIF)
      {
        this.setPermission(Operation.ADD_CHILD, metadata.getOid(), context);
        this.setPermission(Operation.ADD_PARENT, metadata.getOid(), context);

        this.setPermission(Operation.DELETE_CHILD, metadata.getOid(), context);
        this.setPermission(Operation.DELETE_PARENT, metadata.getOid(), context);

        this.setPermission(Operation.READ_CHILD, metadata.getOid(), context);
        this.setPermission(Operation.READ_PARENT, metadata.getOid(), context);

        this.setPermission(Operation.WRITE_CHILD, metadata.getOid(), context);
        this.setPermission(Operation.WRITE_PARENT, metadata.getOid(), context);
      }
    }
    else if (operationName.equals(XMLTags.READ_ALL_ATTRIBUTES))
    {
      this.setPermission(Operation.READ_ALL, metadata.getOid(), context);
    }
    else if (operationName.equals(XMLTags.WRITE_ALL_ATTRIBUTES))
    {
      this.setPermission(Operation.WRITE_ALL, metadata.getOid(), context);
    }
    else
    {
      Operation operation = Operation.valueOf(operationName);

      this.setPermission(operation, metadata.getOid(), context);
    }
  }
}
