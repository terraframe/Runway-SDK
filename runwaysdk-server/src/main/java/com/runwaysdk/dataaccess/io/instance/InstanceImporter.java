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

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.runwaysdk.dataaccess.CoreException;
import com.runwaysdk.dataaccess.io.FileReadException;
import com.runwaysdk.dataaccess.io.XMLParseException;
import com.runwaysdk.dataaccess.resolver.DefaultConflictResolver;
import com.runwaysdk.dataaccess.resolver.IConflictResolver;
import com.runwaysdk.dataaccess.transaction.Transaction;


public class InstanceImporter extends XMLHandlerWithResolver
{
  public InstanceImporter(String source, String schemaLocation, IConflictResolver resolver) throws SAXException
  {
    super(source, schemaLocation, resolver);

    reader.setContentHandler(this);
    reader.setErrorHandler(this);
    reader.setProperty(EXTERNAL_SCHEMA_PROPERTY, schemaLocation);
  }

  public InstanceImporter(File file, String schemaLocation, IConflictResolver resolver) throws SAXException
  {
    super(file, schemaLocation, resolver);

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
    if (localName.equals(XMLTags.OBJECT_TAG))
    {
      InstanceHandler iHandler = new InstanceHandler(reader, this, manager, this.getResolver(), attributes);
      reader.setContentHandler(iHandler);
      reader.setErrorHandler(iHandler);
    }
    else if (localName.equals(XMLTags.RELATIONSHIP_TAG))
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
  public void endElement(String namespaceURI, String localName, String fullName) throws SAXException
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

      InstanceImporter importer = new InstanceImporter(xml, location, resolver);
      importer.begin();
    }
    catch (SAXException e)
    {
      throw new CoreException(e);
    }
  }

  /**
   * Imports to the database the data of an XML document according to the instance.xsd XML schema
   *
   * @param xml The .xml source
   * @param resolver TODO
   * @param schmeaLocation The schema location
   */
  @Transaction
  public synchronized static void runImport(String xml, String schemaLocation, IConflictResolver resolver)
  {
    try
    {
      File file = new File(schemaLocation);
      String location;
      try
      {
        location = file.toURI().toURL().toString();
      }
      catch (MalformedURLException e)
      {
        throw new FileReadException(file, e);
      }

      InstanceImporter importer = new InstanceImporter(xml, location, resolver);
      importer.begin();
    }
    catch (SAXException e)
    {
      throw new XMLParseException(e);
    }
  }

  /**
   *
   * @param file
   *            The file name of the XML document
   * @param schemaLocation
   *            The location of the .xsd document
   * @param resolver TODO
   */
  @Transaction
  public synchronized static void runImport(File file, String schemaLocation, IConflictResolver resolver)
  {
    try
    {
      File schema = new File(schemaLocation);
      String location;
      try
      {
        location = schema.toURI().toURL().toString();
      }
      catch (MalformedURLException e)
      {
        throw new FileReadException(schema, e);
      }

      InstanceImporter importer = new InstanceImporter(file, location, resolver);
      importer.begin();
    }
    catch (SAXException e)
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

      InstanceImporter importer = new InstanceImporter(file, location, resolver);
      importer.begin();
    }
    catch (SAXException e)
    {
      throw new XMLParseException(e);
    }
  }

  public static void main(String[] args)
  {
    if(args.length != 2)
    {
      String msg = "Please include the arguments 1) the .xml and 2) the location of the .xsd";

      throw new RuntimeException(msg);
    }
    
    InstanceImporter.runImport(args[0], args[1], new DefaultConflictResolver());
  }
}
