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
package com.runwaysdk.dataaccess.io.instance;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.runwaysdk.dataaccess.CoreException;
import com.runwaysdk.dataaccess.io.FileReadException;
import com.runwaysdk.dataaccess.io.FileStreamSource;
import com.runwaysdk.dataaccess.io.StreamSource;
import com.runwaysdk.dataaccess.io.StringStreamSource;
import com.runwaysdk.dataaccess.io.XMLParseException;
import com.runwaysdk.dataaccess.resolver.DefaultConflictResolver;
import com.runwaysdk.dataaccess.resolver.IConflictResolver;
import com.runwaysdk.dataaccess.transaction.Transaction;

/**
 * This class performs a full import of Runway object instances (and their
 * attributes) using our custom XML syntax.
 */
public class InstanceImporter extends XMLHandlerWithResolver
{
  public InstanceImporter(StreamSource source, String schemaLocation, IConflictResolver resolver) throws SAXException, ParserConfigurationException
  {
    super(source, schemaLocation, resolver);

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
  public void startElement(String namespaceURI, String localName, String qName, Attributes attributes) throws SAXException
  {
    if (qName.equals(XMLTags.OBJECT_TAG))
    {
      InstanceHandler iHandler = new InstanceHandler(reader, this, manager, this.getResolver(), attributes);
      reader.setContentHandler(iHandler);
      reader.setErrorHandler(iHandler);
    }
    else if (qName.equals(XMLTags.RELATIONSHIP_TAG))
    {
      RelationshipHandler handler = new RelationshipHandler(reader, this, manager, this.getResolver(), attributes);
      reader.setContentHandler(handler);
      reader.setErrorHandler(handler);
    }
  }

  /**
   * Inherits from Contenthandler If a tag is in scope remove the tag from the
   * scope (non-Javadoc)
   * 
   * @see org.xml.sax.ContentHandler#endElement(java.lang.String,
   *      java.lang.String, java.lang.String)
   */
  public void endElement(String namespaceURI, String localName, String qName) throws SAXException
  {

  }

  /**
   * Parses an XML document valid with the datatype.xsd schema
   * 
   * @param path
   *          The path of the XML document to parse
   * @throws SAXException
   * @throws IOException
   */
  public void readDataTypes(String path) throws SAXException, IOException
  {
    reader.setContentHandler(this);
    reader.setErrorHandler(this);

    reader.parse(path);
  }

  @Transaction
  public synchronized static void runImport(String xml, URL schemaLocation, IConflictResolver resolver)
  {
    try
    {
      String location = schemaLocation.toString();

      InstanceImporter importer = new InstanceImporter(new StringStreamSource(xml.trim()), location, resolver);
      importer.begin();
    }
    catch (SAXException | ParserConfigurationException e)
    {
      throw new CoreException(e);
    }
  }

  /**
   * Imports to the database the data of an XML document according to the
   * instance.xsd XML schema
   * 
   * @param xml
   *          The .xml source
   * @param resolver
   *          TODO
   * @param schmeaLocation
   *          The schema location
   */
  @Transaction
  public synchronized static void runImport(String xml, String schemaLocation, IConflictResolver resolver)
  {
    try
    {
      if (schemaLocation != null && !schemaLocation.contains("classpath:"))
      {
        File file = new File(schemaLocation);
        try
        {
          schemaLocation = file.toURI().toURL().toString();
        }
        catch (MalformedURLException e)
        {
          throw new FileReadException(file, e);
        }
      }

      InstanceImporter importer = new InstanceImporter(new StringStreamSource(xml.trim()), schemaLocation, resolver);
      importer.begin();
    }
    catch (SAXException | ParserConfigurationException e)
    {
      throw new XMLParseException(e);
    }
  }

  /**
   * 
   * @param file
   *          The xml file or directory containing xml files to import.
   * @param schemaLocation
   *          The location of the .xsd document
   * @param resolver
   *          TODO
   */
  @Transaction
  public synchronized static void runImport(File file, String schemaLocation, IConflictResolver resolver)
  {
    // The xml file can either be a directory or a file. If its a directory
    // we're going to run a separate import for every file.
    if (file.isDirectory())
    {
      File[] files = file.listFiles();

      if (files != null)
      {
        for (File file2 : files)
        {
          if (file2.isFile() && file2.getName().endsWith(".xml"))
          {
            System.out.println("Importing " + file2.getAbsolutePath());
            runImport(file2, schemaLocation, resolver);
          }
        }
      }

      return;
    }

    try
    {
      if (schemaLocation != null && !schemaLocation.contains("classpath:"))
      {
        File schema = new File(schemaLocation);
        try
        {
          schemaLocation = schema.toURI().toURL().toString();
        }
        catch (MalformedURLException e)
        {
          throw new FileReadException(schema, e);
        }
      }

      InstanceImporter importer = new InstanceImporter(new FileStreamSource(file), schemaLocation, resolver);
      importer.begin();
    }
    catch (SAXException | ParserConfigurationException e)
    {
      throw new XMLParseException(e);
    }
  }

  @Transaction
  public synchronized static void runImport(File file, URL schemaLocation, IConflictResolver resolver)
  {
    try
    {
      String location;
      location = schemaLocation.toString();

      InstanceImporter importer = new InstanceImporter(new FileStreamSource(file), location, resolver);
      importer.begin();
    }
    catch (SAXException | ParserConfigurationException e)
    {
      throw new XMLParseException(e);
    }
  }

  public static void main(String[] args)
  {
    if (args.length != 2 && args.length != 1)
    {
      String msg = "Please include the arguments 1) the .xml and 2) the location of the .xsd";

      throw new RuntimeException(msg);
    }

    InstanceImporter.runImport(args[0], args[1], new DefaultConflictResolver());
  }
}
