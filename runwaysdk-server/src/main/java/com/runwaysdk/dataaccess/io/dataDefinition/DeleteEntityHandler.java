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
package com.runwaysdk.dataaccess.io.dataDefinition;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.EntityDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAO;
import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.io.XMLHandler;

/**
 * Parses a {@link XMLTags#OBJECT_TAG} or a {@link XMLTags#RELATIONSHIP_TAG}
 * under the {@link XMLTags#DELETE_TAG}.
 * 
 * @author Justin Smethie
 */
public class DeleteEntityHandler extends XMLHandler
{
  /**
   * Name of the tag being parsed
   */
  private String tag;

  /**
   * Constructs a handler to parse a {@link XMLTags#OBJECT_TAG} under the
   * {@link XMLTags#DELETE_TAG}
   * 
   * @param tag
   *          name of the tag being parsed
   * @param attributes
   *          List of attributes on the tag
   * @param reader
   *          {@link XMLReader} stream reading the .xml document
   * @param previousHandler
   *          The {@link XMLHandler} to return control to after parsing the tag
   * @param manager
   *          Tracks the status of the import.
   */
  public DeleteEntityHandler(String tag, Attributes attributes, XMLReader reader, XMLHandler previousHandler, ImportManager manager)
  {
    super(reader, previousHandler, manager);

    this.tag = tag;
    String type = attributes.getValue(XMLTags.TYPE_ATTRIBUTE);
    String key = attributes.getValue(XMLTags.KEY_ATTRIBUTE);

    deleteEntity(type, key);
  }

  /**
   * Calls the delete method on a {@link BusinessDAO} or a
   * {@link RelationshipDAO}.
   * 
   * @param type
   *          Fully qualified type of the entity being deleted
   * @param key
   *          Key of the entity being deleted
   */
  private final void deleteEntity(String type, String key)
  {
    if (tag.equals(XMLTags.OBJECT_TAG) || tag.equals(XMLTags.RELATIONSHIP_TAG))
    {
      EntityDAOIF entityDAOIF = EntityDAO.get(type, key);

      if (entityDAOIF != null)
      {
        EntityDAO entityDAO = entityDAOIF.getEntityDAO();
        
//        System.out.println(entityDAO.getType());

        entityDAO.delete();
      }
    }
  }

  public void endElement(String namespaceURI, String localName, String fullName) throws SAXException
  {
    if (localName.equals(tag))
    {
      reader.setContentHandler(previousHandler);
      reader.setErrorHandler(previousHandler);
    }
  }
}
