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

import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.io.TimeFormat;

public class TimestampHandler extends TagHandler implements TagHandlerIF
{
  public enum Action {
    CREATE(XMLTags.CREATE_TAG), DELETE(XMLTags.DELETE_TAG);

    private String tag;

    private Action(String tag)
    {
      this.tag = tag;
    }

    public String getTag()
    {
      return tag;
    }
  }

  private Action action;

  /**
   * @param dispatcher
   * @param manager
   *          TODO
   * @param action
   */
  public TimestampHandler(ImportManager manager, Action action)
  {
    super(manager);

    this.action = action;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.io.dataDefinition.TagHandlerIF#onStartElement(java.lang.String, org.xml.sax.Attributes, com.runwaysdk.dataaccess.io.dataDefinition.TagHandlerIF,
   * com.runwaysdk.dataaccess.io.ImportManager)
   */
  @Override
  public void onStartElement(String localName, Attributes attributes, TagContext context)
  {
    if (action.equals(Action.CREATE))
    {
      this.createTimestamp(attributes);
    }
    else
    {
      this.deleteTimestamp(attributes);
    }
  }

  private void deleteTimestamp(Attributes attributes)
  {
    String format = this.getTimestamp(attributes);

    Database.removePropertyValue(Database.VERSION_NUMBER, MdAttributeCharacterInfo.CLASS, format, Database.VERSION_TIMESTAMP_PROPERTY);
  }

  private void createTimestamp(Attributes attributes)
  {
    String format = this.getTimestamp(attributes);

    Database.addPropertyValue(Database.VERSION_NUMBER, MdAttributeCharacterInfo.CLASS, format, Database.VERSION_TIMESTAMP_PROPERTY);
  }

  private String getTimestamp(Attributes attributes)
  {
    String version = attributes.getValue(XMLTags.VERSION_TAG);
    long timestamp = Long.parseLong(version);

    return new TimeFormat(timestamp).format();
  }
}
