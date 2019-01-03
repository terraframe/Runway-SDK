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
/**
 * 
 */
package com.runwaysdk.dataaccess.io.dataDefinition;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLFilter;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.io.RunwayClasspathEntityResolver;
import com.runwaysdk.dataaccess.io.StreamSource;
import com.runwaysdk.dataaccess.io.XMLParseException;

public class SAXSourceParser extends DefaultHandler
{
  private static Map<String, ImportPluginIF> pluginMap = new ConcurrentHashMap<String, ImportPluginIF>();

  private static List<SAXSourceParser>       listeners = new LinkedList<SAXSourceParser>();

  public static void registerPlugin(ImportPluginIF plugin)
  {
    pluginMap.put(plugin.getModuleIdentifier(), plugin);

    for (SAXSourceParser listener : listeners)
    {
      plugin.register(listener.getManager());
    }
  }

  public static ImportPluginIF[] plugins(ImportPluginIF... plugins)
  {
    List<ImportPluginIF> list = new LinkedList<ImportPluginIF>();

    for (ImportPluginIF plugin : plugins)
    {
      list.add(plugin);
    }

    list.addAll(pluginMap.values());

    ImportPluginIF[] array = list.toArray(new ImportPluginIF[list.size()]);
    return array;
  }

  /**
   * OID to use Xerces SAX parser
   */
  protected static final String READER                       = "org.apache.xerces.parsers.SAXParser";

  /**
   * OID to enable xml validation
   */
  protected static final String VALIDATION_FEATURE_ID        = "http://xml.org/sax/features/validation";

  /**
   * OID to ensure a .xml document is valid with respect to its .xsd document
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

  protected Locator             locator;

  /**
   * Manages the xml import
   */
  protected ImportManager       manager;

  private Stack<TagContext>     stack;

  /**
   * Creates a new XMLHandler to import the given file with the given schema. The status of the import is new.
   * 
   * @param source
   *          The .xml source
   * @param schemaLocation
   *          fully qualified path of the schema file
   * 
   * @throws SAXException
   */
  public SAXSourceParser(StreamSource source, String schemaLocation, ImportPluginIF... plugins) throws SAXException
  {
    this.reader = createReader();

    this.init(new ImportManager(source, schemaLocation), plugins);
  }

  public SAXSourceParser(StreamSource source, String schemaLocation, XMLFilter filter, ImportPluginIF... plugins) throws SAXException
  {
    filter.setParent(this.createReader());
    this.reader = filter;

    this.init(new ImportManager(source, schemaLocation), plugins);
  }

  public SAXSourceParser(ImportManager manager, ImportPluginIF... plugins) throws SAXException
  {
    this.reader = createReader();

    this.init(manager, plugins);
  }

  private void init(ImportManager manager, ImportPluginIF... plugins) throws SAXException
  {
    this.manager = manager;
    this.reader.setFeature(SCHEMA_VALIDATION_FEATURE_ID, true);
    this.reader.setFeature(VALIDATION_FEATURE_ID, true);
    this.reader.setEntityResolver(new RunwayClasspathEntityResolver());
    this.reader.setContentHandler(this);
    this.reader.setErrorHandler(this);
    this.reader.setProperty(EXTERNAL_SCHEMA_PROPERTY, manager.getSchemaLocation());

    this.stack = new Stack<TagContext>();

    for (ImportPluginIF plugin : plugins)
    {
      plugin.register(manager);
    }
  }

  protected XMLReader createReader() throws SAXException
  {
    return XMLReaderFactory.createXMLReader(READER);
  }

  /**
   * @return the manager
   */
  public ImportManager getManager()
  {
    return this.manager;
  }

  /**
   * Invoked by the SAX parser to set our document locator.
   * 
   * @see org.xml.sax.ContentHandler#setDocumentLocator(org.xml.sax.Locator)
   */
  @Override
  public void setDocumentLocator(Locator locator)
  {
    this.locator = locator;
  }

