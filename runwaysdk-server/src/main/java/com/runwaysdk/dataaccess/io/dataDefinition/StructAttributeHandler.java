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
package com.runwaysdk.dataaccess.io.dataDefinition;

import org.xml.sax.Attributes;

import com.runwaysdk.dataaccess.io.ImportManager;

public class StructAttributeHandler extends TagHandler implements TagHandlerIF, HandlerFactoryIF
{
  public static final String STRUCT_ATTRIBUTE_NAME = "STRUCT_ATTRIBUTE_NAME";

  public StructAttributeHandler(ImportManager manager)
  {
    super(manager);

    this.addHandler(XMLTags.ATTRIBUTE_TAG, new AttributeHandler(manager));
    this.addHandler(XMLTags.ATTRIBUTE_STRUCT_TAG, this);
    this.addHandler(XMLTags.ATTRIBUTE_ENUMERATION_TAG, new AttributeEnumerationHandler(manager));
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.io.dataDefinition.TagHandlerIF#onStartElement(java.lang.String, org.xml.sax.Attributes, com.runwaysdk.dataaccess.io.dataDefinition.TagContext)
   */
  @Override
  public void onStartElement(String localName, Attributes attributes, TagContext context)
  {
    String structName = attributes.getValue(XMLTags.ENTITY_ATTRIBUTE_NAME_ATTRIBUTE);

    context.setObject(STRUCT_ATTRIBUTE_NAME, structName);
  }
}
