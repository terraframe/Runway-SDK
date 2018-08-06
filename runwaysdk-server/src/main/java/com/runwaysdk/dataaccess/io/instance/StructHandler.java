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
package com.runwaysdk.dataaccess.io.instance;

import java.lang.Thread.UncaughtExceptionHandler;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.runwaysdk.constants.ComponentInfo;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.StructDAO;
import com.runwaysdk.dataaccess.attributes.entity.AttributeStruct;
import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.io.XMLHandler;

/**
 * Handles the composition tag in the instance importer. See
 * http:\\www.runwaysdk.com\schema\instance.xsd for the entire importer schema.
 * Sets all the attributes which belong to an AttributeStruct.
 * 
 * @author Justin Smethie
 */
public class StructHandler extends XMLHandler
{
  /**
   * The AttributeStruct which is being imported
   */
  private AttributeStruct          attributeStruct;

  private UncaughtExceptionHandler exceptionHandler;

  /**
   * Creates an instance of a MdAttributeEnumeration.
   * 
   * @param reader
   *          The XMLReader stream
   * @param previousHandler
   *          The XMLHandler in which control was passed from
   * @param manager
   *          ImportManager which provides communication between handlers for a
   *          single import
   * @param entity
   *          The EntityDAO which defines the attribute-multiple oid mapping
   * @param attributes
   *          The attributes of the instance tag
   * @param exceptionHandler
   *          TODO
   * @param structName
   *          The name of the struct attribute in which the selection is part of
   * @param locator
   *          The locator of the XMLReader stream
   */
  public StructHandler(XMLReader reader, XMLHandler previousHandler, ImportManager manager, EntityDAO entity, Attributes attributes, UncaughtExceptionHandler exceptionHandler) throws SAXException
  {
    super(reader, previousHandler, manager);

    String structName = attributes.getValue(XMLTags.ATTRIBUTE_TAG);

    this.exceptionHandler = exceptionHandler;
    this.attributeStruct = (AttributeStruct) entity.getAttributeIF(structName);

    // Overwrite the OID that was generated on this node if the structDAO is new.
    StructDAO structDAO = this.attributeStruct.getStructDAO();
    if (structDAO.isNew())
    {
      String importStructId = attributes.getValue(XMLTags.ID_TAG);

      this.attributeStruct.setValue(importStructId);

      structDAO.getAttribute(ComponentInfo.OID).setValue(importStructId);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String,
   * java.lang.String, java.lang.String, org.xml.sax.Attributes) Passes control
   * to the appropiate handler for INSTANCE_VALUE_TAG and SELECTOR_TAG inside of
   * a COMPOSITION_REF_TAG
   */
  public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
  {
    // Load in an Attribute value of the AttributeStruct
    if (localName.equals(XMLTags.VALUE_TAG))
    {
      ValueHandler handler = new StructValueHandler(reader, this, manager, attributeStruct, attributes, exceptionHandler);
      reader.setContentHandler(handler);
      reader.setErrorHandler(handler);
    }
    // Load in an AttributeEnumeration of the AttributeStruct
    else if (localName.equals(XMLTags.ENUMERATION_TAG))
    {
      EnumerationHandler handler = new StructEnumerationHandler(reader, this, manager, attributeStruct, attributes);
      reader.setContentHandler(handler);
      reader.setErrorHandler(handler);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String,
   * java.lang.String, java.lang.String) Returns parsing control to the parent
   * Handler
   */
  public void endElement(String uri, String localName, String qName) throws SAXException
  {
    reader.setContentHandler(previousHandler);
    reader.setErrorHandler(previousHandler);
  }
}
