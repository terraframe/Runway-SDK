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
package com.runwaysdk.dataaccess.io.dataDefinition;

import java.io.File;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.runwaysdk.constants.XMLConstants;
import com.runwaysdk.dataaccess.io.FileStreamSource;
import com.runwaysdk.dataaccess.io.StreamSource;
import com.runwaysdk.dataaccess.io.XMLHandler;
import com.runwaysdk.dataaccess.io.XMLParseException;
import com.runwaysdk.dataaccess.transaction.Transaction;

public class VersionHandler extends XMLHandler
{
  /**
   * List of all possible actions the VersionHandler can take.
   * 
   * @author Justin Smethie
   */
  public enum Action {
    /**
     * Parses only the doIt tag
     */
    DO_IT,

    /**
     * Parses only the undoIt tag
     */
    UNDO_IT;
  }

  /**
   * Path to version.xsd on the classpath
   */
  public static final String VERSION_XSD = XMLConstants.VERSION_XSD;

  /**
   * Action the handler should take
   */
  private Action             action;

  /**
   * Flag indicating if the handler should parse the local names
   */
  private boolean            parse;

  /**
   * Constructor, creates a xerces XMLReader, enables schema validation
   * 
   * @param source
   * @param schemaLocation
   * @param action
   *          Action to perform
   * @throws SAXException
   */
  public VersionHandler(StreamSource source, String schemaLocation, Action action) throws SAXException
  {
    super(source, schemaLocation);

    this.action = action;
    reader.setContentHandler(this);
    reader.setErrorHandler(this);
    reader.setProperty(EXTERNAL_SCHEMA_PROPERTY, schemaLocation);
  }

  public VersionHandler(StreamSource source, Action action) throws SAXException
  {
    super(source, null);

    this.action = action;
    reader.setContentHandler(this);
    reader.setErrorHandler(this);
  }

  @Override
  public void startElement(String namespaceURI, String localName, String fullName, Attributes attributes) throws SAXException
  {
    if ( ( action.equals(Action.DO_IT) && localName.equals(XMLTags.DO_IT_TAG) ) || ( action.equals(Action.UNDO_IT) && localName.equals(XMLTags.UNDO_IT_TAG) ))
    {
      parse = true;
    }

    if (parse)
    {
      XMLHandler handler = new RootHandlerFactory().getHandler(localName, attributes, reader, this, manager);

      // Pass control of the parsin to the new handler
      if (handler != null)
      {
        reader.setContentHandler(handler);
        reader.setErrorHandler(handler);
      }
    }
  }

  @Override
  public void endElement(String uri, String localName, String name) throws SAXException
  {
    if ( ( action.equals(Action.DO_IT) && localName.equals(XMLTags.DO_IT_TAG) ) || ( action.equals(Action.UNDO_IT) && localName.equals(XMLTags.UNDO_IT_TAG) ))
    {
      parse = false;
    }
  }

  @Transaction
  public synchronized static void runImport(File file, Action action)
  {
    VersionHandler.runImport(file, action, VERSION_XSD);
  }

  /**
   * Imports to the database the data of an XML document according to the
   * datatype.xsd XML schema
   * 
   * @param file
   *          The file name of the XML document
   * @param action
   *          Action for the import to perform
   * @param xsd
   *          TODO
   */
  @Transaction
  public synchronized static void runImport(File file, Action action, String xsd)
  {
    try
    {
      if (xsd != null && !xsd.startsWith("classpath:"))
      {
        // TODO FIXME : This logic no longer makes sense since we're appending
        // classpath: to accomplish this.
        if (!xsd.startsWith("/"))
        {
          xsd = "/".concat(xsd);
        }

        java.net.URL resource = VersionHandler.class.getResource(xsd);

        if (resource == null)
        {
          throw new RuntimeException("Unable to find the xsd resource at [" + xsd + "].");
        }

        String location = resource.toString();

        VersionHandler handler = new VersionHandler(new FileStreamSource(file), location, action);
        handler.begin();
      }
      else if (xsd != null && xsd.startsWith("classpath:"))
      {
        // Just pass the xsd right on through. We have a custom entity resolver
        // (RunwayClasspathEntityResolver.java) which will check
        // the classpath. This is a better place to check the classpath because
        // it works with imports in the xml file as well.
        new VersionHandler(new FileStreamSource(file), xsd, action).begin();
      }
      else if (xsd == null)
      {
        new VersionHandler(new FileStreamSource(file), action).begin();
      }
    }
    catch (SAXException e)
    {
      throw new XMLParseException(e);
    }
  }
}
