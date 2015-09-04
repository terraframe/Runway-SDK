/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.dataaccess.io.dataDefinition;

import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.io.XMLHandler;
import com.runwaysdk.dataaccess.io.XMLParseException;

/**
 * Searches from and defines tags
 * 
 * @author Justin Smethie
 * @date 6/02/06
 */
public class SearchHandler extends XMLHandler
{

  /**
   * If the value to search has been found
   */
  private boolean           defined = false;

  private SearchCriteriaIF  criteria;

  private Stack<TagContext> stack;

  /**
   * Protected constructor, sets passed in value and control of the XMLReader to itself
   * 
   * @param manager
   * @param tags
   *          The xml tags to examine the attribute value
   * @param attribute
   *          The attribute on a xml tag to search
   * @param key
   *          The value of the attribute to search
   * @param cause
   *          TODO
   * @throws SAXException
   */
  protected SearchHandler(ImportManager manager, SearchCriteriaIF criteria, String cause) throws SAXException
  {
    super(manager);

    String schemaLocation = manager.getSchemaLocation();

    this.criteria = criteria;
    this.reader.setContentHandler(this);
    this.reader.setErrorHandler(this);
    this.reader.setProperty(EXTERNAL_SCHEMA_PROPERTY, schemaLocation);
    this.stack = new Stack<TagContext>();

    // Validate the value to search and add it to the search stack
    if (manager.enforceValidation(cause))
    {
      manager.validateSearch(criteria);
    }

    manager.addSearchId(criteria, cause);

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
        boolean parse = ( !defined && criteria.check(localName, attributes) ) || context.isParse();

        TagHandlerIF cHandler = factory.getHandler(localName, attributes, handler, manager);
        TagContext cContext = new TagContext(localName, attributes, context, cHandler);
        cContext.setParse(parse);

        if (cContext.isParse())
        {
          System.out.println("Search : Parsing [" + localName + "]: " + cHandler.getClass().getName());

          cHandler.onStartElement(localName, attributes, cContext);
        }

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

      TagContext cContext = new TagContext(localName, attributes, null, handler);
      cContext.setParse(false);

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

    if (context != null && context.isParse())
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
      if (context.isParse())
      {
        TagHandlerIF handler = context.getHandler();
        handler.onEndElement(uri, localName, qName, context);
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

  /**
   * Static method to seach Entity tags
   * 
   * @param manager
   * @param tags
   * @param attribute
   * @param key
   * @param cause
   *          TODO
   */
  public static void searchEntity(ImportManager manager, String[] tags, String attribute, String key, String cause)
  {
    SearchHandler.searchEntity(manager, new SearchCriteria(tags, attribute, key), cause);
  }

  public static void searchEntity(ImportManager manager, SearchCriteriaIF criteria, String cause)
  {
    try
    {
      SearchHandler searcher = new SearchHandler(manager, criteria, cause);
      searcher.begin();
    }
    catch (SAXException e)
    {
      throw new XMLParseException(e);
    }
  }

}
