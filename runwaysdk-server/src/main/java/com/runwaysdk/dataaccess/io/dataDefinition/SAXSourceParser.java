/**
 * 
 */
package com.runwaysdk.dataaccess.io.dataDefinition;

import java.util.Stack;

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

  protected Locator             locator;

  /**
   * Manages the xml import
   */
  protected ImportManager       manager;

  private StreamSource          streamSource;

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

    this.init(source, schemaLocation, plugins);
  }

  public SAXSourceParser(StreamSource source, String schemaLocation, XMLFilter filter, ImportPluginIF... plugins) throws SAXException
  {
    filter.setParent(this.createReader());
    this.reader = filter;

    this.init(source, schemaLocation, plugins);
  }

  private void init(StreamSource source, String schemaLocation, ImportPluginIF... plugins) throws SAXException
  {
    this.reader.setFeature(SCHEMA_VALIDATION_FEATURE_ID, true);
    this.reader.setFeature(VALIDATION_FEATURE_ID, true);
    this.reader.setEntityResolver(new RunwayClasspathEntityResolver());

    this.manager = new ImportManager(source, schemaLocation);
    this.streamSource = source;

    this.reader.setContentHandler(this);
    this.reader.setErrorHandler(this);
    this.reader.setProperty(EXTERNAL_SCHEMA_PROPERTY, schemaLocation);
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
  public void begin()
  {
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
        TagContext cContext = new TagContext(localName, attributes, context, cHandler);

        cHandler.onStartElement(localName, attributes, cContext);

        System.out.println("Found handler for tag [" + localName + "]: " + cHandler.getKey());

        this.stack.push(cContext);
      }
      else
      {
        System.out.println("Unknown handler for tag [" + localName + "]");

        this.stack.push(context);
      }
    }
    else
    {
      TagHandlerIF handler = this.manager.getRoot();

      System.out.println("Found handler for tag [" + localName + "]: " + handler.getClass().getName());

      this.stack.push(new TagContext(localName, attributes, null, handler));
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

      current.characters(ch, start, length, context);
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

      current.onEndElement(uri, localName, qName, context);

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

  /**
   * @see org.xml.sax.ErrorHandler#warning(org.xml.sax.SAXParseException)
   */
  @Override
  public void warning(SAXParseException exception)
  {
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
