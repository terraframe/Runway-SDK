/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved.
 * 
 * This file is part of Runway SDK(tm).
 * 
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.runwaysdk.dataaccess.io;

import java.io.IOException;

import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLFilter;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * Parent Handler Class all othere Handlers extend from. Keeps data that is
 * universal to all Handlers.
 * 
 * @author Justin Smethie
 * @date 6/02/06
 */
public class XMLHandler extends DefaultHandler
{
  /**
   * ID to use Xerces SAX parser
   */
  protected static final String READER                       = "org.apache.xerces.parsers.SAXParser";

  /**
   * ID to enable xml validation
   */
  protected static final String VALIDATION_FEATURE_ID        = "http://xml.org/sax/features/validation";

  /**
   * ID to ensure a .xml document is valid with respect to its .xsd document
   */
  protected static final String SCHEMA_VALIDATION_FEATURE_ID = "http://apache.org/xml/features/validation/schema";

  /**
   * Name of the property to set the schema location.
   */
  protected static final String EXTERNAL_SCHEMA_PROPERTY     = "http://apache.org/xml/properties/schema/external-noNamespaceSchemaLocation";

  /**
   * The XMLReader stream
   */
  protected XMLReader           reader;

  /**
   * The handler which control passed from
   */
  protected XMLHandler          previousHandler;

  /**
   * Manages the xml import
   */
  protected ImportManager       manager;

  /**
   * Creates a new XMLHandler to import the given file with the given schema.
   * The status of the import is new.
   * 
   * @param source
   *          The .xml source
   * @param schemaLocation
   *          fully qualified path of the schema file
   * 
   * @throws SAXException
   */
  public XMLHandler(StreamSource source, String schemaLocation) throws SAXException
  {
    this(new ImportManager(source, schemaLocation));
  }

  public XMLHandler(StreamSource source, String schemaLocation, XMLFilter filter) throws SAXException
  {
    this(new ImportManager(source, schemaLocation), filter);
  }

  /**
   * Creates a new XMLHandler to import the given file, schema, and existing
   * status.
   * 
   * @param manager
   *          The manager of the import. Contains the filename, schema location,
   *          and the status of the import
   * @throws SAXException
   */
  public XMLHandler(ImportManager manager) throws SAXException
  {
    reader = createReader();

    reader.setFeature(SCHEMA_VALIDATION_FEATURE_ID, true);
    reader.setFeature(VALIDATION_FEATURE_ID, true);
    reader.setEntityResolver(new RunwayClasspathEntityResolver());

    // Root Handler
    this.previousHandler = null;
    this.manager = manager;
  }

  public XMLHandler(ImportManager manager, XMLFilter filter) throws SAXException
  {
    filter.setParent(this.createReader());
    reader = filter;

    reader.setFeature(SCHEMA_VALIDATION_FEATURE_ID, true);
    reader.setFeature(VALIDATION_FEATURE_ID, true);
    reader.setEntityResolver(new RunwayClasspathEntityResolver());

    // Root Handler
    this.previousHandler = null;
    this.manager = manager;
  }

  protected XMLHandler()
  {

  }

  protected XMLReader createReader() throws SAXException
  {
    return XMLReaderFactory.createXMLReader(READER);
  }

  /**
   * @param reader
   * @param previousHandler
   * @param manager
   */
  public XMLHandler(XMLReader reader, XMLHandler previousHandler, ImportManager manager)
  {
    this.previousHandler = previousHandler;
    this.reader = reader;
    this.manager = manager;
  }

  /**
   * Parses an XML document valid with the datatype.xsd schema
   * 
   * @param path
   *          The path of the XML document to parse
   * @throws SAXException
   */
  public void begin() throws SAXException
  {
    InputSource source = manager.getSource();

    try
    {
      reader.parse(source);
    }
    catch (IOException e)
    {
      throw new XMLParseException(e);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.xml.sax.ContentHandler#setDocumentLocator(org.xml.sax.Locator)
   */
  public void setDocumentLocator(Locator locator)
  {
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.xml.sax.ErrorHandler#warning(org.xml.sax.SAXParseException)
   */
  public void warning(SAXParseException exception)
  {
    throw new XMLParseException(exception);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.xml.sax.ErrorHandler#error(org.xml.sax.SAXParseException)
   */
  public void error(SAXParseException exception)
  {
    throw new XMLParseException(exception);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.xml.sax.ErrorHandler#fatalError(org.xml.sax.SAXParseException)
   */
  public void fatalError(SAXParseException exception) throws SAXException
  {
    throw new XMLParseException(exception);
  }

}
