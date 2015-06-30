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

import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdEnumerationInfo;
import com.runwaysdk.constants.MdTypeInfo;
import com.runwaysdk.constants.MetadataInfo;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.EnumerationItemDAO;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.database.BusinessDAOFactory;
import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.io.XMLHandler;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdEnumerationDAO;
import com.runwaysdk.dataaccess.metadata.MdTypeDAO;

/**
 * Handles enumeration_filter and children tags, see datatype.xml as a reference
 *
 * @author Justin Smethie
 * @date 6/02/06
 */
public class MdEnumerationHandler extends XMLHandler
{
  /**
   * The BusinessDAO created by the metadata
   */
  private MdEnumerationDAO mdEnumeration;

  /**
   * Creates EntityCache enumeration instances
   *
   * @param attributes
   *            The attributes of the enumeration_filter tag
   * @param reader
   *            The XMLReader stream
   * @param previousHandler
   *            The XMLHandler in which control was passed from
   * @param manager
   *            ImportManager which provides communication between handlers for
   *            a single import
   */
  public MdEnumerationHandler(Attributes attributes, XMLReader reader, XMLHandler previousHandler, ImportManager manager)
  {
    super(reader, previousHandler, manager);

    // Get the MdEnumeration to import, if this is a create then a new instance of MdEnumeration is imported
    mdEnumeration = (MdEnumerationDAO) manager.getEntityDAO(MdEnumerationInfo.CLASS, attributes.getValue(XMLTags.NAME_ATTRIBUTE)).getEntityDAO();

    importEnumFilter(attributes);

    if (!manager.isCreated(mdEnumeration.definesType()))
    {
      mdEnumeration.apply();
      manager.addMapping(mdEnumeration.definesType(), mdEnumeration.getId());
    }
  }

  /**
   * Parses the enumeration_instance tag and the include all tag Inherited from
   * ContentHandler (non-Javadoc)
   *
   * @see org.xml.sax.ContentHandler#startElement(java.lang.String,
   *      java.lang.String, java.lang.String, org.xml.sax.Attributes)
   */
  public void startElement(String namespaceURI, String localName, String fullName, Attributes attributes) throws SAXException
  {
    if (manager.isCreated(mdEnumeration.definesType()))
    {
      return;
    }

    if (localName.equals(XMLTags.INCLUDEALL_TAG))
    {
      //Set the include all attribute to true and reapply the mdEnumeration to the database
      mdEnumeration.setValue(MdEnumerationInfo.INCLUDE_ALL, MdAttributeBooleanInfo.TRUE);
      mdEnumeration.apply();
    }
    // Include a particular instance of the enumeration class in the filter
    else if (localName.equals(XMLTags.ADD_ENUM_ITEM_TAG))
    {
      if (MdAttributeBooleanInfo.TRUE.equals(mdEnumeration.getValue(MdEnumerationInfo.INCLUDE_ALL)))
      {
        mdEnumeration.setValue(MdEnumerationInfo.INCLUDE_ALL, MdAttributeBooleanInfo.FALSE);
        mdEnumeration.apply();
      }

      String masterListType = mdEnumeration.getMasterListMdBusinessDAO().definesType();
      String enumName = attributes.getValue(XMLTags.ENUM_NAME_ATTRIBUTE);
      String enumItemKey = EnumerationItemDAO.buildKey(masterListType, enumName);

      String actualId = "";

      try
      {
        actualId = EntityDAO.getIdFromKey(masterListType, enumItemKey);
      }
      catch(DataNotFoundException e)
      {
        String[] search_tags = { XMLTags.OBJECT_TAG };
//        SearchHandler.searchEntity(manager, search_tags, XMLTags.KEY_ATTRIBUTE, masterListType, enumItemKey);
        SearchHandler.searchEntity(manager, search_tags, XMLTags.KEY_ATTRIBUTE, enumItemKey, mdEnumeration.definesType());
      }

      if (actualId.equals(""))
      {
        actualId = EntityDAO.getIdFromKey(masterListType, enumItemKey);
      }

      if(!mdEnumeration.containsEnumItem(actualId))
      {
        mdEnumeration.addEnumItem(actualId);
      }
    }
    else if (localName.equals(XMLTags.REMOVE_ENUM_ITEM_TAG))
    {
      if (MdAttributeBooleanInfo.TRUE.equals(mdEnumeration.getValue(MdEnumerationInfo.INCLUDE_ALL)))
      {
        mdEnumeration.setValue(MdEnumerationInfo.INCLUDE_ALL, MdAttributeBooleanInfo.FALSE);
        mdEnumeration.apply();
      }

      String masterListType = mdEnumeration.getMasterListMdBusinessDAO().definesType();
      String enumName = attributes.getValue(XMLTags.ENUM_NAME_ATTRIBUTE);
      String enumItemKey = EnumerationItemDAO.buildKey(masterListType, enumName);

      // Get the database ID of a XML puesdo id
      String actualId = EntityDAO.getIdFromKey(masterListType, enumItemKey);

      if(mdEnumeration.containsEnumItem(actualId))
      {
        mdEnumeration.removeEnumItem(actualId);
      }
    }
  }

