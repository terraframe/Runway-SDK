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

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

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
  private boolean          defined = false;

  private SearchCriteriaIF criteria;

  /**
   * Protected constructor, sets passed in value and control of the XMLReader to
   * itself
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

    this.criteria = criteria;

    // Validate the value to search and add it to the search stack
    if (manager.enforceValidation(cause))
    {
      manager.validateSearch(criteria);
    }

    manager.addSearchId(criteria, cause);

    String schemaLocation = manager.getSchemaLocation();
    reader.setContentHandler(this);
    reader.setErrorHandler(this);
    reader.setProperty(EXTERNAL_SCHEMA_PROPERTY, schemaLocation);
  }

  /**
   * Parses through all tags looking for the search attribute on the tag type
   * (non-Javadoc)
   * 
   * @see org.xml.sax.ContentHandler#startElement(java.lang.String,
   *      java.lang.String, java.lang.String, org.xml.sax.Attributes)
   */
  public void startElement(String namespaceURI, String localName, String fullName, Attributes attributes) throws SAXException
  {
    if (localName.equals(XMLTags.CREATE_TAG))
    {
      manager.enterCreateState();
    }
    else if (localName.equals(XMLTags.UPDATE_TAG))
    {
      manager.enterUpdateState();
    }
    else
    {
      if (!defined && criteria.check(localName, attributes))
      {
        // Creat the handler based upon the tag name
        DefaultHandler handler = getHandler(localName, attributes);

        // Pass control of the parsin to the new handler
        if (handler != null)
        {
          reader.setContentHandler(handler);
          reader.setErrorHandler(handler);
          defined = true;

          // Remove the value from the callStack
          manager.removeSearchId();
        }
      }
    }
  }

  @Override
  public void endElement(String uri, String localName, String qName) throws SAXException
  {
    if (localName.equals(XMLTags.CREATE_TAG) || localName.equals(XMLTags.UPDATE_TAG))
    {
      manager.leavingCurrentState();
    }
  }

  protected DefaultHandler getHandler(String localName, Attributes attributes)
  {
    return createHandlerFactory().getHandler(localName, attributes, reader, this, manager);
  }

  protected HandlerFactoryIF createHandlerFactory()
  {
    if (manager.isCreateState() || manager.isCreateOrUpdateState())
    {
      return new CreateHandlerFactory();
    }

    return new UpdateHandlerFactory();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.xml.sax.ContentHandler#endDocument()
   */
  public void endDocument() throws SAXException
  {
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
