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
package com.runwaysdk.dataaccess.schemamanager.xml;

import org.xml.sax.Attributes;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import com.runwaysdk.dataaccess.io.dataDefinition.XMLTags;
import com.runwaysdk.dataaccess.schemamanager.model.MergeSchema;
import com.runwaysdk.dataaccess.schemamanager.model.PermissionActionElement;
import com.runwaysdk.dataaccess.schemamanager.model.PermissionElement;
import com.runwaysdk.dataaccess.schemamanager.model.PermissionHolder;
import com.runwaysdk.dataaccess.schemamanager.model.SchemaElementIF;
import com.runwaysdk.dataaccess.schemamanager.xml.PermissionActionElementHandler.Action;

/**
 * Generic handler for all elements which are under a {@link PermissionHolder}
 * tag. The behavior of this handler differs depending on which action it is
 * performing. When performing a {@link Action}.GRANT action it behaves like
 * {@link SMKeyedElementHandler}. When performing a {@link Action}.REVOKE
 * action the handler first determines if an equal grant permission already
 * exists, if so the grant and revoke permission and are both removed from the
 * merged schema.
 * 
 * @author Justin Smethie
 */
public class PermissionElementHandler extends AbstractSchemaHandler
{
  /**
   * The {@link PermissionHolder} on which permissions are being set
   */
  private PermissionHolder permissionHolder;

  private Action           action;

  public PermissionElementHandler(Attributes attributes, XMLReader reader, DefaultHandler parentHandler, MergeSchema schema, String tag, Action action, PermissionHolder permissionHolder)
  {
    super(schema, reader, parentHandler, tag, attributes);

    this.action = action;
    this.permissionHolder = permissionHolder;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.schemamanager.xml.AbstractSchemaHandler#
   * dispatchChildHandler(org.xml.sax.Attributes, java.lang.String)
   */
  @Override
  protected void dispatchChildHandler(Attributes attributes, String tag)
  {
    childHandler = new PermissionElementHandler(attributes, reader, this, schema, tag, action, permissionHolder);
    reader.setContentHandler(childHandler);
    reader.setErrorHandler(childHandler);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.schemamanager.xml.AbstractSchemaHandler#
   * initRootElement(org.xml.sax.Attributes, java.lang.String)
   */
  @Override
  protected void initRootElement(Attributes attributes, String tag)
  {
    // Attempt to find the element in the list of children of the parent
    // element. The parent element is retrieved from the parent handler.
    SchemaElementIF parent = ( (AbstractSchemaHandler) parentHandler ).builtElement();
    PermissionElement child = (PermissionElement) parent.getChild(attributes, tag);

    if (child != null)
    {
      // If the element was found in the list of children of its parent, then
      // do not create a new child element. Instead, just replace the
      // attributes of the existing elements
      root = child;
      root.addAttributesWithReplacement(attributes);
    }
    else
    {
      // If the child was not found in the list of children of its parent,
      // initialize a new child.
      root = new PermissionElement(attributes, tag, parent);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.schemamanager.xml.AbstractSchemaHandler#
   * obtainChildElement()
   */
  @Override
  protected void obtainChildElement()
  {
    if (childHandler != null)
    {
      PermissionElement childElement = (PermissionElement) childHandler.builtElement();

      if (action.equals(Action.GRANT))
      {
        obtainGrantChildElement(childElement);
      }
      else
      {
        obtainRevokeChildElement(childElement);
      }
    }
  }

  /**
   * Handles the revoke action when adding a child element
   * @param childElement TODO
   */
  private void obtainRevokeChildElement(PermissionElement childElement)
  {
    PermissionActionElement element = permissionHolder.getChild(XMLTags.GRANT_TAG);

    // FIRST: Determine if a grant permission already exists for the child
    if (element != null)
    {
      element.removeDescendant(childElement);
    }

    // Do not add children of the revoke tag which are not an operation tag
    // and do not have any children. These tags are only used to determine
    // what object to udpate permissions on, however if they do not have any
    // children then they have no purpose in the merged schema.
    if (!childElement.getTag().equals(XMLTags.OPERATION_TAG) && !childElement.hasChildren())
    {
      childElement.setExport(false);
    }

    // FINALLY:
    // Only add the child if the revoke permission was not merged with a grant
    // permission.
    if (childElement.isExport()|| childElement.hasChildren())
    {
      schema.registerDependency(childElement);
      root.addChild(childElement);
    }
  }

  /**
   * Handles the grant action when adding a child element
   * @param childElement TODO
   */
  private void obtainGrantChildElement(PermissionElement childElement)
  {
    PermissionActionElement element = permissionHolder.getChild(XMLTags.REVOKE_TAG);

    // FIRST: Determine if a revoke permission already exists for the child
    if (element != null)
    {
      element.removeDescendant(childElement);
    }

    // Add the most recently created child to the children set of this element
    schema.registerDependency(childElement);
    root.addChild(childElement);
  }
}
