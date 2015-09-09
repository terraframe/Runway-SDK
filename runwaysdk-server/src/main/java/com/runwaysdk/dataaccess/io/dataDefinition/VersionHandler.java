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

import java.io.File;

import org.xml.sax.SAXException;
import org.xml.sax.XMLFilter;

import com.runwaysdk.constants.XMLConstants;
import com.runwaysdk.dataaccess.io.FileStreamSource;
import com.runwaysdk.dataaccess.io.StreamSource;
import com.runwaysdk.dataaccess.io.XMLParseException;
import com.runwaysdk.dataaccess.transaction.Transaction;

public class VersionHandler extends SAXSourceParser
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

  public VersionHandler(StreamSource source, String schemaLocation, ImportPluginIF... plugins) throws SAXException
  {
    super(source, schemaLocation, plugins);
  }

  public VersionHandler(StreamSource source, String schemaLocation, XMLFilter filter, ImportPluginIF... plugins) throws SAXException
  {
    super(source, schemaLocation, filter, plugins);
  }

  @Transaction
  public synchronized static void runImport(File file, Action action)
  {
    VersionHandler.runImport(file, action, VERSION_XSD);
  }

  /**
   * Imports to the database the data of an XML document according to the datatype.xsd XML schema
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
        /*
         * IMPORTANT: This block of code prevents backward compatibility issues where classpath xsd defitions start with '/' xsd defitions which start instead of classpath:
         */
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

        VersionHandler handler = new VersionHandler(new FileStreamSource(file), location, new VersionPlugin(action));
        handler.begin();
      }
      else if (xsd != null && xsd.startsWith("classpath:"))
      {
        /*
         * Just pass the xsd right on through. We have a custom entity resolver (RunwayClasspathEntityResolver.java) which will check the classpath. This is a better place to check the classpath
         * because it works with imports in the xml file as well.
         */
        new VersionHandler(new FileStreamSource(file), xsd, new VersionPlugin(action)).begin();
      }
      else if (xsd == null)
      {
        new VersionHandler(new FileStreamSource(file), null, new VersionPlugin(action)).begin();
      }
    }
    catch (SAXException e)
    {
      throw new XMLParseException(e);
    }
  }
}