  public Locator getDocumentLocator()
  {
    return this.locator;
  }

  /**
   * Parses an XML document valid with the datatype.xsd schema
   * 
   * @param path
   *          The path of the XML document to parse
   * @throws XMLParseException
   */
  public final void begin()
  {

    try
    {
      listeners.add(this);

      InputSource source = manager.getSource();

      try
      {
        reader.parse(source);
      }
      catch (XMLParseException e)
      {
        throw e;
      }
      catch (Exception e)
      {
        StreamSource streamSource = manager.getStreamSource();

        if (streamSource != null && e instanceof SAXParseException)
        {
          throw new XMLParseException(streamSource, (SAXParseException) e);
        }
        else if (this.getDocumentLocator() != null)
        {
          throw new XMLParseException(this.getDocumentLocator(), e);
        }
        else
        {
          throw new XMLParseException(e);
        }
      }
    }
    finally
    {
      listeners.remove(this);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
   */
  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
  {
    TagContext context = this.getCurrent();

    if (context != null)
    {
      HandlerFactoryIF factory = this.manager.getFactory(context, localName);

      if (factory != null)
      {
        TagHandlerIF cHandler = factory.getHandler(localName, attributes, context.getHandler(), manager);
        TagContext cContext = this.createContext(localName, attributes, context, cHandler);

        if (this.process(cContext))
        {
          cHandler.onStartElement(localName, attributes, cContext);
        }

        this.stack.push(cContext);
      }
      else
      {
        this.stack.push(context);
      }
    }
    else
    {
      TagHandlerIF cHandler = this.manager.getRoot();
      TagContext cContext = this.createContext(localName, attributes, null, cHandler);

      this.stack.push(cContext);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
   */
  @Override
  public void characters(char[] ch, int start, int length) throws SAXException
  {
    TagContext context = this.getCurrent();

    if (context != null)
    {
      TagHandlerIF current = context.getHandler();

      if (this.process(context))
      {
        current.characters(ch, start, length, context);
      }
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
   */
  @Override
  public void endElement(String uri, String localName, String qName) throws SAXException
  {
    TagContext context = this.getCurrent();

    if (context != null)
    {
      TagHandlerIF current = context.getHandler();

      if (this.process(context))
      {
        current.onEndElement(uri, localName, qName, context);
      }

      this.stack.pop();
    }
  }

  private TagContext getCurrent()
  {
    if (this.stack.size() > 0)
    {
      return this.stack.peek();
    }

    return null;
  }

  protected TagContext createContext(String localName, Attributes attributes, TagContext context, TagHandlerIF cHandler)
  {
    return new TagContext(localName, attributes, context, cHandler);
  }

  /**
   * @param context
   * @return
   */
  protected boolean process(TagContext context)
  {
    return true;
  }

  /**
   * @see org.xml.sax.ErrorHandler#warning(org.xml.sax.SAXParseException)
   */
  @Override
  public void warning(SAXParseException exception)
  {
    StreamSource streamSource = this.manager.getStreamSource();

    if (streamSource != null)
    {
      throw new XMLParseException(streamSource, exception);
    }
    else
    {
      throw new XMLParseException(exception);
    }
  }

  /**
   * @see org.xml.sax.ErrorHandler#error(org.xml.sax.SAXParseException)
   */
  @Override
  public void error(SAXParseException exception)
  {
    StreamSource streamSource = this.manager.getStreamSource();

    if (streamSource != null)
    {
      throw new XMLParseException(streamSource, exception);
    }
    else
    {
      throw new XMLParseException(exception);
    }
  }

  /**
   * @see org.xml.sax.ErrorHandler#fatalError(org.xml.sax.SAXParseException)
   */
  @Override
  public void fatalError(SAXParseException exception) throws SAXException
  {
    StreamSource streamSource = this.manager.getStreamSource();

    if (streamSource != null)
    {
      throw new XMLParseException(streamSource, exception);
    }
    else
    {
      throw new XMLParseException(exception);
    }
  }

}
