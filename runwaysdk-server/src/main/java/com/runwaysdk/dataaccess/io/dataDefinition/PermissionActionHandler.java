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

import org.xml.sax.Attributes;

import com.runwaysdk.dataaccess.io.ImportManager;

public class PermissionActionHandler extends TagHandler implements TagHandlerIF, HandlerFactoryIF
{
  private PermissionAction action;

  public PermissionActionHandler(ImportManager manager, PermissionAction action)
  {
    super(manager);

    this.action = action;

    MdClassPermissionHandler handler = new MdClassPermissionHandler(manager);

    this.addHandler(XMLTags.MD_STRUCT_PERMISSION_TAG, handler);
    this.addHandler(XMLTags.MD_UTIL_PERMISSION_TAG, handler);
    this.addHandler(XMLTags.MD_VIEW_PERMISSION_TAG, handler);
    this.addHandler(XMLTags.MD_BUSINESS_PERMISSION_TAG, new MdBusinessPermissionHandler(manager));
    this.addHandler(XMLTags.MD_RELATIONSHIP_PERMISSION_TAG, new MdRelationshipPermissionHandler(manager));
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.io.dataDefinition.TagHandler#onStartElement(java.lang.String, org.xml.sax.Attributes, com.runwaysdk.dataaccess.io.dataDefinition.TagContext)
   */
  @Override
  public void onStartElement(String localName, Attributes attributes, TagContext context)
  {
    context.setObject("action", this.action);
  }

}