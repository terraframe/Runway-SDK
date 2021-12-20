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

import java.util.HashMap;
import java.util.Map;

import org.xml.sax.Attributes;

public class TagContext
{
  private String              localName;

  private Attributes          attributes;

  private Map<String, Object> objects;

  private TagContext          parent;

  private TagHandlerIF        handler;

  private boolean             parse;

  /**
   * @param localName
   * @param attributes
   * @param current
   */
  public TagContext(String localName, Attributes attributes, TagContext parent, TagHandlerIF handler)
  {
    this.localName = localName;
    this.attributes = attributes;
    this.parent = parent;
    this.handler = handler;
    this.objects = new HashMap<String, Object>();
    this.parse = true;
  }

  /**
   * @return the localName
   */
  public String getLocalName()
  {
    return localName;
  }

  /**
   * @return the attributes
   */
  public Attributes getAttributes()
  {
    return attributes;
  }

  /**
   * @return the parent
   */
  public TagContext getParent()
  {
    return parent;
  }

  /**
   * @return the handler
   */
  public TagHandlerIF getHandler()
  {
    return handler;
  }

  public void setObject(String key, Object object)
  {
    this.objects.put(key, object);
  }

  public Object getObject(String key)
  {
    if (this.objects.containsKey(key))
    {
      return this.objects.get(key);
    }
    else if (this.parent != null)
    {
      return this.parent.getObject(key);
    }

    return null;
  }

  /**
   * @return the parse
   */
  public boolean isParse()
  {
    return parse;
  }

  /**
   * @param parse
   *          the parse to set
   */
  public void setParse(boolean parse)
  {
    this.parse = parse;
  }
}
