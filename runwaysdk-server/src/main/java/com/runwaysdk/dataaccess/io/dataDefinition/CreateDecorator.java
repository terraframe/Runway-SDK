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

public class CreateDecorator extends TagHandlerDecorator implements TagHandlerIF
{
  public CreateDecorator(TagHandlerIF handler)
  {
    super(handler);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.io.dataDefinition.TagHandlerDecorator#onStartElement(java.lang.String, org.xml.sax.Attributes, com.runwaysdk.dataaccess.io.dataDefinition.TagContext)
   */
  @Override
  public void onStartElement(String qName, Attributes attributes, TagContext context)
  {
    if (qName.equals(XMLTags.CREATE_TAG))
    {
      this.getManager().enterCreateState();
    }
    else
    {
      super.onStartElement(qName, attributes, context);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.io.dataDefinition.TagHandlerIF#onEndElement(java.lang.String, java.lang.String, java.lang.String, com.runwaysdk.dataaccess.io.dataDefinition.TagContext,
   * com.runwaysdk.dataaccess.io.ImportManager)
   */
  @Override
  public void onEndElement(String uri, String qName, String name, TagContext context)
  {
    if (qName.equals(XMLTags.CREATE_TAG))
    {
      this.getManager().leavingCurrentState();
    }
    else
    {
      super.onEndElement(uri, qName, name, context);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.io.dataDefinition.TagHandler#modifiesState(java.lang.String)
   */
  @Override
  public boolean modifiesState(String qName)
  {
    if (qName.equals(XMLTags.CREATE_TAG))
    {
      return true;
    }
    else
    {
      return super.modifiesState(qName);
    }
  }

}
