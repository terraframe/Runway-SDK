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
package com.runwaysdk.dataaccess.io.instance;

import java.lang.Thread.UncaughtExceptionHandler;

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.io.XMLHandler;

/**
 * Parses instance_value tags to instiante an Attribute with a value. Makes use
 * of the tempelate pattern to deal with differences when the Attribute belongs
 * to an EntityDAO and an AttributeStruct.
 * 
 * @author Justin Smethie
 * @date 06/09/06
 */
public abstract class ValueHandler extends XMLHandler
{
  /**
   * Keeps track of the text(value) which is parsed.
   */
  private StringBuffer             buffer;

  private UncaughtExceptionHandler exceptionHandler;

  /**
   * Constructor
   * 
   * @param reader
   *          The XMLReader which reads the XML document
   * @param previousHandler
   *          The handler which control passed from
   * @param manager
   *          ImportManager which provides communication between handlers for a
   *          single import
   * @param exceptionHandler
   *          TODO
   * @throws SAXException
   */
  public ValueHandler(XMLReader reader, XMLHandler previousHandler, ImportManager manager, UncaughtExceptionHandler exceptionHandler) throws SAXException
  {
    super(reader, previousHandler, manager);

    this.buffer = new StringBuffer();
    this.exceptionHandler = exceptionHandler;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String,
   * java.lang.String, java.lang.String)
   */
  public void endElement(String uri, String localName, String qName) throws SAXException
  {
    // Remove all white spaces before and after the text
    // Heads up: Smethie fix this. Do not trim the characters.
    String attributeValue = buffer.toString().trim();

    try
    {
      this.setValue(attributeValue);
    }
    catch (Exception e)
    {
      this.exceptionHandler.uncaughtException(Thread.currentThread(), e);
    }

    reader.setContentHandler(previousHandler);
    reader.setErrorHandler(previousHandler);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
   */
  public void characters(char[] ch, int start, int length) throws SAXException
  {
    buffer.append(new String(ch, start, length));
  }

  /**
   * Sets the value of an Attribute.
   * 
   * @param value
   *          The value of the Attribute
   */
  protected abstract void setValue(String value);
}
