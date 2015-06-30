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
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.io.XMLHandler;
import com.runwaysdk.dataaccess.metadata.MdEntityDAO;

public abstract class MdEntityHandler extends XMLHandler
{
  /**
   *
   * @param attributes
   *            The attibutes of the class tag
   * @param reader
   *            The XMLReader stream
   * @param previousHandler
   *            The Handler which passed control
   * @param manager
   *            ImportManager which provides communication between handlers for
   *            a single import
   * @param tagType
   *            The type to construct. Can be either enumeration master class or
   *            a regular class.
   */
  public MdEntityHandler(Attributes attributes, XMLReader reader, XMLHandler previousHandler, ImportManager manager, String tagType)
  {
    super(reader, previousHandler, manager);
  }

  /**
   * Parses the attributes tag Inherited from ContentHandler (non-Javadoc)
   *
   * @see org.xml.sax.ContentHandler#startElement(java.lang.String,
   *      java.lang.String, java.lang.String, org.xml.sax.Attributes)
   */
  public abstract void startElement(String namespaceURI, String localName, String fullName, Attributes attributes) throws SAXException;

  /**
   * When the class tag is closed: Returns parsing control back to the Handler
   * which passed control
   *
   * Inherits from ContentHandler (non-Javadoc)
   *
   * @see org.xml.sax.ContentHandler#endElement(java.lang.String,
   *      java.lang.String, java.lang.String)
   */
  public abstract void endElement(String namespaceURI, String localName, String fullName) throws SAXException;

  /**
   *
   * @return
   */
  protected abstract MdEntityDAO getMdEntityDAO();
}
