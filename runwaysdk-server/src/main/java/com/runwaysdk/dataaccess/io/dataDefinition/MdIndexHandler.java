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

import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdIndexInfo;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdElementDAOIF;
import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.io.XMLHandler;
import com.runwaysdk.dataaccess.metadata.MdElementDAO;
import com.runwaysdk.dataaccess.metadata.MdIndexDAO;
import com.runwaysdk.dataaccess.metadata.MdTypeDAO;

public class MdIndexHandler extends XMLHandler
{
  /**
   * A prefixs that gurantees that an indexName will be unqiue in the isCreated
   * mapping, if it has not yet been created.
   */
  private static final String PREFIX = "~INDEX_";

  /**
   * The MdIndex being defined
   */
  private MdIndexDAO          mdIndex;

  /**
   * The name of the index being defined plus a prefix to ensure the index name
   * is unique in the isCreated mapping defined in XMLHandler
   */
  private String              indexName;

  /**
   * Flag denoting if the MdIndexDAO is active
   */
  private String              active;

  /**
   * Constructor - Defines a new MdIndex according to the xml attributes parsed
   * 
   * @param attributes
   *          The attibutes of the class tag
   * @param reader
   *          The XMLReader stream
   * @param previousHandler
   *          The Handler which passed control
   * @param manager
   *          ImportManager which provides communication between handlers for a
   *          single import
   */
  public MdIndexHandler(Attributes attributes, XMLReader reader, XMLHandler previousHandler, ImportManager manager)
  {
    super(reader, previousHandler, manager);

    // TODO Fix after a logical key is determined for instances of MdIndex
    mdIndex = MdIndexDAO.newInstance();
    active = attributes.getValue(XMLTags.INDEX_ACTIVE_ATTRIBUTE);

    importMdIndex(attributes);

    indexName = PREFIX + mdIndex.getIndexName();

    // Make sure the MdIndex has not already been defined
    if (!manager.isCreated(indexName))
    {
      mdIndex.apply();
    }
  }

  /**
   * Parses the attribute tag Inherited from ContentHandler (non-Javadoc)
   * 
   * @see org.xml.sax.ContentHandler#startElement(java.lang.String,
   *      java.lang.String, java.lang.String, org.xml.sax.Attributes)
   */
  public void startElement(String namespaceURI, String localName, String fullName, Attributes attributes) throws SAXException
  {
    if (manager.isCreated(indexName))
    {
      return;
    }

    if (localName.equals(XMLTags.INDEX_ATTRIBUTE_TAG))
    {
      String attributeName = attributes.getValue(XMLTags.INDEX_NAME_ATTRIBUTE);
      int indexOrder = Integer.parseInt(attributes.getValue(XMLTags.INDEX_ORDER_ATTRIBUTE));
      String parentId = mdIndex.getValue(MdIndexInfo.MD_ENTITY);

      MdElementDAOIF mdEntity = MdElementDAO.get(parentId);
      MdAttributeDAOIF mdAttribute = mdEntity.definesAttribute(attributeName);

      // IMPORTANT: The mdAttribute may not be defined yet. It could be defined
      // in an update tag later on in the same file, thus if mdAttribute == null
      // we need to ensure that the type of the mdIndex is imported.
      if (mdAttribute == null)
      {
        String type = mdEntity.definesType();
        String[] search_tags = XMLTags.TYPE_TAGS;
        SearchHandler.searchEntity(manager, search_tags, XMLTags.NAME_ATTRIBUTE, type, PREFIX + type);

        mdAttribute = mdEntity.definesAttribute(attributeName);
      }

      mdIndex.addAttribute(mdAttribute, indexOrder);
    }
  }

  /**
   * Creates an MdIndex from the parse of the class tag attributes.
   * 
   * @param attributes
   *          The attributes of an class tag.
   * @return MdIndex from the parse of the class tag attributes.
   */
  private final void importMdIndex(Attributes attributes)
  {
    String entityType = attributes.getValue(XMLTags.INDEX_ENTITY_TYPE_ATTRIBUTE);

    if (!MdTypeDAO.isDefined(entityType))
    {
      String[] searchTags = { XMLTags.MD_BUSINESS_TAG, XMLTags.MD_STRUCT_TAG, XMLTags.MD_LOCAL_STRUCT_TAG, XMLTags.MD_RELATIONSHIP_TAG, XMLTags.MD_TREE_TAG, XMLTags.MD_GRAPH_TAG };
      SearchHandler.searchEntity(manager, searchTags, XMLTags.NAME_ATTRIBUTE, entityType, mdIndex.getKey());
    }

    MdElementDAOIF mdEntity = MdElementDAO.getMdElementDAO(entityType);

    mdIndex.setValue(MdIndexInfo.MD_ENTITY, mdEntity.getId());
    mdIndex.setValue(MdIndexInfo.UNIQUE, attributes.getValue(XMLTags.INDEX_UNIQUE_ATTRIBUTE));
    mdIndex.setValue(MdIndexInfo.ACTIVE, MdAttributeBooleanInfo.FALSE);
    mdIndex.setStructValue(MdIndexInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, attributes.getValue(XMLTags.DISPLAY_LABEL_ATTRIBUTE));
  }

  /**
   * When the class tag is closed: Returns parsing control back to the Handler
   * which passed control
   * 
   * Inherits from ContentHandler (non-Javadoc)
   * 
   * @see org.xml.sax.ContentHandler#endElement(java.lang.String,
   *      java.lang.String, java.lang.String)
   */
  public void endElement(String namespaceURI, String localName, String fullName)
  {
    if (localName.equals(XMLTags.MD_INDEX_TAG))
    {
      mdIndex.setValue(MdIndexInfo.ACTIVE, active);
      mdIndex.apply();

      manager.endImport(indexName);
      reader.setContentHandler(previousHandler);
      reader.setErrorHandler(previousHandler);
    }
  }

}
