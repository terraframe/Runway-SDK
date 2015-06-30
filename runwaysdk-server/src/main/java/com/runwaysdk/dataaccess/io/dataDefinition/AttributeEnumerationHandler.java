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
import org.xml.sax.XMLReader;

import com.runwaysdk.constants.MdAttributeEnumerationInfo;
import com.runwaysdk.constants.MdAttributeReferenceInfo;
import com.runwaysdk.constants.MdEnumerationInfo;
import com.runwaysdk.dataaccess.ElementDAO;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.EnumerationItemDAO;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeEnumerationDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdEnumerationDAOIF;
import com.runwaysdk.dataaccess.attributes.InvalidAttributeTypeException;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.io.XMLHandler;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdEnumerationDAO;

/**
 * Parses selection tags to instianted an attribute with multiple reference to
 * other instances
 * 
 * @author Justin
 * @date 06/09/06
 */
public class AttributeEnumerationHandler extends XMLHandler
{
  /**
   * The current EntityDAO in scope
   */
  private EntityDAO          current;

  /**
   * The name of the attriubte
   */
  private String             attributeName;

  /**
   * The name of the struct attribute that owns the selection, The selection is
   * not part of a struct attribute if structName=null
   */
  private String             structName = null;

  private MdBusinessDAOIF    mdEnumerationMaster;

  private MdEnumerationDAOIF mdEnumeration;

  /**
   * Creates an instance of a MdAttributeEnumeration.
   * 
   * @param attributes
   *          The attributes of the instance tag
   * @param reader
   *          The XMLReader stream
   * @param previousHandler
   *          The XMLHandler in which control was passed from
   * @param manager
   *          ImportManager which provides communication between handlers for a
   *          single import
   * @param current
   *          The EntityDAO which defines the attribute-multiple id mapping
   */
  public AttributeEnumerationHandler(Attributes attributes, XMLReader reader, XMLHandler previousHandler, ImportManager manager, EntityDAO current) throws SAXException
  {
    super(reader, previousHandler, manager);

    this.current = current;
    this.attributeName = attributes.getValue(XMLTags.ENTITY_ATTRIBUTE_NAME_ATTRIBUTE);
    
    MdAttributeDAOIF mdAttribute = current.getAttributeIF(attributeName).getMdAttribute();
    mdEnumeration = MdEnumerationDAO.get(mdAttribute.getValue(MdAttributeEnumerationInfo.MD_ENUMERATION));
    mdEnumerationMaster = MdBusinessDAO.get(mdEnumeration.getValue(MdEnumerationInfo.MASTER_MD_BUSINESS));
  }

  /**
   * Creates an instance of a MdAttributeEnumeration.
   * 
   * @param attributes
   *          The attributes of the instance tag
   * @param reader
   *          The XMLReader stream
   * @param previousHandler
   *          The XMLHandler in which control was passed from
   * @param manager
   *          ImportManager which provides communication between handlers for a
   *          single import
   * @param current
   *          The EntityDAO which defines the attribute-multiple id mapping
   * @param structName
   *          The name of the struct attribute in which the selection is part of
   */
  public AttributeEnumerationHandler(Attributes attributes, XMLReader reader, XMLHandler previousHandler, ImportManager manager, ElementDAO current, String structName) throws SAXException
  {
    super(reader, previousHandler, manager);

    this.structName = structName;
    this.current = current;
    this.attributeName = attributes.getValue(XMLTags.ENTITY_ATTRIBUTE_NAME_ATTRIBUTE);
    
    MdAttributeDAOIF mdAttribute = current.getAttributeIF(attributeName).getMdAttribute();
    mdEnumeration = MdEnumerationDAO.get(mdAttribute.getValue(MdAttributeEnumerationInfo.MD_ENUMERATION));
    mdEnumerationMaster = mdEnumeration.getMasterListMdBusinessDAO();
  }

  /**
   * Parses the instance_ref tag Inherited from ContentHandler (non-Javadoc)
   * 
   * @see org.xml.sax.ContentHandler#startElement(java.lang.String,
   *      java.lang.String, java.lang.String, org.xml.sax.Attributes)
   */
  public void startElement(String namespaceURI, String localName, String fullName, Attributes attributes) throws SAXException
  {
    MdAttributeDAOIF mdAttributeDAOIF = current.getAttributeIF(attributeName).getMdAttribute();

    if (! ( mdAttributeDAOIF instanceof MdAttributeEnumerationDAOIF ))
    {
      String errMsg = "The attribute [" + this.attributeName + "] on type [" + this.current.getType() + "] is not an enumeration attribute.";

      MdBusinessDAOIF expectedAttributeTypeDefinition = MdBusinessDAO.getMdBusinessDAO(MdAttributeReferenceInfo.CLASS);
      MdBusinessDAOIF givenAttributeTypeDefinition = MdBusinessDAO.getMdBusinessDAO(mdAttributeDAOIF.getType());

      throw new InvalidAttributeTypeException(errMsg, mdAttributeDAOIF, expectedAttributeTypeDefinition, givenAttributeTypeDefinition);
    }

    if (localName.equals(XMLTags.ENUMERATED_ITEM_TAG))
    {
      String masterListType = mdEnumerationMaster.definesType();
      String enumName = attributes.getValue(XMLTags.ENUM_NAME_ATTRIBUTE);
      String enumItemKey = EnumerationItemDAO.buildKey(masterListType, enumName);

      String dataID = "";

      try
      {
        dataID = EntityDAO.getIdFromKey(masterListType, enumItemKey);
      }
      catch (DataNotFoundException e)
      {
        // Define the instance
        String[] search_tags = { XMLTags.OBJECT_TAG };
        SearchHandler.searchEntity(manager, search_tags, XMLTags.KEY_ATTRIBUTE, enumItemKey, current.getKey());
      }

      if (dataID.equals(""))
      {
        dataID = EntityDAO.getIdFromKey(masterListType, enumItemKey);
      }

      // The enumeration instance is not part of a struct attribute
      if (structName == null)
      {
        // Add the database ID to the attribute name
        current.addItem(attributeName, dataID);
      }
      // The enumeration instance is part of a struct attribute
      else
      {
        current.addStructItem(structName, attributeName, dataID);
      }
    }
  }

  /**
   * When the selection is closed: Returns parsing control back to the Handler
   * which passed control
   * 
   * Inherits from ContentHandler (non-Javadoc)
   * 
   * @see org.xml.sax.ContentHandler#endElement(java.lang.String,
   *      java.lang.String, java.lang.String)
   */
  public void endElement(String namespaceURI, String localName, String fullName) throws SAXException
  {
    if (localName.equals(XMLTags.ATTRIBUTE_ENUMERATION_TAG))
    {
      reader.setContentHandler(previousHandler);
      reader.setErrorHandler(previousHandler);
    }
  }

}
