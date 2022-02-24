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

import org.xml.sax.Attributes;

import com.runwaysdk.constants.MdTypeInfo;
import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.metadata.MdTypeDAO;

/**
 * Parses the {@link XMLTags#MESSAGES_TAG}, {@link XMLTags#STUB_SOURCE_TAG} and {@link XMLTags#DTO_STUB_SOURCE_TAG} tags.
 * 
 * @author Justin Smethie
 */
public class SourceHandler extends TagHandler implements TagHandlerIF, HandlerFactoryIF
{
  private String attributeName;

  private String tag;

  public SourceHandler(ImportManager manager, String tag, String attributeName)
  {
    super(manager);

    this.tag = tag;
    this.attributeName = attributeName;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.io.dataDefinition.TagHandlerIF#onStartElement(java.lang.String, org.xml.sax.Attributes, com.runwaysdk.dataaccess.io.dataDefinition.TagContext)
   */
  @Override
  public void onStartElement(String localName, Attributes attributes, TagContext context)
  {
    context.setObject(this.attributeName, new StringBuilder());
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.io.dataDefinition.TagHandler#onEndElement(java.lang.String, java.lang.String, java.lang.String, com.runwaysdk.dataaccess.io.dataDefinition.TagContext)
   */
  @Override
  public void onEndElement(String uri, String localName, String name, TagContext context)
  {
    if (localName.equals(this.tag))
    {
      MdTypeDAO mdType = (MdTypeDAO) context.getObject(MdTypeInfo.CLASS);
      StringBuilder builder = (StringBuilder) context.getObject(this.attributeName);

      // Remove all white spaces before and after the text
      String attributeValue = builder.toString().trim();

      mdType.setValue(this.attributeName, attributeValue);
      mdType.apply();
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.io.dataDefinition.TagHandler#characters(char[], int, int, com.runwaysdk.dataaccess.io.dataDefinition.TagContext)
   */
  @Override
  public void characters(char[] ch, int start, int length, TagContext context)
  {
    StringBuilder builder = (StringBuilder) context.getObject(this.attributeName);

    builder.append(new String(ch, start, length));
  }
}
