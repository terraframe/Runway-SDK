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
package com.runwaysdk.dataaccess.io.instance;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.runwaysdk.constants.ElementInfo;
import com.runwaysdk.constants.EntityInfo;
import com.runwaysdk.dataaccess.DataAccessException;
import com.runwaysdk.dataaccess.RelationshipDAO;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.io.XMLHandler;
import com.runwaysdk.dataaccess.resolver.IConflictResolver;

/**
 * Handles the <relationship_instance> tag in the instance importer. See
 * http:\\www.runwaysdk.com\schema\instance.xsd for the entire importer schema.
 * Imports the type, parentOid, childOid, and Id attributes of a Relationship.
 * After importing the previous attributes this passes control to the appropiate
 * handler for other system and user defined attributes of the Relationship, eg,
 * owner.
 * 
 * @author Justin Smethie
 */
public class RelationshipHandler extends ElementHandler
{
  /**
   * 
   * @param reader
   *          The XMLReader to parse
   * @param previousHandler
   *          The handler which control passed from
   * @param manager
   *          ImportManager which provides communication between handlers for a
   *          single import
   * @param attributes
   *          The attributes defined inside of the <relationship_instance> tag
   * @throws SAXException
   */
  public RelationshipHandler(XMLReader reader, XMLHandler previousHandler, ImportManager manager, IConflictResolver resolver, Attributes attributes) throws SAXException
  {
    this(0L, reader, previousHandler, manager, resolver, attributes);
  }

  /**
   * 
   * @param importSeq
   *          seqence number of the imported object, or 0 if it is not known.
   * @param reader
   *          The XMLReader to parse
   * @param previousHandler
   *          The handler which control passed from
   * @param manager
   *          ImportManager which provides communication between handlers for a
   *          single import
   * @param attributes
   *          The attributes defined inside of the <relationship_instance> tag
   * @throws SAXException
   */
  public RelationshipHandler(Long importSeq, XMLReader reader, XMLHandler previousHandler, ImportManager manager, IConflictResolver resolver, Attributes attributes) throws SAXException
  {
    this(importSeq, reader, previousHandler, manager, resolver, attributes, false);
  }

  public RelationshipHandler(Long importSeq, XMLReader reader, XMLHandler previousHandler, ImportManager manager, IConflictResolver resolver, Attributes attributes, Boolean ignoreSeq) throws SAXException
  {
    super(reader, previousHandler, manager, resolver, ignoreSeq);

    String type = attributes.getValue(XMLTags.TYPE_TAG);
    String databaseId = attributes.getValue(XMLTags.ID_TAG);
    String parentOid = attributes.getValue(XMLTags.PARENT_OID_TAG);
    String childOid = attributes.getValue(XMLTags.CHILD_OID_TAG);

    // Import a new 'type' Relationship
    try
    {
      this.elementDAO = RelationshipDAO.get(databaseId).getRelationshipDAO();

      this.sequence = elementDAO.getSequence();
      this.siteMaster = elementDAO.getValue(ElementInfo.SITE_MASTER);

      if (ignoreSeq || (importSeq > sequence))
      {
        this.skipProcessing = false;
      }
      else
      {
        this.skipProcessing = true;
      }
    }
    catch (DataAccessException e)
    {
      this.elementDAO = RelationshipDAO.newInstance(parentOid, childOid, type);
      this.isNew = true;

      // Set the imported oid
      Attribute attribute = elementDAO.getAttribute(EntityInfo.OID);
      attribute.setValue(databaseId);

      this.skipProcessing = false;
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.dataaccess.io.instance.EntityHandler#endElement(java.lang
   * .String, java.lang.String, java.lang.String)
   */
  public void endElement(String namespaceURI, String localName, String qName) throws SAXException
  {
    if (qName.equals(XMLTags.RELATIONSHIP_TAG))
    {
      super.endElement(namespaceURI, localName, qName);
    }
  }
}
