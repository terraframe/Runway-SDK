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

import com.runwaysdk.dataaccess.io.ImportManager;

public class HandlerFactory implements HandlerFactoryIF
{
  private Map<String, TagHandlerIF> handlers;

  /**
   * 
   */
  public HandlerFactory()
  {
    this.handlers = new HashMap<String, TagHandlerIF>();

  }

  public void addHandler(String tag, TagHandlerIF handler)
  {
    this.handlers.put(tag, handler);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.io.dataDefinition.HandlerFactoryIF#supports(java.lang.String)
   */
  @Override
  public boolean supports(TagContext context, String qName)
  {
    return this.handlers.containsKey(qName);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.io.dataDefinition.HandlerFactoryIF#getHandler( java.lang.String, org.xml.sax.Attributes, org.xml.sax.XMLReader, com.runwaysdk.dataaccess.io.XMLHandler,
   * com.runwaysdk.dataaccess.io.ImportManager)
   */
  public TagHandlerIF getHandler(String qName, Attributes attributes, TagHandlerIF prev, ImportManager manager)
  {
    return this.handlers.get(qName);
  }

}
