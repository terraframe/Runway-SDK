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
package com.runwaysdk.dataaccess.io.dataDefinition;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.io.TimeFormat;
import com.runwaysdk.dataaccess.io.XMLHandler;

public class TimestampHandler extends XMLHandler
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

  /**
   * Constructor - Creates a MdBusinessDAO BusinessDAO and sets the parameters
   * according to the attributes parse
   * 
   * @param attributes
   *          The attibutes of the class tag
   * @param reader
   *          The XMLReader stream
   * @param previousHandler
   *          The Handler which passed control
   * @param manager
   *          ImportManager which provides communication between handlers for a
   *          single import
   * @param action
   *          TODO
   * @param tagType
   *          The type to construct. Can be either enumeration master class or a
   *          regular class.
   */
  public TimestampHandler(Attributes attributes, XMLReader reader, XMLHandler previousHandler, ImportManager manager, Action action)
  {
    super(reader, previousHandler, manager);

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

  /**
   * When the class tag is closed: Returns parsing control back to the Handler
   * which passed control
   * 
   * Inherits from ContentHandler (non-Javadoc)
   * 
   * @see org.xml.sax.ContentHandler#endElement(java.lang.String,
   *      java.lang.String, java.lang.String)
   */
  public void endElement(String namespaceURI, String localName, String fullName) throws SAXException
  {
    if (localName.equals(XMLTags.TIMESTAMP_TAG))
    {
      reader.setContentHandler(previousHandler);
      reader.setErrorHandler(previousHandler);
    }
  }

}
