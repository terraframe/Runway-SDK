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

import java.io.File;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLFilter;
import org.xml.sax.helpers.DefaultHandler;

import com.runwaysdk.configuration.ConfigurationManager.ConfigGroup;
import com.runwaysdk.constants.XMLConstants;
import com.runwaysdk.dataaccess.io.XMLHandler;
import com.runwaysdk.dataaccess.io.XMLParseException;
import com.runwaysdk.dataaccess.transaction.Transaction;

/**
 * Imports datatype definitions from an xml document conforming to the
 * datatype.xsd XML schema
 * 
 * @author Justin Smethie
 * @date 6/01/06
 */
public class SAXImporter extends XMLHandler
{
  /**
   * Constructor, creates a xerces XMLReader, enables schema validation
   * 
   * @param source
   * @param schemaLocation
   * @throws SAXException
   */
  public SAXImporter(String source, String schemaLocation) throws SAXException
  {
    super(source, schemaLocation);
    reader.setContentHandler(this);
    reader.setErrorHandler(this);
    reader.setProperty(EXTERNAL_SCHEMA_PROPERTY, schemaLocation);
  }
  
  /**
   * Constructor: creates a xerces XMLReader, enables schema validation
   * 
   * @param file File containing the .xml source
   * @param schemaLocation fully qualified path of the schema file
   * @throws SAXException
   */
  public SAXImporter(File file, String schemaLocation) throws SAXException
  {
    super(file, schemaLocation);
    reader.setContentHandler(this);
    reader.setErrorHandler(this);
    reader.setProperty(EXTERNAL_SCHEMA_PROPERTY, schemaLocation);
  }
  
  public SAXImporter (File file, String schemaLocation, XMLFilter filter) throws SAXException {
    super (file, schemaLocation, filter);
    reader.setContentHandler(this);
    reader.setErrorHandler(this);
    reader.setProperty(EXTERNAL_SCHEMA_PROPERTY, schemaLocation);
  }

  /**
   * Inherited from ContentHandler Parses the elements of the root tags and
   * delegates specific parsing to other handlers (non-Javadoc)
   * 
   * @see org.xml.sax.ContentHandler#startElement(java.lang.String,
   *      java.lang.String, java.lang.String, org.xml.sax.Attributes)
   */
  public void startElement(String namespaceURI, String localName, String fullName, Attributes attributes) throws SAXException
  {
    DefaultHandler handler = getHandler(localName, attributes);

    // Pass control of the parsin to the new handler
    if (handler != null)
    {
      reader.setContentHandler(handler);
      reader.setErrorHandler(handler);
    }
  }

  protected DefaultHandler getHandler(String localName, Attributes attributes)
  {
    return createRootHandlerFactory().getHandler(localName, attributes, reader, this, manager);
  }
  
  protected HandlerFactoryIF createRootHandlerFactory() {
    return new RootHandlerFactory();
  }

  /**
   * Imports to the database the data of an XML document according to the
   * datatype.xsd XML schema
   * 
   * @param file
   *            The file name of the XML document
   */
  @Transaction
  public synchronized static void runImport(File file)
  {
    try
    {
      String location = SAXImporter.class.getResource("/" + ConfigGroup.XSD.getPath() + "datatype.xsd").toString();

      SAXImporter importer = new SAXImporter(file, location);
      importer.begin();
    }
    catch (SAXException e)
    {
      throw new XMLParseException(e);
    }
  }
  
  /**
   * Imports to the database the data of an XML document according to the
   * datatype.xsd XML schema
   * 
   * @param file
   *            The file name of the XML document
   * @param schemaLocation
   */
  @Transaction
  public synchronized static void runImport(File file, String schemaLocation)
  {
    try
    {
      SAXImporter importer = new SAXImporter(file, schemaLocation);
      importer.begin();
    }
    catch (SAXException e)
    {
      throw new XMLParseException(e);
    }
  }
  
  @Transaction
  public synchronized static void runImport(String xml)
  {
    SAXImporter.runImport(xml, XMLConstants.DATATYPE_XSD);
  }
  
  @Transaction
  public synchronized static void runImport(String xml, String xsd)
  {
    try
    {
      String location = SAXImporter.class.getResource(xsd).toString();
      
      SAXImporter importer = new SAXImporter(xml.trim(), location);
      importer.begin();
    }
    catch (SAXException e)
    {
      throw new XMLParseException(e);
    }
  }    
}