  /**
   * Creates an MdEnumeration from the parse of the enumeration_filter tag
   * attributes
   *
   * @param attributes
   *            The attributes of an enumeration_filter tag
   * @return A MdEnumeration BusinessDAO
   */
  private final void importEnumFilter(Attributes attributes)
  {
    // Import required attributes
    String enumType = attributes.getValue(XMLTags.NAME_ATTRIBUTE);
    mdEnumeration.setValue(MdTypeInfo.NAME, BusinessDAOFactory.getClassNameFromType(enumType));
    mdEnumeration.setValue(MdTypeInfo.PACKAGE, BusinessDAOFactory.getPackageFromType(enumType));

    ImportManager.setLocalizedValue(mdEnumeration, MdTypeInfo.DISPLAY_LABEL, attributes, XMLTags.DISPLAY_LABEL_ATTRIBUTE);
    ImportManager.setValue(mdEnumeration, MetadataInfo.REMOVE, attributes, XMLTags.REMOVE_ATTRIBUTE);
    ImportManager.setLocalizedValue(mdEnumeration, MetadataInfo.DESCRIPTION, attributes, XMLTags.DESCRIPTION_ATTRIBUTE);
    ImportManager.setValue(mdEnumeration, MdEnumerationInfo.EXPORTED, attributes, XMLTags.EXPORTED_ATTRIBUTE);


    String tableName = attributes.getValue(XMLTags.ENUMERATION_TABLE);
    if (tableName != null)
    {
      mdEnumeration.setTableName(tableName);
    }

    // Import required reference attributes
    String masterType = attributes.getValue(XMLTags.TYPE_ATTRIBUTE);

    if (masterType != null)
    {
      // Make sure that the class being reference has already been defined
      if (!MdTypeDAO.isDefined(masterType))
      {
        // Check if the class is defined later in the xml document
        String[] search_tags = { XMLTags.ENUMERATION_MASTER_TAG };
        SearchHandler.searchEntity(manager, search_tags, XMLTags.NAME_ATTRIBUTE, masterType, mdEnumeration.definesType());
      }

      MdBusinessDAOIF mdBusinessIF = MdBusinessDAO.getMdBusinessDAO(masterType);
      mdEnumeration.setValue(MdEnumerationInfo.MASTER_MD_BUSINESS, mdBusinessIF.getId());
    }
  }

  /**
   * When the enumeration_filter tag is closed: Returns parsing control back to
   * the Handler which passed control Adds the MdEnumeration BusinessDAO to the
   * database
   *
   * Inherits from ContentHandler (non-Javadoc)
   *
   * @see org.xml.sax.ContentHandler#endElement(java.lang.String,
   *      java.lang.String, java.lang.String)
   */
  public void endElement(String namespaceURI, String localName, String fullName) throws SAXException
  {
    if (localName.equals(XMLTags.MD_ENUMERATION_TAG))
    {
      manager.endImport(mdEnumeration.definesType());
      reader.setContentHandler(previousHandler);
      reader.setErrorHandler(previousHandler);
    }
  }
}
