package com.runwaysdk.dataaccess.io.dataDefinition;

import java.io.File;
import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLFilter;

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
 * Imports datatype definitions from an xml document conforming to the datatype.xsd XML schema
 * 
 * @author Justin Smethie
 * @date 6/01/06
 */
public class SAXImporter extends XMLHandler
{
  private Stack<TagContext> stack;

  /**
   * Constructor, creates a xerces XMLReader, enables schema validation
   * 
   * @param source
   * @param schemaLocation
   * @throws SAXException
   */
  public SAXImporter(StreamSource source, String schemaLocation, ImportPluginIF... plugins) throws SAXException
  {
    super(source, schemaLocation);

    this.reader.setContentHandler(this);
    this.reader.setErrorHandler(this);
    this.reader.setProperty(EXTERNAL_SCHEMA_PROPERTY, schemaLocation);
    this.stack = new Stack<TagContext>();

    for (ImportPluginIF plugin : plugins)
    {
      plugin.register(manager);
    }
  }

  public SAXImporter(StreamSource source, String schemaLocation, XMLFilter filter, ImportPluginIF... plugins) throws SAXException
  {
    super(source, schemaLocation, filter);

    this.reader.setContentHandler(this);
    this.reader.setErrorHandler(this);
    this.reader.setProperty(EXTERNAL_SCHEMA_PROPERTY, schemaLocation);
    this.stack = new Stack<TagContext>();

    for (ImportPluginIF plugin : plugins)
    {
      plugin.register(manager);
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
      TagHandlerIF handler = context.getHandler();

      HandlerFactoryIF factory = this.manager.getFactory(context, localName);

      if (factory != null)
      {
        TagHandlerIF cHandler = factory.getHandler(localName, attributes, handler, manager);
        TagContext cContext = new TagContext(localName, attributes, context, cHandler);

        cHandler.onStartElement(localName, attributes, cContext);

        System.out.println("Found handler for tag [" + localName + "]: " + cHandler.getClass().getName());

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
   * Imports a Runway SDK metadata XML file. If the file, xml, contains metadata that has already been imported the metadata will be skipped. FIXME this uses datatype.xsd to validate the file when it
   * should be validating it based on the schema specified in the xml file.
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
   * Imports a Runway SDK metadata XML file. If the file, xml, contains metadata that has already been imported the metadata will be skipped.
   * 
   * @param xml
   *          An absolute path to an XML file, or if prefixed with classpath:
   * @param xsd
   *          Either a valid URL, an absolute or relative file path, or an entity on the classpath prefixed with 'classpath:/'.
   */
  @Transaction
  public synchronized static void runImport(File file, String schemaLocation)
  {
    try
    {
      SAXImporter importer = new SAXImporter(new FileStreamSource(file), schemaLocation, new DefaultPlugin());
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
      SAXImporter importer = new SAXImporter(source, schemaLocation, new DefaultPlugin());
      importer.begin();
    }
    catch (SAXException e)
    {
      throw new XMLParseException(e);
    }
  }

  /**
   * Imports a Runway SDK metadata XML file. If the file, xml, contains metadata that has already been imported the metadata will be skipped. FIXME this uses datatype.xsd to validate the file when it
   * should be validating it based on the schema specified in the xml file.
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
   * Imports a Runway SDK metadata XML file. If the file, xml, contains metadata that has already been imported the metadata will be skipped.
   * 
   * @param xml
   *          An absolute path to an XML file, or if prefixed with classpath:
   * @param xsd
   *          Either a valid URL, an absolute or relative file path, or an entity on the classpath prefixed with 'classpath:/'.
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
      SAXImporter importer = new SAXImporter(new StringStreamSource(xml.trim()), xsd, new DefaultPlugin());
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
