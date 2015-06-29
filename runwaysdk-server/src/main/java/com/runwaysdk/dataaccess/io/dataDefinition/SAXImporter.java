/**
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
 */
package com.runwaysdk.dataaccess.io.dataDefinition;

import java.io.File;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLFilter;
import org.xml.sax.helpers.DefaultHandler;

import com.runwaysdk.constants.XMLConstants;
import com.runwaysdk.dataaccess.CoreException;
import com.runwaysdk.dataaccess.cache.globalcache.ehcache.CacheShutdown;
import com.runwaysdk.dataaccess.io.FileStreamSource;
import com.runwaysdk.dataaccess.io.StreamSource;
import com.runwaysdk.dataaccess.io.StringStreamSource;
import com.runwaysdk.dataaccess.io.XMLHandler;
import com.runwaysdk.dataaccess.io.XMLParseException;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.session.Request;

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
  public SAXImporter(StreamSource source, String schemaLocation) throws SAXException
  {
    super(source, schemaLocation);
    reader.setContentHandler(this);
    reader.setErrorHandler(this);
    reader.setProperty(EXTERNAL_SCHEMA_PROPERTY, schemaLocation);
  }

  public SAXImporter(StreamSource source, String schemaLocation, XMLFilter filter) throws SAXException
  {
    super(source, schemaLocation, filter);
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

  protected HandlerFactoryIF createRootHandlerFactory()
  {
    return new RootHandlerFactory();
  }

  /**
   * Imports a Runway SDK metadata XML file. If the file, xml, contains metadata
   * that has already been imported the metadata will be skipped. FIXME this
   * uses datatype.xsd to validate the file when it should be validating it
   * based on the schema specified in the xml file.
   * 
   * @param xml
   *          An absolute or relative path to an XML metadata file.
   */
  @Transaction
  public synchronized static void runImport(File file)
  {
    SAXImporter.runImport(file, XMLConstants.DATATYPE_XSD);
  }

  /**
   * Imports a Runway SDK metadata XML file. If the file, xml, contains metadata
   * that has already been imported the metadata will be skipped.
   * 
   * @param xml
   *          An absolute path to an XML file, or if prefixed with classpath:
   * @param xsd
   *          Either a valid URL, an absolute or relative file path, or an
   *          entity on the classpath prefixed with 'classpath:/'.
   */
  @Transaction
  public synchronized static void runImport(File file, String schemaLocation)
  {
    try
    {
      SAXImporter importer = new SAXImporter(new FileStreamSource(file), schemaLocation);
      importer.begin();
    }
    catch (SAXException e)
    {
      throw new XMLParseException(e);
    }
  }

  @Transaction
  public synchronized static void runImport(StreamSource source, String schemaLocation)
  {
    try
    {
      SAXImporter importer = new SAXImporter(source, schemaLocation);
      importer.begin();
    }
    catch (SAXException e)
    {
      throw new XMLParseException(e);
    }
  }

  /**
   * Imports a Runway SDK metadata XML file. If the file, xml, contains metadata
   * that has already been imported the metadata will be skipped. FIXME this
   * uses datatype.xsd to validate the file when it should be validating it
   * based on the schema specified in the xml file.
   * 
   * @param xml
   *          An absolute or relative path to an XML metadata file.
   */
  @Transaction
  public synchronized static void runImport(String xml)
  {
    SAXImporter.runImport(xml, XMLConstants.DATATYPE_XSD);
  }

  /**
   * Imports a Runway SDK metadata XML file. If the file, xml, contains metadata
   * that has already been imported the metadata will be skipped.
   * 
   * @param xml
   *          An absolute path to an XML file, or if prefixed with classpath:
   * @param xsd
   *          Either a valid URL, an absolute or relative file path, or an
   *          entity on the classpath prefixed with 'classpath:/'.
   */
  @Transaction
  public synchronized static void runImport(String xml, String xsd)
  {
    if (xsd == null)
    {
      xsd = XMLConstants.DATATYPE_XSD;
    }

    try
    {
      SAXImporter importer = new SAXImporter(new StringStreamSource(xml.trim()), xsd);
      importer.begin();
    }
    catch (SAXException e)
    {
      throw new XMLParseException(e);
    }
  }

  /**
   * @param args
   */
  @Request
  private static void run(String[] args)
  {
    if (args.length < 1)
    {
      String errMsg = "At least one argument is required for Versioning:\n" + "  1) Location of the folder containing the schema(version date).xml files\n" + "  2) xsd file to use (optional)";
      throw new CoreException(errMsg);
    }

    String xsd;
    if (args.length == 1)
    {
      xsd = null;
    }
    else
    {
      xsd = args[1];
    }

    SAXImporter.runImport(new File(args[0]), xsd);
  }

  public static void main(String[] args)
  {
    try
    {
      SAXImporter.run(args);
    }
    finally
    {
      CacheShutdown.shutdown();
    }
  }

}
