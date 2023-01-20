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
package com.runwaysdk.dataaccess.io.dataDefinition;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.io.XMLParseException;

/**
 * Searches from and defines tags
 * 
 * @author Justin Smethie
 * @date 6/02/06
 */
public class SearchHandler extends SAXSourceParser
{

  /**
   * If the value to search has been found
   */
  private boolean          defined = false;

  private SearchCriteriaIF criteria;

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
   * @throws ParserConfigurationException 
   */
  protected SearchHandler(ImportManager manager, SearchCriteriaIF criteria, String cause) throws SAXException, ParserConfigurationException
  {
    super(manager);

    this.criteria = criteria;

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
   * @see com.runwaysdk.dataaccess.io.dataDefinition.SAXSourceParser#process(com.runwaysdk.dataaccess.io.dataDefinition.TagContext)
   */
  @Override
  protected boolean process(TagContext context)
  {
    TagHandlerIF handler = context.getHandler();
    String qName = context.getLocalName();

    boolean parse = context.isParse();
    boolean modifiesState = handler.modifiesState(qName);

    return ( parse || modifiesState );
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.io.dataDefinition.SAXSourceParser#createContext(java.lang.String, org.xml.sax.Attributes, com.runwaysdk.dataaccess.io.dataDefinition.TagContext,
   * com.runwaysdk.dataaccess.io.dataDefinition.TagHandlerIF)
   */
  @Override
  protected TagContext createContext(String qName, Attributes attributes, TagContext parent, TagHandlerIF handler)
  {
    TagContext context = super.createContext(qName, attributes, parent, handler);

    if (parent != null)
    {
      boolean parse = ( !defined && criteria.check(qName, attributes) ) || parent.isParse();
      context.setParse(parse);
    }
    else
    {
      context.setParse(false);
    }

    return context;
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
    catch (SAXException | ParserConfigurationException e)
    {
      throw new XMLParseException(e);
    }
  }

}
