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

import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.io.dataDefinition.TimestampHandler.Action;

/**
 * Parses the {@link XMLTags#DELETE_TAG}.
 * 
 * @author Justin Smethie
 */
public class DeleteHandler extends TagHandler implements TagHandlerIF
{
  /**
   * @param dispatcher
   * @param manager
   *          TODO
   */
  public DeleteHandler(ImportManager manager)
  {
    super(manager);

    // Setup default dispatching
    this.addHandler(XMLTags.TIMESTAMP_TAG, new TimestampHandler(manager, Action.DELETE));
    this.addHandler(XMLTags.OBJECT_TAG, new DeleteEntityHandler(manager, XMLTags.OBJECT_TAG));
    this.addHandler(XMLTags.RELATIONSHIP_TAG, new DeleteEntityHandler(manager, XMLTags.RELATIONSHIP_TAG));
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.io.dataDefinition.TagHandler#processTag(java.lang.String, org.xml.sax.Attributes, com.runwaysdk.dataaccess.io.XMLHandler, com.runwaysdk.dataaccess.io.ImportManager)
   */
  @Override
  public void onStartElement(String qName, Attributes attributes, TagContext context)
  {
    this.getManager().enterDeleteState();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.io.dataDefinition.TagHandler#onEndElement(java.lang.String, java.lang.String, java.lang.String)
   */
  @Override
  public void onEndElement(String uri, String qName, String name, TagContext context)
  {
    this.getManager().leavingCurrentState();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.io.dataDefinition.TagHandler#modifiesState(java.lang.String)
   */
  @Override
  public boolean modifiesState(String qName)
  {
    return qName.equals(XMLTags.DELETE_TAG);
  }
}
