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
import com.runwaysdk.dataaccess.schemamanager.model.PermissionHolder;
import com.runwaysdk.dataaccess.schemamanager.model.SchemaElement;
import com.runwaysdk.dataaccess.schemamanager.model.SchemaElementIF;

public class PermissionActionElementHandler extends AbstractSchemaHandler
{
  enum Action {
    /**
     * Permissions are being added to the xml file
     */
    GRANT(XMLTags.GRANT_TAG),

    /**
     * Permissions are being removed from the xml file
     */
    REVOKE(XMLTags.REVOKE_TAG);

    private String tag;

    private Action(String tag)
    {
      this.tag = tag;
    }

    static Action getAction(String tag)
    {
      for (Action action : Action.values())
      {
        if (action.tag.equals(tag))
        {
          return action;
        }
      }

      return null;
    }
  }

  private Action           action;

  private PermissionHolder permissionHolder;

  public PermissionActionElementHandler(Attributes attributes, XMLReader reader, DefaultHandler parentHandler, MergeSchema schema, String tag, PermissionHolder permissionHolder)
  {
    super(schema, reader, parentHandler, tag, attributes);

    this.action = Action.getAction(tag);
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
    SchemaElementIF child = parent.getChild(attributes, tag);

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
      root = new PermissionActionElement(attributes, tag, parent);
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
      // Add the most recently created child to the children set of this element
      SchemaElement childElement = (SchemaElement) childHandler.builtElement();
      root.addChild(childElement);
    }
  }
}
