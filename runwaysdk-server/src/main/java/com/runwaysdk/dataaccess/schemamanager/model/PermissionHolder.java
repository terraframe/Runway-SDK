/**
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
 */
package com.runwaysdk.dataaccess.schemamanager.model;

import org.xml.sax.Attributes;

import com.runwaysdk.dataaccess.io.dataDefinition.XMLTags;

public class PermissionHolder extends KeyedElement implements ElementListener
{
  public PermissionHolder(Attributes attributes, String tag)
  {
    super(attributes, tag);
  }

  public <T> T accept(SchemaVisitor<T> v)
  {
    return v.visit(this);
  }

  @Override
  public PermissionActionElement getChild(Attributes attributes, String tag)
  {
    return (PermissionActionElement) super.getChild(attributes, tag);
  }

  @Override
  public PermissionActionElement getChild(SchemaElementIF child)
  {
    return (PermissionActionElement) super.getChild(child);
  }

  @Override
  public PermissionActionElement getChild(String tag)
  {
    return (PermissionActionElement) super.getChild(tag);
  }

  public PermissionActionElement getChild(String tag, String key)
  {
    return (PermissionActionElement) super.getChild(tag, key);
  }

  public String[] getElementsToObserve()
  {
    if (this.getTag().equals(XMLTags.METHOD_TAG))
    {
      String type = this.getXMLAttributeValue(XMLTags.TYPE_ATTRIBUTE);
      String method = this.getXMLAttributeValue(XMLTags.METHOD_NAME_ATTRIBUTE);
      String key = type + "." + method;
      
      return new String[] { key };
    }

    return new String[] {};
  }

  @Override
  public void handleEvent(ElementEvent event)
  {
    if (event instanceof DeleteEvent)
    {
      this.remove();
    }
  }
}
