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
package com.runwaysdk.dataaccess.schemamanager.xml;

import java.io.File;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLFilter;
import org.xml.sax.helpers.DefaultHandler;

import com.runwaysdk.dataaccess.io.FileStreamSource;
import com.runwaysdk.dataaccess.io.XMLParseException;
import com.runwaysdk.dataaccess.io.dataDefinition.SAXImporter;
import com.runwaysdk.dataaccess.schemamanager.model.MergeSchema;

public class SMSAXImporter extends SAXImporter
{
  public SMSAXImporter(File file, String schemaLocation) throws SAXException
  {
    super(new FileStreamSource(file), schemaLocation);
    this.manager = new MergeSchema(new FileStreamSource(file), schemaLocation);

    ( (MergeSchema) manager ).addTimestamp(file);
  }

  public SMSAXImporter(File file, String schemaLocation, MergeSchema manager) throws SAXException
  {
    super(new FileStreamSource(file), schemaLocation);
    this.manager = manager;

    manager.addTimestamp(file);
  }

  public SMSAXImporter(File file, String schemaLocation, MergeSchema manager, XMLFilter filter) throws SAXException
  {
    super(new FileStreamSource(file), schemaLocation, filter);
    this.manager = manager;

    manager.addTimestamp(file);
  }

  public SMSAXImporter(File file, String schemaLocation, XMLFilter filter) throws SAXException
  {
    super(new FileStreamSource(file), schemaLocation, filter);
  }

  protected DefaultHandler getHandler(String localName, Attributes attributes)
  {
    return new SMRootHandlerFactory().getHandler(reader, this, manager, localName, attributes);
  }

  public synchronized static void runImport(File file, String xsdLocation, MergeSchema manager)
  {
    try
    {
      SMSAXImporter importer = new SMSAXImporter(file, xsdLocation, manager);
      importer.begin();

    }
    catch (SAXException e)
    {
      throw new XMLParseException(e);
    }
  }

  public synchronized static void runImport(File file, String xsdLocation, MergeSchema manager, XMLFilter filter)
  {
    try
    {
      SMSAXImporter importer = new SMSAXImporter(file, xsdLocation, manager, filter);
      importer.begin();

    }
    catch (SAXException e)
    {
      throw new XMLParseException(e);
    }
  }

  protected MergeSchema schema()
  {
    return (MergeSchema) manager;
  }

}
